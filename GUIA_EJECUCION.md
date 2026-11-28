# Guía de Ejecución — TP Diseño de Sistemas 2026 (Grupo 30)

Esta guía detalla los pasos para levantar la base de datos, el backend y el frontend del sistema, junto con las direcciones generadas y los datos de acceso para probar la aplicación.

---

## Requisitos previos

- **Docker** y **Docker Compose** (para la base de datos PostgreSQL)
- **Java 21** (para el backend Spring Boot)
- **Node.js** y **npm** (para el frontend Next.js)

---

## 1. Levantar la Base de Datos (PostgreSQL)

Desde la raíz del proyecto, ingresar a la carpeta `database/` y levantar el contenedor de PostgreSQL 17:

```bash
cd database
docker compose up -d postgres
```

*(Opcional)* Si también se desea levantar **pgAdmin 4**:

```bash
docker compose up -d
```

### Datos de conexión generados
- **PostgreSQL 17:** `localhost:5433`
  - Base de datos: `capital_humano`
  - Usuario / Contraseña: `postgres` / `postgres`
- **pgAdmin 4 (opcional):** [http://localhost:5050](http://localhost:5050)
  - Email / Contraseña: `admin@tp.com` / `admin`

> **Nota:** El esquema de tablas y los datos iniciales (`backend/src/main/resources/data.sql`) se cargan automáticamente cuando inicia el backend.

---

## 2. Levantar el Backend (Spring Boot)

En otra terminal, desde la raíz del proyecto, ingresar a `backend/` y ejecutar el wrapper de Maven:

- **Linux / macOS:**
  ```bash
  cd backend
  ./mvnw spring-boot:run
  ```
  *(Si `JAVA_HOME` apunta a otra versión de Java, especificar Java 21, por ejemplo: `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./mvnw spring-boot:run`)*

- **Windows (PowerShell / CMD):**
  ```cmd
  cd backend
  mvnw.cmd spring-boot:run
  ```

### Direcciones generadas por el Backend
- **API REST:** `http://localhost:8080/api`
- **Swagger UI (documentación interactiva):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 3. Levantar el Frontend (Next.js)

En otra terminal, desde la raíz del proyecto, ingresar a `frontend/`, instalar dependencias (solo la primera vez) y levantar el servidor de desarrollo:

```bash
cd frontend
npm install
npm run dev
```

### Dirección generada por el Frontend
- **Aplicación web:** [http://localhost:3000](http://localhost:3000)

---

## 4. A dónde ingresar para usar el sistema

Abrir el navegador e ingresar a:

👉 **[http://localhost:3000](http://localhost:3000)**

Desde la pantalla principal se puede acceder a **"Iniciar sesión"** (`/login`) y luego a los módulos de **Consultor** y **Candidato**.

### Credenciales y datos de prueba (`data.sql`)
- **Consultor:**
  - Usuario: `ADMIN` (o `JUAN`)
  - Contraseña: `admin123` *(modo desarrollo con `app.ldap.enabled=false`)*
- **Candidato:**
  - Ingresar con tipo de documento, número de documento y la clave generada al crear una evaluación en **Evaluar candidatos** (CU025), o utilizando los cuestionarios precargados en `backend/src/main/resources/data.sql`.
- **Puesto de prueba para Orden de Mérito (CU028):**
  - Código: `P001` (`ANALISTA DE DATOS`, empresa `GRINGOTTS`).
