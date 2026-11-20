# Documentación del TP — Grupo 30

Los diagramas conservan sus archivos originales. Los PDF permiten consultarlos y los archivos `.puml` son la fuente editable en PlantUML.

| Material | Archivo |
| --- | --- |
| DER corregido | [PDF](der/DER-Grupo30%20(corregido).pdf) · [PlantUML](der/DER-Grupo30%20(corregido).puml) |
| Diagrama de clases corregido | [PDF](diagramas/clases/diagramaClases-Grupo30%20(corregido).pdf) · [PlantUML](diagramas/clases/diagramaClases-Grupo30%20(corregido).puml) |
| Secuencia CU020 | [Imagen](diagramas/secuencia/CU020.png) |
| Secuencia CU021 | [Imagen](diagramas/secuencia/CU021.png) |
| Secuencia CU025 | [Imagen](diagramas/secuencia/CU025.png) |
| Devolución de etapa 4 | [Correcciones](entregas/etapa-4/correcciones_grupo30_etapa4.md) |
| Devolución de etapa 5 | [Correcciones](entregas/etapa-5/correcciones_etapa5_grupo30.md) |

## Al implementar

Estas son las observaciones de la cátedra, no cambios ya realizados en los diagramas:

- **CU021:** definir cómo se crean y persisten las opciones de respuesta al crear una pregunta, y si corresponde hacerlo en una transacción.
- **CU025:** explicitar cómo se obtienen las competencias del puesto según la carga del ORM; mostrar la creación y asociación de la evaluación con los cuestionarios y su persistencia, posiblemente transaccional.
- La devolución de etapa 5 pide incorporar estas correcciones para la **etapa 7**.
- La devolución de etapa 4 también pide revisar el tipo de usuario, las respuestas del candidato en el DER, las claves primarias y la referencia a Usuario en el registro de auditoría. Consultar el texto original antes de dar por resuelto cada punto.

`casos-de-uso/` queda reservado para incorporar las especificaciones de casos de uso cuando estén disponibles.
