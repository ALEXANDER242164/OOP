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
   - The app is configured to run on `server.port=8081` in `application.properties`. Railway will map external port to container port automatically.
   - If Railway builds with the `Dockerfile`, no extra start command is needed (ENTRYPOINT is already set).

5) Test the deployment:
   - After Railway finishes the build and deploy, open the service URL and verify the UI loads.
   - Check logs in Railway if the app fails to start (common issues: DB credentials, missing env vars, SMTP misconfiguration).

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

