# Correcciones GRUPO 30 - Etapa 4 (Diagrama Entidad-Relación)

## Diagrama de clases
- Revisen nuevamente el punto marcado sobre el tipoUsuario. Que tipos de usuarios tiene el sistema?
## DER

### 1 - Modelado de la relacion entre pregunta cuestionario y la respuetsta del candidato
En el modelo tal y como esta, no es posible recuperar cuales fueron ls respuestas del candidato a cada pregunta determinada. 
En general les recomiendo evaluar como estan modeladas en su DER las preguntas y respuestas. Quizas sería una buena idea que PreguntaCuestionario referencie directamente la pregunta y la respuesta que el usuario eligio a esa pregunta, tomando esa idea como base para reducir la complejidad del modelado.

En general revisen el modelado de pregunta, opcion respuesta, item opcion respuesta, opcion pregunta y pregunta cuestionario.



### 2 - Claves primarias no declaradas en cuatro entidades
Puesto, Competencia, Factor y ConfiguracionSistema no tienen ningún atributo marcado como clave primaria. Da la impresión de que la intención era usar codigo (y clave en el caso de ConfiguracionSistema) como clave natural



### 3 - Falta la referencia al usuario en Registro de auditoria.



## Veredicto:
Buen trabajo, sigan así!Incorporar las correcciones para la proxima etapa de entrega. 