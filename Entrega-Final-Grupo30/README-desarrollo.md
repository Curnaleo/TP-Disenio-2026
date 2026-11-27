# TP Diseño de Sistemas 2026 — Grupo 30

Sistema de gestión de evaluaciones de candidatos para puestos de trabajo.

## Estado actual

- **Base de datos:** PostgreSQL 17 mediante Docker ([`database/docker-compose.yml`](database/docker-compose.yml)), con esquema generado por Hibernate (`ddl-auto=create`) y datos iniciales en [`backend/src/main/resources/data.sql`](backend/src/main/resources/data.sql).
- **Backend:** API REST en Spring Boot 4.1.3 (Java 21), Spring Data JPA, Spring Security (JWT stateless), PostgreSQL Driver y Lombok, en el paquete `com.grupo30.capitalhumano.evaluaciones`.
- **Frontend:** Aplicación web en Next.js 15, React 19, TypeScript y Tailwind CSS 4 en [`frontend/`](frontend/).
- **Documentación:** Diagramas (DER, clases, secuencia), enunciado y devoluciones en [`docs/README.md`](docs/README.md).

## Stack

| Componente | Versión / Tecnología |
| --- | --- |
| Java | 21 (`pom.xml`) |
| Spring Boot | 4.1.3 |
| Maven Wrapper | 3.10.1 (`backend/.mvn/wrapper/maven-wrapper.properties`) |
| Persistencia y seguridad | Spring Data JPA, Spring Security (JWT), Validation, PostgreSQL Driver |
| Frontend | Next.js 15.5.29, React 19.1.0, TypeScript 5, Tailwind CSS 4 |
| Base de datos | PostgreSQL 17 (puerto `5433`) y pgAdmin 4 (puerto `5050`) |

## Estructura del repositorio

```text
TP-Disenio-2026/
├── backend/
│   ├── src/main/java/com/grupo30/capitalhumano/evaluaciones/
│   │   ├── autenticacion/   # JWT, SecurityConfig, AuthController, LDAP simulado
│   │   ├── config/          # OpenAPI / Swagger
│   │   ├── controller/      # Pregunta, Competencia, OpcionRespuesta, Evaluacion, Cuestionario, Reporte
│   │   ├── dao/             # Interfaces DAO
│   │   ├── dto/             # DTOs de entrada/salida
│   │   ├── entity/          # Entidades JPA
│   │   ├── exception/       # GlobalExceptionHandler y excepciones de negocio
│   │   ├── repository/      # Implementaciones JPA de los DAO
│   │   └── service/         # Gestores de negocio
│   └── src/main/resources/
│       ├── application.properties
│       └── data.sql         # Datos iniciales de prueba
├── frontend/
│   └── src/
│       ├── app/             # Rutas App Router (login, menú, consultores, cuestionario)
│       ├── components/      # Componentes UI reutilizables (popups)
│       ├── constants/       # Límites y constantes de validación
│       ├── features/        # Componentes específicos de dominio
│       ├── lib/             # apiClient (fetch + JWT) y manejo de sesión
│       ├── services/        # Servicios HTTP hacia la API REST
│       └── types/           # Tipos TypeScript alineados con los DTOs
├── database/
│   └── docker-compose.yml   # Servicios postgres y pgadmin
├── docs/                    # DER, diagramas de clases, secuencia, vistas y entregas
└── README.md
```

## Cómo ejecutar el proyecto

### 1. Base de datos (PostgreSQL)

```bash
cd database
docker compose up -d postgres
```

*(Opcional: `docker compose up -d` para levantar también pgAdmin en `http://localhost:5050`.)*

### 2. Backend (Spring Boot)

```bash
cd backend
./mvnw spring-boot:run
```

En Linux, si `JAVA_HOME` apunta a otra versión, especificar Java 21:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 ./mvnw spring-boot:run
```

El backend queda disponible en `http://localhost:8080` (Swagger UI en `http://localhost:8080/swagger-ui.html`).

### 3. Frontend (Next.js)

```bash
cd frontend
npm install
npm run dev
```

El frontend queda disponible en `http://localhost:3000`.

### Credenciales de prueba (`data.sql`)

- **Consultor:** usuario `ADMIN` (o `JUAN`), contraseña `admin123` (`app.ldap.enabled=false`).
- **Candidato:** cualquier candidato asociado a una evaluación generada desde **CU025**, o los cuestionarios precargados en `data.sql`.
- **Puesto de prueba para CU028:** código `P001` (`ANALISTA DE DATOS`, empresa `GRINGOTTS`).

## Casos de uso implementados (Back + Front)

- **CU001** — Autenticar usuario (consultor y candidato con JWT).
- **CU020** — Gestionar preguntas (búsqueda con filtros combinados).
- **CU021** — Dar de alta pregunta (con selección en cascada de competencia/factor y ponderaciones que suman 10).
- **CU022** — Modificar pregunta.
- **CU023** — Dar de baja pregunta (baja lógica con auditoría).
- **CU025** — Evaluar a candidatos (selección de candidatos, validación, selección de puesto, generación de claves y exportación a Excel).
- **CU026** — Completar cuestionario (inicio, recuperación y guardado por bloques, control de accesos y tiempo máximo).
- **CU028** — Emitir orden de mérito (búsqueda de puesto, selección de evaluaciones, ordenamiento por puntaje y ponderación mínima, e impresión del reporte).
