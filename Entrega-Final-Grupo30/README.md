# Entrega final · Grupo 30

Trabajo práctico de Diseño de Sistemas 2026. Sistema de gestión de evaluaciones de candidatos. El código corresponde a develop en el commit e1ac7de.

| Material | Ubicación |
| --- | --- |
| Documentación y diagramas | [Índice](docs/README.md) |
| Programa compilado | programa/ |
| Código fuente y pruebas | backend/, frontend/ y database/ |
| Compilación desde el código | [Guía de desarrollo](README-desarrollo.md) |

## Ejecutar en Windows

1. Extraé el ZIP completo y abrí Docker Desktop. Esperá a que Docker esté listo.
2. Hacé doble clic en [Iniciar.cmd](Iniciar.cmd). El lanzador inicia PostgreSQL 17, el backend y el frontend, y abre [localhost:3000](http://localhost:3000/). La primera vez Docker puede tener que descargar la imagen de PostgreSQL.
3. Para apagar el sistema, presioná **Enter** en la ventana del lanzador.

Se necesita Java 21 o superior y Node.js 24 disponibles en la terminal. El lanzador usa una base de demostración propia, publicada localmente en el puerto 55430. El esquema de esa base se recrea cada vez que se inicia el backend.

## Ejecución manual

La versión compilada se preparó para Windows x64. Se necesita Java 21 o superior, Node.js 24 y PostgreSQL. En otros sistemas, seguir la [guía de desarrollo](README-desarrollo.md) para compilar y ejecutar desde el código fuente. Desde la carpeta extraída, abrir tres terminales.

1. Iniciar PostgreSQL 17 con Docker.

   ```sh
   docker compose -f database/docker-compose.yml -p grupo30-entrega up -d postgres
   ```

   Si se usa PostgreSQL local, crear una base de prueba vacía llamada `capital_humano`. El backend se conecta por defecto a `127.0.0.1:5433` con usuario y contraseña `postgres`. Se puede ajustar la conexión con `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y `SPRING_DATASOURCE_PASSWORD`. El esquema se recrea y se cargan datos iniciales al arrancar. Usar una base dedicada a la entrega.

2. Iniciar el backend.

   ```sh
   java -jar programa/backend.jar --server.address=127.0.0.1
   ```

3. Iniciar el frontend.

   **PowerShell**

   ```powershell
   $env:HOSTNAME = "127.0.0.1"
   $env:PORT = "3000"
   node programa/frontend/server.js
   ```

4. Abrir [http://localhost:3000](http://localhost:3000/). El frontend compilado usa el backend en `http://localhost:8080/api`.

## Recorrido de demostración

1. Ingresar como consultor con `ADMIN` y `admin123`.
2. En **Preguntas**, buscar por competencia, factor o nombre. Para crear o modificar una pregunta, los pesos de las opciones deben sumar 10.
3. En **Evaluar candidatos**, buscar el candidato de prueba n.º 1002. Seleccionar **ANALISTA DE DATOS** de **GRINGOTTS**, revisar la clave y finalizar.
4. En **Realizar cuestionario**, usar el tipo y número de documento de la grilla y la clave generada. Completar todos los bloques.
5. En **Orden de mérito**, buscar el puesto `P001` y emitir el listado para la evaluación recién creada o para todas.

[Verificación de la entrega](docs/verificacion-entrega.md)
