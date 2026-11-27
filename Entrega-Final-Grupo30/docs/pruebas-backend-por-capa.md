# Pruebas del backend por capa

La suite cubre autenticación, preguntas, evaluación de candidatos, cuestionarios y orden de mérito. También incluye los gestores de puestos y escalas y sus repositorios. Los casos se diseñaron a partir de los flujos normal y alternativos del enunciado. Para cada regla con un umbral se eligieron ejemplos por debajo, en el límite y por encima cuando aplicaba. Los rechazos comprueban el mensaje, el estado o la ausencia de escrituras según el efecto esperado. [Enunciado del TP](casos-de-uso/Enunciado-del-TP-2026-de-Diseo-de-Sistemas-v20263---Gestin-de-Evaluaciones-de-Pe.md)

La suite pasó de 89 a **126 ejecuciones aprobadas con PostgreSQL**, incluidas las variantes parametrizadas.

## HTTP y autenticación

| Clases de prueba | Comportamientos verificados |
| --- | --- |
| `AuthControllerTest`, `JwtTokenProviderTest` | Inicio de sesión de consultor y candidato, validación de datos, errores de autenticación, generación y validación de JWT. |
| `CuestionarioControllerTest` | Los cuestionarios incompletos y sin contestar devuelven HTTP 409 con el motivo correspondiente. Un reingreso sin accesos devuelve `ACCESOS_SUPERADOS`. Consultar o enviar un bloque adelantado, o enviar JSON mal formado, devuelve HTTP 400. Repetir el envío de un bloque ya contestado devuelve el número del bloque pendiente. Las solicitudes rechazadas y el reintento no vuelven a escribir respuestas. |

Las pruebas HTTP de cuestionarios usan MockMvc con colaboradores simulados. Verifican el mapeo de rutas, el código de respuesta y el JSON del controlador. El token se representa con una sesión de candidato en la solicitud de prueba.

## Servicios y reglas de negocio

| Caso de uso y clase | Particiones, límites y efectos verificados |
| --- | --- |
| **CU001** · `GestorAutenticacionImplTest` | Consultor válido, credenciales inválidas, usuario inactivo y límites de 15 caracteres de usuario y 20 de contraseña. Autenticación de candidato con documento y clave y cierre de sesión. LDAP se simula. |
| **CU008** · `GestorPuestosTest` | Búsqueda con criterios, ausencia de criterios y cero coincidencias. Con resultados, se comprueban los totales de candidatos y cuestionarios completos. |
| **CU020–023** · `GestorPreguntasTest` | Búsqueda sin filtros, búsqueda sin resultados y mapeo de la grilla. Máximos de 150, 500 y 255 caracteres para nombre, texto y descripción. Se rechaza cada campo con un carácter extra. Ponderaciones con extremos 0 y 10, sumas inferiores y superiores a 10, pesos negativos, ítems ajenos y duplicados dentro del factor. Cambio de factor rechazado, edición de pesos y baja auditada. |
| **CU024, CU029–031** · `GestorOpcionesRespuestaTest` | Búsqueda de escalas y orden de ítems. Máximos válidos de 100, 255 y 100 caracteres para nombre, descripción e ítem, respectivamente. Omisiones simultáneas, mínimo de dos ítems, nombres y descripciones repetidos, órdenes con saltos, nombre de solo lectura y protección de escalas en uso. |
| **CU025** · `GestorEvaluacionesTest` | Puesto sin competencias y factor con 0, 1, 2 o 3 preguntas activas. El umbral para evaluar es 2. Un candidato con cuestionario vigente bloquea el lote antes de guardar. Confirmación de las claves mostradas, selección de dos preguntas distintas por factor y generación del Excel para el consultor que emitió la evaluación. |
| **CU026** · `GestorCuestionarioTest` | Dos factores con puntajes calculados de 2,5 y 8,5, repartidos entre bloques. La competencia obtiene 5,5. Un factor sin preguntas asignadas queda fuera del promedio. |
| **CU028** · `GestorReportesTest` | Evaluaciones seleccionadas y opción todas, orden descendente de aprobados, agrupación de los demás por estado y ausencia de puntaje en cuestionarios incompletos. Para un mínimo de 7, se comprueban 6,99, 7,00 y 7,01. |

## Repositorios y PostgreSQL

| Clases de prueba | Comportamientos verificados |
| --- | --- |
| `CompetenciaRepositoryTest`, `FactorRepositoryTest`, `PreguntaRepositoryTest`, `OpcionRespuestaRepositoryTest` | Consultas, persistencia y filtros de los catálogos. |
| `CandidatoRepositoryTest` | La búsqueda combina filtros parciales de apellido y nombre, ignora mayúsculas y espacios exteriores, ordena el resultado, aplica el número exacto y excluye los candidatos dados de baja. |
| `GestionPreguntasIntegracionTest` | Alta y edición de ponderaciones, recuperación con filtros parciales, baja lógica y auditoría con usuario y fecha. La escala vinculada a una pregunta activa queda protegida. |
| `GestorEvaluacionesIntegracionTest` | La evaluación persiste un cuestionario activo. Un cuestionario en proceso impide emitir otro para ese candidato. Los estados completo, incompleto y sin contestar permiten una nueva evaluación. |
| `GestorCuestionarioIntegracionTest` | Inicio, reanudación, guardado de bloques, faltantes, opción de otra pregunta y dos respuestas para la misma pregunta. El límite de tiempo incluye el instante exacto y vence un segundo después. Se verifica el último acceso disponible y el rechazo al agotarlo. Un cuestionario que vence antes de iniciar queda sin contestar, con fecha de cierre y cero accesos. El cuestionario finalizado aparece con puntaje en el reporte de su evaluación. |

Las clases de integración ejecutan transacciones de prueba con rollback. Usan candidatos y cuestionarios propios donde el estado debe controlarse por caso. Conservan el catálogo de puestos del esquema de prueba.

## Hallazgos al cotejar el enunciado

- **CU021 y CU022.** El enunciado exige que las ponderaciones de cada pregunta sumen 10. Se corrigieron las cinco preguntas precargadas con 10 puntos para Verdadero y 0 para Falso. La suma se verificó en PostgreSQL. [Regla del enunciado](casos-de-uso/Enunciado-del-TP-2026-de-Diseo-de-Sistemas-v20263---Gestin-de-Evaluaciones-de-Pe.md#L584) · [Datos iniciales](../backend/src/main/resources/data.sql#L230)
- **CU029.** El gestor comprueba el nombre antes de guardar mediante una consulta que incluye escalas dadas de baja. Una prueba con PostgreSQL verifica que intentar reutilizar el nombre devuelve HTTP 400 con el aviso de duplicado. [Gestor](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorOpcionesRespuesta.java#L59) · [Consulta](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/repository/OpcionRespuestaRepository.java#L52) · [Prueba](../backend/src/test/java/com/grupo30/capitalhumano/evaluaciones/service/GestionPreguntasIntegracionTest.java#L94)
- **CU033.** La generación con IA sigue pendiente como extensión del alta de preguntas. También siguen pendientes las altas, ediciones y bajas de puestos y la emisión del reporte comparativo. Las pruebas de los métodos que lanzan `UnsupportedOperationException` registran ese estado de implementación. [Preguntas](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorPreguntas.java#L232) · [Puestos](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorPuestos.java#L41) · [Reportes](../backend/src/main/java/com/grupo30/capitalhumano/evaluaciones/service/GestorReportes.java#L95)

## Ejecución

Desde `backend`, apuntar `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` y, si corresponde, `SPRING_DATASOURCE_PASSWORD` a una instancia de prueba. El esquema se recrea al inicializar Spring con la configuración actual.

```powershell
.\mvnw.cmd test
```

La suite estándar incluye las pruebas unitarias, HTTP, de repositorios y de integración. Maven guarda los resultados por clase en `backend/target/surefire-reports`.
