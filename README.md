# TP Diseño de Sistemas 2026

Sistema de gestión de evaluaciones de candidatos para puestos de trabajo. El repositorio contiene el proyecto inicial del backend y carpetas reservadas para el frontend, la base de datos y la documentación.

## Estado actual

- **Backend:** generado con Spring Initializr. Todavía no hay funcionalidades del TP implementadas.
- **Frontend:** carpeta creada; proyecto Next.js pendiente.
- **Base de datos:** Se creo un docker que contiene la base de datos.
- **Documentación:** diagramas y devoluciones del grupo 30 disponibles en [docs/README.md](docs/README.md).

## Stack

| Componente | Versión o estado |
| --- | --- |
| Java del proyecto | 21 (`pom.xml`) |
| Spring Boot | 4.1.3 |
| Maven Wrapper | Maven 3.10.1 (`backend/.mvn/wrapper/maven-wrapper.properties`) |
| Persistencia y seguridad | Spring Data JPA, Spring Security y PostgreSQL Driver |
| API y validación | Spring Web MVC y Validation |
| Lombok | Gestionado por Spring Boot |
| Frontend | Next.js, React y TypeScript: pendiente de crear |
| Base de datos | PostgreSQL: pendiente de configurar |

El Java instalado en el equipo puede ser más reciente que Java 21; el proyecto está configurado para compilar con Java 21.

## Estructura actual

```text
TP-Disenio-2026/
├── backend/
│   ├── .mvn/wrapper/
│   ├── src/
│   │   ├── main/java/com/capitalhumano/evaluaciones/
│   │   │   └── EvaluacionesApplication.java
│   │   ├── main/resources/application.properties
│   │   └── test/java/com/capitalhumano/evaluaciones/
│   │       └── EvaluacionesApplicationTests.java
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml
├── frontend/
│   └── src/                 # Vacía, reservada para Next.js
├── database/
│   └── scripts/             # Vacía
├── docs/
│   ├── casos-de-uso/
│   ├── der/                 # DER en PDF y PlantUML
│   ├── diagramas/
│   │   ├── clases/          # Diagrama de clases en PDF y PlantUML
│   │   └── secuencia/       # CU020, CU021 y CU025
│   ├── entregas/
│   │   ├── etapa-4/         # Devolución del docente
│   │   └── etapa-5/         # Devolución del docente
│   └── README.md           # Índice y observaciones pendientes
├── .gitignore
└── README.md
```

Las carpetas que permanecen vacías contienen `.gitkeep` para que Git las conserve. No se han creado todavía `Dockerfile`, `docker-compose.yml` ni `frontend/package.json`.

## Backend

Configuración de Spring Initializr:

- Maven, Java, Spring Boot 4.1.3, empaquetado Jar y configuración Properties.
- Grupo: `com.capitalhumano`; artefacto: `evaluaciones`; paquete: `com.capitalhumano.evaluaciones`.
- Dependencias: Spring Web, Spring Data JPA, Spring Security, Validation, Lombok y PostgreSQL Driver. El generador añadió también dependencias de prueba.

Para ejecutar desde Windows, una vez configurada la conexión con PostgreSQL:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
cd backend
./mvnw spring-boot:run
```

Por ahora `application.properties` solo define `spring.application.name=evaluaciones`. Hay que configurar la conexión a la base de datos antes de esperar que arranque la aplicación con Spring Data JPA. Spring Security también protege las rutas por defecto hasta que se defina la autenticación del TP.

## Convenciones

- Java: nombres de clases en PascalCase, variables y métodos en camelCase; archivos UTF-8.
- Base de datos: relacional, según el enunciado del TP.
- El código implementado debe respetar los diagramas y las especificaciones aprobadas.

## Alcance funcional del TP

Autenticación; importación de candidatos; exportación de resultados; gestión de puestos, competencias, factores, preguntas y opciones; evaluación de candidatos; resolución de cuestionarios; reporte comparativo y orden de mérito.

### Casos de uso a implementar

- **CU025** — Evaluar a candidatos.
- **CU020** — Gestionar preguntas.
- **CU023** — Dar de alta pregunta.
- **CU026** — Completar cuestionario.
- **CU028** — Emitir orden de mérito.

Según la última comunicación de la cátedra, la entrega final unificada es el **27/11/2026** y los coloquios de cierre posteriores son obligatorios. Durante la implementación, cada grupo debe mostrar sus avances de código en los espacios de consulta.
