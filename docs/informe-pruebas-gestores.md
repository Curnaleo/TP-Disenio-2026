# Informe de pruebas de gestores

Se revisaron los seis gestores del backend a partir del commit `2a232cc`. Las expectativas se basaron en el [enunciado del TP](casos-de-uso/Enunciado-del-TP-2026-de-Diseo-de-Sistemas-v20263---Gestin-de-Evaluaciones-de-Pe.md). El material de la Unidad 6.1 de la cátedra se usó como referencia para elegir clases válidas e inválidas, probar límites y comprobar que un rechazo no produzca la operación solicitada.

La suite completa terminó con **74 pruebas aprobadas, sin fallos, errores ni omisiones**. Se añadieron 39 ejecuciones, incluidas las instancias parametrizadas y tres pruebas de integración con PostgreSQL.

## Cobertura

| Caso de uso | Pruebas de gestor | Resultado esperado |
| --- | --- | --- |
| CU001 · Autenticación | `GestorAutenticacionImplTest` | Las credenciales válidas identifican al usuario. Se aceptan 15 caracteres de usuario y 20 de contraseña y se rechaza superar cualquiera de esos límites. También se prueban credenciales inválidas, usuario inactivo y cierre de sesión con LDAP simulado. |
| CU008 y CU028 · Búsqueda de puestos | `GestorPuestosTest` | Se exige algún filtro. Con coincidencias se devuelven los datos y contadores del puesto. Sin coincidencias se devuelve una lista vacía. |
| CU020–023 · Preguntas | `GestorPreguntasTest`, `GestionPreguntasIntegracionTest` | Los filtros parciales recuperan preguntas. El alta vincula un peso a cada ítem y admite 0 y 10 como extremos si la suma es 10. Se rechazan sumas de 9,99 y 10,01, pesos negativos, ítems ajenos, datos omitidos, duplicados, una escala inactiva y factores de otra competencia. La edición conserva el factor y actualiza los pesos. La baja retira la pregunta de la búsqueda y registra al consultor, la fecha y la hora. |
| CU024 y CU029–031 · Opciones de respuesta | `GestorOpcionesRespuestaTest`, `GestionPreguntasIntegracionTest` | La búsqueda sin nombre lista escalas con ítems ordenados. Se rechazan nombres o descripciones repetidos, órdenes repetidos o discontinuos y escalas de un solo ítem. La edición permite reordenar ítems existentes y rechaza agregarlos. La baja se bloquea con una pregunta activa asociada y se audita cuando se permite. |
| CU025 · Evaluar candidatos | `GestorEvaluacionesTest` | La búsqueda devuelve los datos identificatorios o una lista vacía. Entre los candidatos seleccionados, se informa quién ya tiene un cuestionario vigente. |
| CU028 · Orden de mérito | `GestorReportesTest` | La consulta sin evaluaciones devuelve una lista vacía. El reporte por selección incluye los cuestionarios solicitados, evita repetir el mismo cuestionario y acepta el mínimo exacto. Se ordenan los aprobados por puntaje y se incluyen los demás agrupados por estado. Los cuestionarios incompletos carecen de puntaje. |

Las pruebas de integración usan Spring y PostgreSQL. Se limpia el contexto de persistencia después de escribir para volver a leer los datos desde la base. Las transacciones de los casos se revierten al finalizar cada prueba.

## Defectos y funcionalidades pendientes

| CU | Hallazgo | Evidencia |
| --- | --- | --- |
| CU020–023 | El frontend de preguntas usa datos en memoria. La búsqueda y el alta no llaman al backend, el alta descarta las ponderaciones y las pantallas de edición y baja no están disponibles. Esto impide completar la gestión de preguntas desde la interfaz. | [Servicio frontend](../frontend/src/services/preguntaService.ts#L67) y [pantalla de preguntas](../frontend/src/app/consultores/preguntas/page.tsx). |
| CU020 | La API acepta buscar sin ningún criterio y devuelve preguntas activas. El CU pide ingresar al menos un criterio para buscar. La pantalla sí controla esa condición. | Solicitar `GET /api/preguntas` sin parámetros. El [controlador](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/controller/PreguntaController.java#L30) envía la consulta al [repositorio](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/repository/PreguntaRepository.java#L24), que omite los filtros vacíos. |
| CU029 | Volver a dar de alta el nombre de una escala dada de baja elude la validación de negocio y llega a la restricción de unicidad de PostgreSQL. El CU pide un aviso de nombre duplicado. El manejador general devolvería HTTP 500. | Dar de alta una escala sin preguntas, eliminarla y repetir el alta con el mismo nombre. La [búsqueda del repositorio](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/repository/OpcionRespuestaRepository.java#L31) solo ve activas. [La columna es única](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/entity/OpcionRespuesta.java#L18) y el [manejador general](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/exception/GlobalExceptionHandler.java) responde 500 para excepciones sin tratamiento específico. |
| CU009–011 | El alta, la modificación, la baja y la validación de uso de puestos están pendientes en el gestor. | Los métodos arrojan `UnsupportedOperationException`. [GestorPuestos](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorPuestos.java#L41). |
| CU024 y CU029–031 | Hay endpoints para escalas, pero falta la pantalla de gestión en el frontend. | [Controlador de escalas](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/controller/OpcionRespuestaController.java) y [menú](../frontend/src/app/page.tsx). |
| CU025 | El gestor permite buscar candidatos y detectar cuestionarios vigentes. Faltan crear la evaluación y sus cuestionarios, generar las claves y emitir el listado de candidatos con sus claves. | [GestorEvaluaciones](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorEvaluaciones.java#L40). |
| CU026 | Falta el flujo de completar el cuestionario, que incluye iniciar o reanudar por bloques, guardar respuestas, controlar los vencimientos y calcular resultados al finalizar. | El [menú](../frontend/src/app/page.tsx) lista la opción sin ruta y el backend carece de un gestor para este flujo. |
| CU027 | La generación del reporte comparativo está pendiente. | [GestorReportes](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorReportes.java#L95). |
| CU028 | El backend genera los datos del orden de mérito. Faltan la pantalla para emitir el listado, el aviso de que el puesto carece de evaluaciones y la impresión. | [Controlador de reportes](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/controller/ReporteController.java) y [menú](../frontend/src/app/page.tsx). |
| CU033 | La generación de preguntas con IA queda pendiente en el gestor. | [GestorPreguntas](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorPreguntas.java#L226). |

Los hallazgos de interfaz y de contrato HTTP surgen de inspeccionar el código. La ejecución automatizada comprueba el comportamiento de los gestores y la persistencia indicada en la cobertura. La configuración y conectividad de un LDAP real quedan sin verificar.

## Entorno y ejecución

Se corrigieron dos bloqueos de compilación para habilitar las pruebas. Faltaba el punto y coma en la firma de `buscarActivasPorFactor` y había dos métodos `validarCandidatosActivos` con la misma firma. Se conservó la primera implementación.

El siguiente comando ejecuta las pruebas unitarias de gestores desde `backend`.

```powershell
.\mvnw.cmd '-Dtest=Gestor*Test' test
```

Para ejecutar la suite completa, configurar `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y, si corresponde, `SPRING_DATASOURCE_PASSWORD` para apuntar a una base PostgreSQL descartable. El perfil actual inicializa el esquema con `ddl-auto=create`.

```powershell
.\mvnw.cmd test
```

La ejecución local usó Java 25 con compilación para Java 21, Maven Wrapper y PostgreSQL 16 en una instancia aislada. El detalle por clase queda en `backend/target/surefire-reports`.
