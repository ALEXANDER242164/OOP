Deployment to Railway (using GitHub)
===================================

This file contains step-by-step instructions to deploy this Spring Boot application to Railway using your existing Railway database.

Files added to repo:
- `Dockerfile` - multi-stage build that creates the Spring Boot jar and produces a runtime image.
- `.dockerignore` - to speed up Docker builds.
- `Procfile` - optional, for PaaS that use a Procfile.

Recommended approach (Railway)
------------------------------
1) Push this branch (`alexVersion`) to GitHub.

   From the project root run (adjust remote name if needed):

```powershell
git checkout alexVersion
git add Dockerfile .dockerignore Procfile DEPLOYMENT.md
git commit -m "chore: add Dockerfile and deployment guide"
git push origin alexVersion
```

2) On Railway:
   - Create a new project -> "Deploy from GitHub".
   - Connect your GitHub account, choose this repository and the branch `alexVersion`.
   - Railway will detect your Dockerfile and build the image. Alternatively you can set the build command to `mvn -DskipTests package` and the start command to `java -jar target/*.jar`.

3) Environment variables (important): set the following in Railway project settings (ENV):
   - `MYSQLHOST` (your Railway DB host)
   - `MYSQLPORT` (your Railway DB port)
   - `MYSQLDATABASE` (database name)
   - `MYSQLUSER` (db username)
   - `MYSQLPASSWORD` (db password)
   - `spring.mail.host` (optional, SMTP host)
   - `spring.mail.port` (optional)
   - `spring.mail.username` (optional)
   - `spring.mail.password` (optional)

   Note: The application reads DB settings from `application.properties` using environment variables like `${MYSQLHOST:localhost}`. That is why we expose these exact variable names.

4) Ports and start command:
   - The app is configured to run on `server.port=${PORT:8081}` in `application.properties`. Railway will inject the `PORT` variable automatically.
   - If Railway builds with the `Dockerfile`, no extra start command is needed (ENTRYPOINT is already set).
   - The `Procfile` provides an alternative start command if needed.

5) **Frontend Configuration (HTML/CSS)**:
   - ✅ The application serves static files (HTML, CSS, JS) from `src/main/resources/static/`
   - ✅ The root URL `/` automatically serves `index.html`
   - ✅ All static resources are packaged in the JAR during build
   - ✅ Spring Boot is configured to serve static content with:
     ```properties
     spring.web.resources.static-locations=classpath:/static/
     spring.web.resources.add-mappings=true
     spring.mvc.static-path-pattern=/**
     ```
   - The `StaticController` handles the root path and serves `index.html` with CSS properly loaded.

6) Test the deployment:
   - **Frontend**: Open the Railway service URL (e.g., `https://your-app.up.railway.app/`)
     - You should see the HTML interface with CSS styling from `diseno.css`
     - Open browser DevTools (F12) > Network tab to verify `/diseno.css` loads with status 200
   - **API endpoints**: Test REST endpoints:
     - `GET /api/appointments` - List appointments
     - `GET /api/catalog/therapists` - List therapists
     - `GET /swagger-ui.html` - API documentation
   - **Logs**: Check Railway logs if the app fails to start (common issues: DB credentials, missing env vars, SMTP misconfiguration, PORT not configured).

7) Verify static files in JAR (before deploying):
   ```powershell
   # Build the project
   .\mvnw.cmd clean package -DskipTests
   
   # Verify static files are included
   jar -tf target\springboot-0.0.1-SNAPSHOT.jar | Select-String "static/"
   
   # You should see:
   # BOOT-INF/classes/static/
   # BOOT-INF/classes/static/diseno.css
   # BOOT-INF/classes/static/index.html
   ```

Notes and alternatives
----------------------
- If you prefer to use Railway's "build from source" (Maven) rather than Docker, set the build command to `mvn -DskipTests package` and start command to `java -jar target/*.jar`.
- If you want automatic deployments via GitHub Actions, I can add a workflow that builds the Docker image and triggers Railway deploys; you will need to provide Railway's deploy token as a GitHub secret.
- I cannot push to your GitHub or connect to Railway for you without your credentials/access. I prepared these files and the exact commands you need to run locally; if you give me temporary access or add me as a collaborator I can open a PR for you.

If you want, I can also:
- Add a GitHub Actions workflow template to build and publish an image (requires secrets).
- Create a Flyway migration for the `reminder_sent` column instead of relying on `hibernate.ddl-auto=update`.
- Help you set Railway environment variables (I can prepare a list in the UI-friendly form to paste into Railway).

GitHub Actions (CI) - Automatic build & publish to GHCR
-----------------------------------------------------
I added a workflow file at `.github/workflows/ci-build-and-publish-ghcr.yml` that runs on pushes to the `alexVersion` branch. What it does:

- Builds the project with Maven (`mvn -DskipTests package`).
- Builds a Docker image and pushes it to GitHub Container Registry (GHCR) as `ghcr.io/<OWNER>/<REPO>:alexVersion`.

How to use it:

1. Push your `alexVersion` branch to GitHub (see previous steps).
2. The Actions workflow will run automatically and push the image to GHCR using the `GITHUB_TOKEN`.
3. In Railway (or any other host), point the deployment to the GHCR image `ghcr.io/<OWNER>/<REPO>:alexVersion`.

Notes:
- GHCR publishing in this workflow uses `${{ secrets.GITHUB_TOKEN }}` which usually has permission to write packages for the repository. If the push is rejected, you may need to create a Personal Access Token (PAT) with `write:packages` and set it as `CR_PAT` or a secret and adjust the workflow.
- Alternatively, you can push to Docker Hub; if you prefer that, tell me and I will add a workflow step that logs into Docker Hub and pushes the image (you'll need to add `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` as repo secrets).

Deploying from GHCR to Railway
-----------------------------
1. In Railway, create or open your project. Choose "Deploy from Container Registry" (or similar option).
2. Select GitHub Container Registry and provide the image `ghcr.io/<OWNER>/<REPO>:alexVersion`.
3. Set environment variables in Railway as described earlier (`MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`, and optionally the SMTP vars).
4. Start the deployment and monitor logs.

