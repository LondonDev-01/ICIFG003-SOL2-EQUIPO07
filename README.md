# Proyecto Reservas - Equipo 07

Este repositorio contiene dos componentes principales: el backend (Spring Boot, Maven) y el frontend (Angular).

**Descripción**: Aplicación para gestionar reservas de salas con API REST y una interfaz Angular.

**Requisitos**:
- **Java 17**: JDK 17 instalado y `JAVA_HOME` configurado.
- **Maven** (opcional): Si no desea usar el wrapper, instale Maven.
- **Node.js & npm**: Recomendado Node 18+ y npm. El frontend usa Angular 21.
- **PostgreSQL** (opcional): Por defecto la configuración apunta a una base PostgreSQL local.

**Estructura**:
- `backend/` - API Spring Boot (Maven)
- `frontend/` - Aplicación Angular

**Arrancar el backend (Windows)**
1. Abrir PowerShell o cmd
2. Ir al directorio del backend:

```powershell
cd backend
```

3a. Ejecutar con el wrapper (recomendado):

```powershell
.\mvnw.cmd spring-boot:run
```

3b. O con Maven instalado globalmente:

```powershell
mvn spring-boot:run
```

4. El servicio arrancará por defecto en `http://localhost:8080`.

5. Para crear el JAR:

```powershell
.\mvnw.cmd clean package
java -jar target\reservas-0.0.1-SNAPSHOT.jar
```

**Notas de configuración (backend)**
- Archivo de configuración: `backend/src/main/resources/application.properties`.
- Por defecto está configurado para PostgreSQL en `jdbc:postgresql://localhost:5432/test` con usuario `postgres` y contraseña `1234`.
- Si no quiere usar PostgreSQL, puede cambiar la configuración para usar H2 (la dependencia de H2 ya está incluida en `pom.xml`).

**Arrancar el frontend**
1. Abrir terminal y moverse al directorio del frontend:

```powershell
cd frontend
```

2. Instalar dependencias:

```powershell
npm install
```

3. Ejecutar la app Angular (desarrollo):

```powershell
npm start
```

4. El frontend por defecto servirá en `http://localhost:4200`.

**Construcción para producción (frontend)**

```powershell
npm run build
```

Los archivos de salida quedarán en `frontend/dist/`.

**Pruebas**
- Backend: ejecutar tests con Maven: `.\mvnw.cmd test`.
- Frontend: `npm test`.

**Desarrollo y debugging**
- Asegúrese de que backend y frontend no usen puertos en conflicto (8080 y 4200 por defecto).
- Si necesita habilitar CORS o cambiar el `application.properties`, edite `backend/src/main/resources/application.properties`.

**Contactos y colaboradores**
- Equipo 07

Si quiere que añada pasos de despliegue, Docker, o instrucciones para cambiar la DB a H2 automáticamente, dímelo y lo añado.
