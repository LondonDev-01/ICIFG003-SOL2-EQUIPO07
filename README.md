# Proyecto Reservas - Equipo 07

Este repositorio contiene dos componentes principales: el backend (Spring Boot, Maven) y el frontend (Angular).

**Descripcion**: Aplicacion para gestionar reservas de salas con API REST y una interfaz Angular.

**Requisitos**:
- **Java 17**: JDK 17 instalado y `JAVA_HOME` configurado (solo para ejecucion local).
- **Maven** (opcional): Si no desea usar el wrapper, instale Maven.
- **Node.js & npm**: Recomendado Node 18+ y npm. El frontend usa Angular 21.
- **Docker + Docker Compose** (recomendado): Para levantar el stack completo (MySQL 8.4 + backend).

**Estructura**:
- `backend/` - API Spring Boot (Maven)
- `frontend/` - Aplicacion Angular

---

# **Guia Rapida para Windows (desde cero)**

Esta guia muestra como ejecutar el proyecto completo en un PC con Windows, desde la clonacion hasta tener backend, base de datos y frontend corriendo. Elija una de las dos opciones.

> **Nota sobre comandos:** En Windows se usa `copy` en lugar de `cp` para copiar archivos. `copy` funciona tanto en CMD como en PowerShell.

## Opcion 1: Stack completo con Docker (recomendado)

Requisitos: Docker Desktop para Windows instalado y corriendo.

1. Clonar unicamente la rama deseada y entrar a la carpeta:

```powershell
git clone --branch <rama> --single-branch <url-del-repo>
cd ICIFG003-SOL2-EQUIPO07
```

2. Copiar el archivo de variables de entorno:

```powershell
copy .env.example .env
```

3. (Opcional) Editar `.env` si desea cambiar credenciales o el secreto JWT.

4. Levantar el stack completo (backend, MySQL y frontend):

```powershell
docker compose up --build
```

5. Abrir el frontend en el navegador:

```text
http://localhost:4200
```

El backend queda en `http://localhost:8080` y MySQL escucha en el puerto `3306`.

6. Para detener los servicios:

```powershell
docker compose down
```

## Opcion 2: Ejecucion local (sin Docker)

Requisitos: Java 17 con `JAVA_HOME` configurado, Node.js 18+ y MySQL 8 instalado localmente.

### Base de datos

1. Abrir MySQL:

```powershell
mysql -u root -p
```

2. Crear la base de datos:

```sql
CREATE DATABASE reservas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Salir de MySQL con `exit`. Ajuste `backend/src/main/resources/application.properties` o las variables de entorno con su usuario/contrasena local.

### Backend

1. Abrir PowerShell o CMD en la raiz del proyecto y entrar a `backend`:

```powershell
cd backend
```

2. Ejecutar Spring Boot con el wrapper de Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

o, si tiene Maven instalado globalmente:

```powershell
mvn spring-boot:run
```

3. El backend queda disponible en `http://localhost:8080`. Hibernate crea las tablas y `data.sql` carga los datos de prueba automaticamente.

### Frontend

1. Abrir una nueva terminal y entrar a `frontend`:

```powershell
cd frontend
```

2. Instalar dependencias:

```powershell
npm install
```

3. Iniciar Angular:

```powershell
npm start
```

4. Abrir el frontend en el navegador:

```text
http://localhost:4200
```

> **Importante:** No es necesario crear tablas ni insertar datos manualmente. El backend genera automaticamente toda la estructura y los datos de prueba al iniciar.

Una vez corriendo, use las credenciales de prueba de la seccion [Credenciales de Prueba](#credenciales-de-prueba).

---

# **Configuracion de Base de Datos**

La aplicacion usa **MySQL 8.4**. Puede ejecutarla facilmente con Docker Compose (recomendado) o instalar MySQL localmente.

## Opcion A: MySQL con Docker Compose (recomendado)

No es necesario crear la base de datos manualmente. Docker Compose la crea automaticamente al iniciar el contenedor `db`.

1. Copie el archivo de variables de entorno:

Linux/macOS:

```bash
cp .env.example .env
```

Windows (CMD o PowerShell):

```powershell
copy .env.example .env
```

2. (Opcional) Ajuste los valores en `.env`.

## Opcion B: MySQL local (sin Docker)

1. Abrir MySQL:

```bash
mysql -u root -p
```

2. Crear la base de datos:

```sql
CREATE DATABASE reservas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Actualice `backend/src/main/resources/application.properties` o las variables de entorno segun su usuario/contrasena local.

**Importante:** No es necesario crear tablas ni insertar datos manualmente. El sistema genera automaticamente toda la estructura y los datos de prueba al iniciar el backend.

---

## **Ejecutar con Docker**

Levanta el backend y la base de datos MySQL con un solo comando:

```bash
docker-compose up --build
```

- El backend estara disponible en: `http://localhost:8080`
- MySQL escucha en el puerto `3306` del host.
- El backend espera a que MySQL pase el healthcheck antes de iniciarse.

Para detener los servicios:

```bash
docker-compose down
```

Para detener y eliminar el volumen de datos (base de datos limpia):

```bash
docker-compose down -v
```

---

## **Ejecucion Local (sin Docker)**

### **Arrancar el Backend (Windows)**

1. Asegurese de tener MySQL corriendo localmente y la base de datos `reservas_db` creada.
2. Abrir PowerShell o CMD.
3. Ir al directorio del backend:

```powershell
cd backend
```

4. Ejecutar Spring Boot:

```powershell
.\mvnw.cmd spring-boot:run
```

o alternativamente:

```powershell
mvn spring-boot:run
```

5. El backend quedara disponible en:

```text
http://localhost:8080
```

### **Que ocurre al iniciar el backend?**

- Hibernate crea automaticamente el esquema (tablas y restricciones) desde las entidades JPA al iniciar la aplicacion.
- Se ejecuta el archivo `data.sql`, que carga los datos iniciales mediante sentencias DML.
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

5. El frontend quedara disponible en:

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

**Contrasena**

```text
1234
```

---
