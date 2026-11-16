# Configuración del Entorno

## Stack Tecnológico

### Backend

| Componente | Versión |
|------------|----------|
| Java | 21 LTS |
| Spring Boot | 3.5.x |
| Maven | 3.9.x |
| Spring Data JPA | Incluido en Spring Boot |
| Spring Security | Incluido en Spring Boot |
| Hibernate | Incluido en Spring Boot |
| Lombok | Última estable |

### Frontend

| Componente | Versión |
|------------|----------|
| Node.js | 22 LTS |
| npm | 10.x |
| Next.js | 15.x |
| React | 19.x |
| TypeScript | 5.x |
| Tailwind CSS | 4.x |

### Base de Datos

| Componente | Versión |
|------------|----------|
| PostgreSQL | 17 |
| pgAdmin | Última estable |

### Herramientas

| Componente | Versión |
|------------|----------|
| Git | 2.49+ |
| Docker | 28+ |
| Docker Compose | Última estable |

---

# Estructura del Proyecto

```text
tp-diseno/
│
├── backend/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── Dockerfile
│
├── database/
│   ├── scripts/
│   └── docker-compose.yml
│
├── docs/
│   ├── casos-de-uso/
│   ├── diagramas/
│   ├── der/
│   └── entregas/
│
└── README.md
```

---

# Convenciones Generales

## Java

- JDK: 21
- UTF-8 obligatorio
- Tabulación: 4 espacios
- Lenguaje: Inglés
- Nombres de clases en PascalCase
- Variables y métodos en camelCase

Ejemplo:

```java
public class CuestionarioService {

    public void generarCuestionario() {

    }

}
```

---

## TypeScript

- Strict Mode habilitado
- ESLint obligatorio
- Prettier obligatorio

```json
{
  "strict": true
}
```

---

# Git

## Rama principal

```text
main
```

## Desarrollo

```text
develop
```

## Features

```text
feature/gestion-preguntas
feature/orden-merito
feature/autenticacion
```

## Fixes

```text
fix/login
fix/exportacion-csv
```

---

# Variables de Entorno

## Backend

Archivo:

```text
backend/.env
```

Ejemplo:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tpdisenio

DB_USERNAME=postgres
DB_PASSWORD=postgres

SPRING_PROFILES_ACTIVE=dev
```

---

## Frontend

Archivo:

```text
frontend/.env.local
```

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api
```

---

# Configuración de PostgreSQL

## Base de Datos

```text
tpdisenio
```

## Usuario

```text
postgres
```

## Password

```text
postgres
```

---

# application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tpdisenio
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.jpa.properties.hibernate.format_sql=true
```

---

# Docker

## Base de Datos

```yaml
services:
  postgres:

    image: postgres:17

    container_name: tp-postgres

    restart: unless-stopped

    environment:
      POSTGRES_DB: tpdisenio
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres

    ports:
      - "5432:5432"

    volumes:
      - postgres-data:/var/lib/postgresql/data

volumes:
  postgres-data:
```

---

# Ejecución del Proyecto

## Backend

```bash
cd backend
mvn spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

---

## Frontend

```bash
cd frontend

npm install

npm run dev
```

Disponible en:

```text
http://localhost:3000
```

---

# Dependencias Backend

## Spring Initializr

Seleccionar:

- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- Lombok
- PostgreSQL Driver

---

# Dependencias Frontend

Crear proyecto:

```bash
npx create-next-app@latest frontend \
  --typescript \
  --tailwind \
  --eslint \
  --src-dir \
  --app
```

---

# Compatibilidad de Sistemas Operativos

El proyecto debe funcionar en:

- Windows 11
- Ubuntu 24.04+
- Arch Linux

## Reglas

NO hacer:

```java
"C:\\Users\\Juan\\archivo.txt"
```

Hacer:

```java
Paths.get("data", "archivo.txt")
```

---

# Entregables del TP

El sistema debe contemplar las funcionalidades definidas en el enunciado:

- Autenticación
- Importación de candidatos
- Exportación de resultados
- Gestión de puestos
- Gestión de competencias
- Gestión de factores
- Gestión de preguntas
- Gestión de opciones de respuesta
- Evaluación de candidatos
- Completar cuestionarios
- Reporte comparativo
- Orden de mérito
