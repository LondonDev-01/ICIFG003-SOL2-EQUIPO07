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

# **Configuración de Base de Datos**

1. Abrir PostgreSQL (psql):

```sql
psql -U postgres
```

2. Crear la base de datos:

```sql
CREATE DATABASE test;
```

**Importante:** No es necesario crear tablas ni insertar datos manualmente. El sistema genera automáticamente toda la estructura y los datos de prueba al iniciar el backend.

---

## **Arrancar el Backend (Windows)**

1. Abrir PowerShell o CMD.
2. Ir al directorio del backend:

```powershell
cd backend
```

3. Ejecutar Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

o alternativamente:

```powershell
mvn spring-boot:run
```

4. El backend quedará disponible en:

```text
http://localhost:8080
```

### **¿Qué ocurre al iniciar el backend?**

- Hibernate crea automáticamente las tablas.
- Se ejecuta el archivo `data.sql`.
- Se cargan carreras, edificios, salas, horarios, estudiantes y reservas de prueba.

---

## **Arrancar el Frontend**

1. Abrir una nueva terminal.
2. Ir al directorio del frontend:

```powershell
cd frontend
```

3. Instalar dependencias:

```powershell
npm install
```

4. Ejecutar Angular:

```powershell
ng serve
```

o:

```powershell
npm start
```

5. El frontend quedará disponible en:

```text
http://localhost:4200
```

---

## **Credenciales de Prueba**

Para probar el sistema puede utilizar el siguiente usuario:

**RUT**

```text
11.111.111-1
```

**Contraseña**

```text
1234
```

---
