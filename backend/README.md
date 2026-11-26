# TP Diseño 2026 - Backend

Backend desarrollado con Spring Boot para el TP de Diseño de Sistemas 2026 - Gestión de Evaluaciones de Perfiles. 【1-198711】

---

## Requisitos

- Java 21
- Maven
- Docker
- Docker Compose

---

## Levantar la base de datos

La infraestructura de desarrollo se encuentra en la carpeta:

```bash
database/
```

Desde dicha carpeta ejecutar:

```bash
docker compose up
```

Esto levantará:

- PostgreSQL
- PGAdmin

---

## Acceso a PGAdmin

Una vez iniciado Docker, se puede acceder a PGAdmin desde:

[http://localhost:5050/browser/](http://localhost:5050/browser/)

---

## Levantar el backend

Desde la raíz del proyecto ejecutar:

```bash
mvn spring-boot:run
```

Alternativamente:

```bash
./mvnw spring-boot:run
```

Si el inicio fue exitoso, la aplicación quedará disponible en:

```text
http://localhost:8080
```

---

## Swagger

La documentación OpenAPI se encuentra disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

También puede consultarse la especificación OpenAPI en formato JSON:

```text
http://localhost:8080/v3/api-docs
```

---

## Autenticación

La API utiliza autenticación JWT.

Swagger puede utilizarse para probar los endpoints de autenticación y posteriormente autorizar el resto de las operaciones protegidas.

### Login de Consultor

```http
POST /api/auth/consultor
```

### Login de Candidato

```http
POST /api/auth/candidato
```

---

## Uso del token JWT en Swagger

1. Ejecutar un endpoint de login.
2. Copiar el token JWT obtenido.
3. Presionar el botón **Authorize** en Swagger.
4. Ingresar:

```text
Bearer <token>
```

5. Confirmar la autorización.

A partir de ese momento Swagger enviará automáticamente el token en todos los endpoints protegidos.

---

## Funcionalidades principales

El sistema contempla:

- Autenticación de usuarios
- Gestión de Competencias
- Gestión de Factores
- Gestión de Preguntas
- Gestión de Opciones de Respuesta
- Gestión de Puestos
- Evaluación de Candidatos
- Completar Cuestionarios
- Reporte Comparativo
- Orden de Mérito
- Generación de preguntas mediante IA

Estas funcionalidades corresponden a los casos de uso definidos para el TP. 【1-198711】【2-d479f3】【3-86e1ca】

---

## Verificación rápida

### 1. Levantar infraestructura

```bash
cd database
docker compose up
```

### 2. Verificar PGAdmin

Abrir:

```text
http://localhost:5050/browser/
```

### 3. Levantar backend

```bash
mvn spring-boot:run
```

### 4. Abrir Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

### 5. Probar autenticación

Ejecutar:

```http
POST /api/auth/consultor
```

o

```http
POST /api/auth/candidato
```

### 6. Autorizar Swagger

Utilizar el token JWT obtenido mediante el botón **Authorize**.

---

## Tecnologías

- Spring Boot
- Spring Security
- JWT
- PostgreSQL
- PGAdmin
- Spring Data JPA
- Swagger / OpenAPI
- Maven
- Docker Compose
