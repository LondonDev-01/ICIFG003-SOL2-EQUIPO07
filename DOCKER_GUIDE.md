# Docker Guide — Reservas App

Levantar el stack completo (MySQL 8.4 + Backend Spring Boot) con un solo comando.

---

## Requisitos

- **Docker Engine** 24+ (https://docs.docker.com/engine/install/)
- **Docker Compose** V2 (incluido con Docker Desktop / Docker Engine)

Verificar instalacion:

```bash
docker --version
docker compose version
```

---

## Setup rapido

```bash
# 1. Clonar el repo (si no lo hiciste)
git clone <repo-url>
cd ICIFG003-SOL2-EQUIPO07

# 2. Crear archivo de variables de entorno
cp .env.example .env

# 3. Arrancar todo
docker compose up --build
```

Esperar unos minutos (la primera vez Maven descarga dependencias). El backend queda disponible en `http://localhost:8080`.

---

## Referencia del archivo `.env`

### Variables MySQL

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `MYSQL_ROOT_PASSWORD` | `rootpass` | Password del usuario `root` de MySQL |
| `MYSQL_DATABASE` | `reservas_db` | Nombre de la base de datos (se crea automaticamente al iniciar el contenedor) |
| `MYSQL_USER` | `reservas_user` | Usuario de aplicacion que crea MySQL |
| `MYSQL_PASSWORD` | `reservas_pass` | Password del usuario de aplicacion |

### Variables de conexion (backend)

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `DB_NAME` | `reservas_db` | Base de datos (debe coincidir con `MYSQL_DATABASE`) |
| `DB_USER` | `reservas_user` | Usuario (debe coincidir con `MYSQL_USER`) |
| `DB_PASSWORD` | `reservas_pass` | Password (debe coincidir con `MYSQL_PASSWORD`) |

> `DB_HOST` y `DB_PORT` no van en `.env` — Docker Compose los setea automaticamente a `db` y `3306`.

### Variables JWT

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `JWT_SECRET` | `ZmFrZS1zZWNyZXQ...` | Clave secreta para firmar tokens JWT (cambiar en produccion) |
| `JWT_EXPIRATION_HOURS` | `1` | Horas de validez del token |
| `JWT_ISSUER` | `reservas-app` | Emisor del token (`iss` claim) |

### Variables de rate limiting

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `RATELIMIT_MAX_ATTEMPTS` | `5` | Maximos intentos de login fallidos antes del bloqueo |
| `RATELIMIT_WINDOW_SECONDS` | `60` | Ventana de tiempo (segundos) para contar intentos |
| `RATELIMIT_LOCKOUT_MINUTES` | `15` | Duracion del bloqueo (minutos) |

> **Nunca commitees el archivo `.env`.** `.gitignore` ya lo protege, pero revisa antes de hacer `git add`. Usa `.env.example` como template.

---

## Credenciales de prueba

| Campo | Valor |
|-------|-------|
| RUT | `11.111.111-1` |
| Password | `1234` |

---

## Comandos utiles

```bash
# Primera vez o despues de cambiar codigo
docker compose up --build

# Si ya esta todo construido
docker compose up

# En background
docker compose up --build -d

# Ver logs del backend
docker compose logs -f backend

# Ver logs de MySQL
docker compose logs -f db

# Seguir ambos logs
docker compose logs -f

# Entrar a la consola de MySQL
docker compose exec db mysql -u root -p

# Ver el estado de los servicios
docker compose ps

# Detener servicios (sin borrar datos)
docker compose down

# Detener y borrar el volumen de datos (empezar de cero)
docker compose down -v

# Reconstruir la imagen del backend sin cache
docker compose build --no-cache backend

# Validar la configuracion de compose
docker compose config
```

### Spring Boot devtools (recarga automatica)

Si queres hot-reload mientras desarrollas, podes arrancar el backend localmente:

```bash
cd backend
./mvnw spring-boot:run -DskipTests
```

El backend se conecta a MySQL via Docker (el contenedor `reservas-db` debe estar corriendo).

---

## Troubleshooting

### Puerto 3306 ya esta en uso

```bash
# Averiguar que proceso lo ocupa
sudo lsof -i :3306  # Linux / macOS
netstat -ano | findstr :3306  # Windows

# Si tenes MySQL local corriendo, detenerlo
sudo systemctl stop mysql  # Linux
brew services stop mysql   # macOS
```

### Puerto 8080 ya esta en uso

Cambiar el puerto expuesto en `docker-compose.yml`:

```yaml
ports:
  - "8081:8080"   # host:container
```

### Backend no conecta a MySQL

Verificar que MySQL este healthy:

```bash
docker compose ps
docker compose logs db
```

Si MySQL falla, revisar credenciales en `.env` — `DB_USER`/`DB_PASSWORD` deben coincidir con `MYSQL_USER`/`MYSQL_PASSWORD`.

### Error "Table doesn't exist"

Si ves errores de tablas no encontradas, el `PhysicalNamingStrategyStandardImpl` puede no estar activo. Verificar en `application.properties`:

```properties
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
```

Esto mantiene los nombres exactos de `@Table`/`@Column` en vez de convertirlos a minusculas.

### Quiero empezar desde cero (base limpia)

```bash
docker compose down -v   # borra volumen de datos
docker compose up --build  # recrea todo
```

### Docker build falla por falta de memoria

```bash
# Limitar memoria de Maven en el build
docker compose build --memory=2g backend
```

---

## Arquitectura

```
┌──────────────┐     ┌──────────────────┐
│   Frontend   │     │  Backend (8080)   │
│   Angular    │────▶│  Spring Boot      │
│  localhost:4200│    │  reservas-backend  │
└──────────────┘     └────────┬─────────┘
                              │
                              │ DB_HOST=db:3306
                              │
                      ┌──────▼───────┐
                      │   MySQL 8.4   │
                      │  reservas-db  │
                      │  puerto 3306   │
                      └──────────────┘
                              │
                      ┌──────▼───────┐
                      │  db_data      │
                      │  (volume)     │
                      └──────────────┘
```

La red interna `reservas-net` aísla los contenedores. El frontend Angular se ejecuta localmente (no dockerizado) y apunta a `http://localhost:8080/api`.
