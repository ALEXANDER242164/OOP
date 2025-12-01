# Guía de Despliegue en Railway

## ✅ Configuración Completada para Railway

Este proyecto Spring Boot está configurado para servir correctamente el frontend HTML/CSS cuando se despliega en Railway.

### 🎯 Archivos Configurados

1. **`application.properties`**: 
   - Puerto dinámico: `server.port=${PORT:8081}`
   - Recursos estáticos configurados explícitamente
   - Variables de entorno para MySQL configuradas

2. **`Dockerfile`**: 
   - Build multi-stage con Maven
   - Usa variable `PORT` de Railway
   - Incluye todos los recursos static en el JAR

3. **`Procfile`**: 
   - Comando optimizado para Railway
   - Puerto dinámico configurado

4. **`StaticController.java`**: 
   - Sirve `index.html` en la ruta raíz `/`
   - Maneja correctamente archivos CSS

### 📦 Recursos Static Incluidos

- ✅ `index.html` - Página principal
- ✅ `diseno.css` - Estilos de la aplicación
- ✅ Todos los archivos en `src/main/resources/static/` se empaquetan automáticamente

### 🚀 Pasos para Desplegar en Railway

#### 1. Crear Nuevo Proyecto en Railway

```bash
# Opción A: Desde GitHub (Recomendado)
1. Ve a railway.app y crea una cuenta
2. Click en "New Project" > "Deploy from GitHub repo"
3. Selecciona tu repositorio ALEXANDER242164/OOP
4. Railway detectará automáticamente el Dockerfile
```

```bash
# Opción B: Desde CLI de Railway
railway login
railway init
railway up
```

#### 2. Configurar Variables de Entorno

En el dashboard de Railway, añade estas variables:

**Base de Datos MySQL (Railway proporciona estas automáticamente si añades MySQL):**
- `MYSQLHOST` - Host de la base de datos
- `MYSQLPORT` - Puerto (usualmente 3306)
- `MYSQLDATABASE` - Nombre de la base de datos
- `MYSQLUSER` - Usuario
- `MYSQLPASSWORD` - Contraseña

**Opcional - Correo (si usas notificaciones):**
- `SPRING_MAIL_HOST` - smtp.gmail.com
- `SPRING_MAIL_PORT` - 587
- `SPRING_MAIL_USERNAME` - tu-email@gmail.com
- `SPRING_MAIL_PASSWORD` - tu-app-password

#### 3. Añadir Base de Datos MySQL

1. En tu proyecto Railway, click en "New" > "Database" > "Add MySQL"
2. Railway inyectará automáticamente las variables de entorno
3. La aplicación se conectará automáticamente

#### 4. Verificar el Despliegue

Después del despliegue:

1. **Frontend**: Visita la URL de Railway (ej: `https://tu-app.up.railway.app/`)
   - Debe cargar `index.html` con los estilos de `diseno.css`

2. **API REST**: Prueba los endpoints:
   - `GET /api/appointments` - Lista de citas
   - `GET /api/catalog/therapists` - Terapeutas
   - `GET /swagger-ui.html` - Documentación Swagger

3. **Logs**: Revisa los logs en Railway para verificar:
   ```
   Tomcat started on port(s): XXXX (http)
   Started SpringbootApplication in X seconds
   ```

### 🔧 Solución de Problemas

#### El frontend no carga (página en blanco):

1. Verifica que los archivos static estén en el JAR:
   ```bash
   jar -tf target/springboot-0.0.1-SNAPSHOT.jar | grep "static/"
   ```

2. Revisa logs de Railway:
   ```
   railway logs
   ```

3. Verifica que el puerto esté configurado correctamente:
   - Railway asigna el puerto automáticamente vía `$PORT`
   - La app debe escuchar en ese puerto

#### CSS no se carga:

1. Abre DevTools del navegador (F12) > Network
2. Verifica que `/diseno.css` retorne 200 (no 404)
3. El controlador `StaticController` maneja explícitamente este archivo

#### Error de conexión a base de datos:

1. Verifica que MySQL esté añadido al proyecto Railway
2. Confirma que las variables de entorno estén inyectadas:
   ```bash
   railway variables
   ```

### 📋 Checklist Pre-Despliegue

- [x] Dockerfile configurado con multi-stage build
- [x] Procfile con comando correcto
- [x] application.properties usa variables de entorno
- [x] Recursos static en `src/main/resources/static/`
- [x] StaticController sirve `/` como `index.html`
- [x] Puerto dinámico configurado (`${PORT:8081}`)
- [x] Build exitoso: `mvn clean package`
- [x] JAR incluye archivos static

### 🌐 URLs Después del Despliegue

- **Frontend**: `https://tu-app.up.railway.app/`
- **API**: `https://tu-app.up.railway.app/api/appointments`
- **Swagger UI**: `https://tu-app.up.railway.app/swagger-ui.html`
- **OpenAPI JSON**: `https://tu-app.up.railway.app/v3/api-docs`

### 💡 Notas Adicionales

- Railway ejecuta el `Dockerfile` automáticamente si lo detecta
- El `Procfile` es alternativo (Railway prefiere Dockerfile)
- Los cambios en `alexVersion` branch se pueden desplegar automáticamente si configuras el auto-deploy
- Recomendación: usa el branch `main` o `master` para production en Railway

### 🔄 Re-deploys

Para actualizar la aplicación después de cambios:

```bash
git add .
git commit -m "Actualización de frontend"
git push origin alexVersion
```

Railway detectará el push y re-desplegará automáticamente si tienes configurado el auto-deploy.

---

**¡Todo listo para desplegar! 🚀**
