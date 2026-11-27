# Cambios para actualizar el diagrama de clases

## Gestores

- [ ] **GestorEvaluaciones.** Agregar `validarPuestoEvaluable`, `prepararEvaluacion(): VistaPrevia` y `confirmarEvaluacion(): int`. Incorporar `VistaPrevia` y `CandidatoConClave` y dibujar la dependencia hacia `GestorCuestionario`.
- [ ] **GestorCuestionario.** Cambiar el retorno de `iniciarCuestionario` de `void` a `int` y agregar `prepararCuestionarios(Puesto, List<Cuestionario>)`.
- [ ] **ExportadorCandidatosExcel.** Si se mantiene la implementación actual, retirar esta clase del diagrama. `GestorEvaluaciones` ya realiza la exportación.
- [ ] **GestorPreguntas y GestorOpcionesRespuesta.** Mostrar que ambos delegan en `AuditoriaBajas`.
- [ ] **GestorAutenticacion.** Representar `AutenticacionService` y su implementación `GestorAutenticacionImpl`.

## DAO

- [ ] **ICompetenciaDAO e IFactorDAO.** Reemplazar la búsqueda por ID por `findByCodigo(String)` y usar el código para la baja y, en competencia, la validación de uso. Agregar `buscarPorCompetencia` en `IFactorDAO`.
- [ ] **IPreguntaDAO e IBloquePreguntasDAO.** Usar el código en `eliminarPorFactor` y agregar `buscarActivasPorFactor` en el primero. Agregar `actualizar` en el segundo.
- [ ] **Consultas DAO que pueden no encontrar una entidad.** Reflejar los retornos `Optional` vigentes en candidatos, competencias, factores, preguntas, opciones de respuesta, bloques, cuestionarios, evaluaciones, configuración y usuarios.
- [ ] **DAO nuevos.** Agregar `IOpcionPreguntaDAO`, `IResultadoFactorDAO` e `IResultadoCompetenciaDAO`.

## Entidades y DTO

- [ ] **Evaluacion.** Agregar `nombreEvaluacion`, `descripcion` y `activo`.
- [ ] **Puesto, CaracteristicaPuesto, ItemOpcionRespuesta, ResultadoFactor y ResultadoCompetencia.** Mostrar los ID si el diagrama incluye persistencia.
- [ ] **PreguntaDTO y OpcionPreguntaDTO.** Agregar `codigoCompetencia`, `codigoFactor`, `idOpcionRespuesta` y `nombreOpcionRespuesta` a `PreguntaDTO`. Quitar la asociación de `OpcionPreguntaDTO` con `ItemOpcionRespuestaDTO`.
- [ ] **OpcionRespuestaDTO e ItemOpcionRespuestaDTO.** Renombrar `idOpcionRespuesta` a `id` en el primero y agregar `ordenOriginal` en el segundo.
- [ ] **CompetenciaDTO y FactorDTO.** Revisar si conservar `id` en ambos e `idCompetencia` en `FactorDTO`.
- [ ] **DTO de reportes.** Agregar `puestoId`, `cantidadCandidatos` y `evaluacionesCompletas` a `PuestoDTO`, `nombreEvaluacion` a `EvaluacionDTO` y `cumplePonderacionMinima` a `CandidatoOrdenMeritoDTO`.
