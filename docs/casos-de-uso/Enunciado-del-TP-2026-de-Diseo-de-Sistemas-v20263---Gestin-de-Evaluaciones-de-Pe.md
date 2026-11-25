***Universidad Tecnológica Nacional*** ***Facultad Regional Santa Fe*** ***Ingeniería en Sistemas de Información*** ***Diseño de Sistemas de Información*** ***TP - 2026***

# DISEÑO DE SISTEMAS TRABAJO PRÁCTICO 2026

# Tabla de contenidos

# DESCRIPCIÓN DEL PROBLEMA 1 OBJETIVO 3

# ALCANCES 3 PAUTAS PARA EL DESARROLLO DEL TP 3

# CRONOGRAMA 3 CONSIDERACIONES FINALES 5

# ANÁLISIS 6 Diagrama de Casos de Usos 6

# Especificación de Casos de Usos 9

## Descripción del Problema

La consultora “Capit@l Humano” es una consultora de recursos humanos de vanguardia de la República Argentina, focalizada en el mercado de las TIC. “Capit@l Humano” pone a disposición de las grandes corporaciones las mejores prácticas en procesos y provisión de servicios de Recursos Humanos.

La consultora, observando la importante demanda que posee para la selección de recursos humanos especializados en TICs, ha decidido hacer uso de las tecnologías para “agilizar” parte del proceso de evaluación que se realiza sobre los candidatos.

Uno de los test del proceso de selección que la consultora decidió informatizar es la evaluación de determinadas competencias técnicas (dependientes del perfil buscado) y competencias actitudinales (dependientes de la empresa que contrata los servicios de “Capit@l Humano”).

Este tipo de test se conforma con preguntas cerradas con elección de una única respuesta (multiple-choice de una sola opción). Las opciones que posee cada pregunta pueden ser configuradas al momento de definir las mismas. Por ejemplo: en una pregunta las opciones podrían ser: verdadero – falso, en otras preguntas las opciones podrían ser: malo – regular – bueno – muy bueno – excelente.

Las competencias, tanto técnicas como actitudinales, se componen de diferentes factores, donde cada uno de los factores posee las preguntas asociadas al mismo. A continuación se presenta un ejemplo de una competencia técnica con sus factores y preguntas asociadas (no se muestran las opciones de cada pregunta)

| Competencia | Factor | Preguntas |
| --- | --- | --- |
| Gestión de Proyectos | Gestión del alcance | ¿La descomposición de estructura de trabajo (WBS) forma parte de la definición del alcance? ¿Cuándo se define un alcance, debe definirse una línea base de los requerimientos? |
|  | Gestión del tiempo | En el cronograma de un proyecto, ¿qué significa que una actividad sea del tipo “fin a comienzo”? ¿La metodología de “Valor Ganado” permite predecir cuánto dinero se gastará en un proyecto según los avances que el mismo tenga a la fecha? |

***

***TP - 2026***

A continuación se presenta un ejemplo de una competencia actitudinal con dos factores y una pregunta asociada a cada uno de ellos:

| Competencia | Factor | Preguntas |
| --- | --- | --- |
| Lealtad | Credibilidad | Con cuál de las siguientes frases te identificas más en tu vida profesional: a) Soy digno de confianza, porque a través de mis hechos profesionales lo demuestro. b) Cumplo de manera íntegra con lo que se me pide en el trabajo. c) Guardo los intereses de la empresa sobre cualquier situación que pudiera afectarla. d) Soy solidario con mis compañeros de trabajo buscando el bien de la empresa. e) En mis actividades profesionales asumo todos los compromisos que contraigo. |
|  | Compromiso | Para mí el logro de objetivos colectivos es importante porque: a) Gracias a mi desempeño cotidiano mis compañeros de trabajo confían en mí. b) Me gusta comprometerme con los proyectos que me asignan en el trabajo. c) La información que se genera en mi puesto la resguardo para el mejor uso por parte de la empresa. d) Soy partidario de ayudar a mis compañeros de trabajo, con tal de beneficiar los intereses laborales. e) Cuando me asignan proyectos estratégicos para la empresa, me hago cargo de ellos totalmente. |

Ante la solicitud de las empresas de cubrir un determinado puesto, la consultora creará los puestos buscados y a los mismos se les asociará qué puntaje mínimo debe obtener un candidato en cada competencia solicitada para ajustarse al mismo.

Dado que la consultora ya posee un sistema de gestión de candidatos y CV, la nueva aplicación que se desarrollará para automatizar uno de los diferentes tipos de test del proceso de selección importará la lista de candidatos desde el sistema de gestión de Candidatos y CV. A su vez, una vez concluidos los test, existirá una opción para exportar los resultados obtenidos por los candidatos al sistema de “Gestión de candidatos y CV”.

Los consultores asociarán qué candidatos pueden aplicar al puesto requerido por la empresa y les notificarán a los mismos (vía telefónica) que deben completar el cuestionario (vía web o asistiendo a las oficinas de la consultora).

Una vez que un candidato completa el cuestionario, el sistema, a través de un reporte gráfico, deberá permitir mostrar el comparativo entre **lo requerido** para el puesto versus **lo obtenido** por el candidato. Esta información será exclusiva de los consultores. A su vez, existirá otro reporte que mostrará para una función o puesto, los resultados obtenidos por los diferentes candidatos ordenados por orden de mérito.

Al momento de ingresar un candidato a completar el cuestionario, el mismo deberá autenticarse en el sistema y la aplicación le mostrará el cuestionario a completar. Todo candidato puede tener un solo cuestionario activo. Cabe remarcar que un candidato puede dejar parcialmente contestado un cuestionario y retirarse de la aplicación, para volver luego a continuar con la contestación de las preguntas restantes.

***

<u></u>TP - 2026

Las preguntas se mostrarán en pantalla en bloques de una cantidad especificada. Una vez que un candidato completa un bloque no puede regresar a modificar alguna pregunta del mismo, ni tampoco puede regresar a ver qué ha respondido en las preguntas de dicho bloque.

Cabe remarcar que cada cuestionario no tendrá las mismas preguntas, sino que aleatoriamente se tomarán 2 preguntas por factor de cada competencia a evaluar. Si la competencia a evaluar no posee definidos factores con al menos 2 preguntas, dicha competencia no podrá ser evaluada. Las preguntas en cada cuestionario se mostrarán en forma aleatoria, mezclando preguntas de los diferentes factores de las diferentes competencias.

## Objetivo

El objetivo del presente proyecto es la realización de una herramienta o plataforma que permita gestionar el proceso de evaluación de candidatos a diferentes puestos vacantes.

## Alcances

Análisis, diseño, programación, prueba y depuración de un sistema con la siguiente funcionalidad:

- ✔ Autenticación en la aplicación. ✔ Importación de Candidatos ✔ Exportación de resultados. ✔ Gestión de puestos o funciones. Creación con GenAI. ✔ Gestión de competencias. ✔ Gestión de factores. ✔ Gestión de preguntas. Creación con GenAI. ✔ Gestión de opciones de respuestas. ✔ Evaluar a candidatos. ✔ Completar cuestionarios. ✔ Reporte comparativo. ✔ Reporte por orden de mérito.

## Pautas para el desarrollo del TP

1. El Trabajo Práctico es grupal.
2. Consiste en el diseño de un sistema utilizando metodología orientada a objetos, más la programación de algunos CU del sistema.
3. Los grupos estarán formados por **4 (CUATRO)** alumnos. No es necesario que los alumnos cursen la materia en el mismo turno.
4. Cada grupo tiene asignado un docente auxiliar quien se encargará de realizar el seguimiento del grupo y corregirá las entregas parciales y finales del TP. El auxiliar guiará al grupo respecto de la conveniencia de avanzar con el diseño.
5. En la Planificación del TP, se consignan ocho puntos de control: siete parciales y una entrega final. El incumplimiento de cualquiera de los objetivos establecidos en dichos puntos en tiempo y forma, dejará LIBRES a los integrantes del grupo (ver Cronograma).
6. Si el docente auxiliar lo considera necesario puede convocar a un grupo para realizar un coloquio a fin de decidir sobre la aprobación de una etapa.

***

<u></u>TP - 2026

7. Las consultas referentes al enunciado y a la confección del TP propiamente dichas serán dirigidas al docente auxiliar asignado. Cada docente auxiliar dará al menos una consulta semanal en horario de clase.
8. Para las consultas se utilizará el correo oficial de la práctica <u></u>disenio.utn.frsf@gmail.com y/o a través de Teams (previo acuerdo)
9. La programación se realizará en lenguaje que soporte programación OO a elección de cada grupo y sobre base de datos relacional.

   10.El desarrollo del sistema deberá adecuarse al diseño. Caso contrario el TP no estará aprobado.
   **Conformación de grupos** La conformación de grupos se hará mediante correo electrónico, un alumno por grupo

# deberá enviar un correo electrónico a la dirección <u></u>disenio.utn.frsf@gmail.com con la siguiente información:

- ▪ Nombre del grupo. ▪ Por cada integrante: o Correo electrónico. o Número de legajo. o Comisión a la que fue inscripto (tarde o noche)

# A cada grupo conformado se le asignará un docente auxiliar quien se encargará de

# realizar el seguimiento del grupo y corregirá las entregas parciales y finales del TP. El

# auxiliar guiará al grupo respecto de la conveniencia de avanzar con el diseño.

# Una vez confirmado el grupo los alumnos pueden comenzar a realizar entregas en caso

# de ser necesario. La conformación del grupo será confirmada mediante correo

electrónico donde cada grupo recibirá un número de identificación junto con el nombre del docente auxiliar asignado para su seguimiento.

***

***TP - 2026***

## Cronograma

A continuación se consignan las etapas en las que se ha dividido el desarrollo del TP y sus correspondientes fechas de entrega. El TP puede ser (y de hecho se recomienda) entregado y corregido cada vez que el grupo finalice alguna instancia propia de la metodología. De esta forma al hacer la entrega parcial los alumnos pueden estar seguros respecto de la aprobación del mismo. Para obtener la aprobación del TP será necesario cumplir con los siguientes requisitos:

| Etapa | Descripción y Entregables | Fecha Tope de ENTREGA |
| --- | --- | --- |
| 1 | Diseño ✔ Diseño de Entradas ✔ Diseño de Salidas De los siguientes casos de uso: o CU025 - Evaluar a Candidatos o CU020 - Gestionar Preguntas o CU021 - Dar de Alta pregunta o CU023 - Dar de baja pregunta o CU008 - Gestionar Puestos o CU009 - Dar de alta puesto o CU026 - Completar cuestionario o CU027 - Emitir reporte comparativo o CU028 - Emitir orden de mérito | 08/05/2026 |
| 2 | Diseño ✔ Diagrama de clases (1era entrega) | 29/05/2026 |
| 3 | Diseño ✔ Diagrama de clases (final) | 03/07/2026 |
| 4 | Diseño ✔ Diagrama entidad relación | 30/08/2026 |
| 5 | Diseño ✔ Diagrama de Secuencia de los siguientes casos de uso (1er entrega) o CU025 - Evaluar a Candidatos o CU020 - Gestionar Preguntas o CU023 - Dar de alta pregunta | 25/09/2026 |
| 6 | Diseño ✔ Diagrama de despliegue ✔ Diagrama de Secuencia de los siguientes casos de uso (2da entrega) o CU026 - Completar cuestionario o CU028 - Emitir orden de mérito | 16/10/2026 |
| 7 | Implementación de los siguientes casos de uso: o CU020 - Gestionar Preguntas o CU028 - Emitir orden de mérito | 06/11/2026 |
| 8 | Implementación de los CU con diagramas de secuencia (entregados en las etapas 5 y 6) Entrega Final ✔ Documentación del sistema ✔ Programa funcionando | 20/11/2026 |

***

***TP - 2026***

- ✔ Código fuente

  Si fuera necesario aclarar puntos sobre el TP una vez que ha sido entregado para la corrección final de la etapa se podrá convocar a los alumnos a un coloquio.

IMPORTANTE: Los plazos consignados son plazos máximos. Los trabajos podrán presentarse en forma anticipada.

## Consideraciones finales

A lo largo del desarrollo del TP, los alumnos deben tener presente que las etapas aprobadas probablemente deberán ser modificadas con posterioridad. En la información presentada se han obviado algunos aspectos de la problemática planteada con el objeto de simplificar la situación y obtener un nivel de complejidad acorde a un Trabajo Práctico, recordando que el objetivo del TP es que el alumno aplique los conocimientos adquiridos durante las clases y los integre con los contenidos de otras materias de la carrera.

Los casos de uso 002 al 005 no forman parte del presente trabajo práctico, por lo cual no se incluyen en las especificaciones.

***

<u></u>TP - 2026

## Análisis

### Diagrama de Casos de Usos

***

<u></u>TP - 2026

***

<u></u>TP - 2026

***

***TP - 2026***

### Especificación de Casos de Usos

| Nombre : Autenticar usuario | Nro : 001 |
| --- | --- |
| Actores : Usuario No Autenticado Objetivo : Realizar la autenticación del usuario y en base a su perfil realizar la autorización a las funcionalidades que el usuario posee acceso en el sistema. Precondiciones : Ninguna | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ Imposibilidad de trabajar en el sistema. | Éxito: ▪ Aparece la pantalla de trabajo. |
| Flujo Principal 1. El actor desea trabajar en el sistema 2. El sistema presenta la pantalla para autenticar al usuario solicitando que seleccione en primer término el tipo de acceso: ● Ingresar como usuario consultor ● Realizar cuestionario | Flujo Alternativo |
| 3. El actor selecciona “Ingresar como usuario”. | 3.A. El actor selecciona “Realizar cuestionario”. 3.A.1 El usuario ingresa: ● Tipo ● número de documento y ● clave. 3.A.2 El sistema comprueba que exista un candidato para ese tipo y número de documento y un cuestionario activo o en proceso asociado al mismo; y que la clave ingresada corresponda al cuestionario en cuestión. 3.A.2.1 No existe el candidato o un cuestionario activo o en proceso o la clave no se corresponde. 3.A.2.2 El sistema muestra el mensaje de error “Los datos ingresados no son válidos o no existe un cuestionario para el Candidato” 3.A.2.3 El sistema emite una alerta sonora. 3.A.2.4 El actor cierra la pantalla de error. 3.A.2.5 El sistema blanquea los campos. 3.A.2.6 El CU continua en el paso 3.A. |

***

***TP - 2026***

|  | 3.A.3 El sistema presenta la pantalla principal invocando al “ CU026 – Completar Cuestionario ”. 3.A.4 El CU continúa en el paso 7. |
| --- | --- |
| 4. El usuario ingresa: ● nombre de usuario, y ● contraseña. |  |
| 5. El sistema valida que el usuario y contraseña son correctos comprobando dicha información en el LDAP de la organización. | 5.A. El usuario o la contraseña son inválidos. 5.A.1 El sistema muestra el mensaje de error: “El usuario o la contraseña no son válidos” 5.A.2 El sistema emite una alerta sonora. 5.A.3 El actor cierra la pantalla de error. 5 .A.4 El sistema blanquea los campos. 5 .A.5 El CU continua en el paso 4. |
| 6. El sistema presenta la pantalla principal activando todas las opciones del perfil Consultor. |  |
| 7. El CU termina |  |
| Asociaciones de Inclusión : ▪ No posee |  |
| Asociaciones de Extensión : CU Condición ● No posee |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederá un campo. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso del identificador de usuario será SIEMPRE en mayúsculas. ▪ La contraseña y la clave se visualizarán con asteriscos (\\*). ▪ El LDAP posee definido una longitud máxima para el identificador de usuario de 15 caracteres y una contraseña de 20 caracteres máximo. |  |
| Observaciones : ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir. ▪ En cualquier flujo del caso de uso el actor debe poder volver a seleccionar si desea autenticarse como Usuario o para realizar un cuestionario. ▪ Cualquier funcionalidad del sistema debe percibirse como una extensión de este CU. Por razones de practicidad tal situación no ha sido consignada en el diagrama. |  |

***

***TP - 2026***

| Nombre : Importar Candidatos | Nro : 006 |  |
| --- | --- | --- |
| Actores : Consultor Objetivo : Este caso de uso permite importar los candidatos desde el sistema de candidatos y CV Precondiciones : Actor autenticado en el sistema. | Versión: 1.1 |  |
| Postcondiciones : Fracaso: ▪ Imposibilidad de importar candidatos. | Éxito: ▪ Importación de candidatos realizada con éxito. |  |
| Flujo Principal 1. El actor desea importar candidatos desde el sistema de gestión de candidatos y cv. 2. El sistema presenta una pantalla donde el usuario selecciona en qué carpeta del sistema operativo está el archivo csv a importar. 3. El actor selecciona el archivo csv a importar y presiona el botón Importar. 4. El sistema realiza la importación de los candidatos, mostrando luego de la importación un mensaje de éxito con la siguiente estadística: 4.1.Cantidad Candidatos creados 4.2.Cantidad Candidatos actualizados 4.3.Cantidad Candidatos eliminados 5. El actor presiona el botón aceptar. 6. El caso de uso finaliza y retorna a la pantalla principal del aplicativo. Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : | Flujo Alternativo |  |
| CU ▪ No posee | Condición |  |

***

***TP - 2026***

**Requerimientos Especiales**:

- ▪ El archivo csv a generar contendrá en la primer fila los nombres de las columnas a importar. ▪ El archivo csv contendrá las siguientes columnas, separadas por punto y coma (;): o Nro candidato o Apellido o Nombre o Nro documento o Tipo documento (DNI, LE, LC, PP) o Fecha de nacimiento (aaaa-mm-dd) o Género (H/M) o Nacionalidad o Email o Escolaridad o Eliminado (True/False) ▪ Dado que la importación puede tardar algunos segundos, se solicita se muestre un reloj o barra de estado de avance mientras dura la importación.

  **Observaciones**:

- ▪ En cualquier paso del caso del uso el actor puede cancelar la operación y retornar a la pantallla principal. ▪ Cuando se realiza la importación, se consideran como claves única el nro candidato en cada fila.

|  | Nombre : Exportar Resultados |  |  | Nro : 007 |  |
| --- | --- | --- | --- | --- | --- |
|  | Actores : Consultor |  |  | Versión: 1.1 |  |
|  | Objetivo |  | : Generar un archivo csv con los resultados obtenidos por los candidatos. |  |  |
|  | Precondiciones Postcondiciones : 1. obtenidos por los candidatos | : Actor autenticado en el sistema. Éxito: ▪ Archivo de texto generado. Fracaso: ▪ Flujo Principal El actor desea exportar los resultados evaluados en una función o puesto. | Imposibilidad de generar el archivo de texto. Flujo Alternativo |  |  |
|  | 2. El sistema presenta la pantalla de Búsqueda de Funciones con los o puesto, Empresa, Fecha de finalización de cuestionario(desde/hasta) | siguientes filtros: Nombre de Función |  |  |  |
|  | 3. El actor completa el/los filtros (al con los criterios de búsqueda. | menos uno) y el sistema completa la grilla con las funciones que cumplan |  |  |  |
|  | 4. El actor selecciona una función o puesto. |  |  |  |  |
|  | 5. El sistema presenta la lista de en la función seleccionada. muestra apellido, nombre, tipo documento, nro documento, nro | candidatos que han sido evaluados Se |  |  |  |
|  |  |  |  | 13 |  |

***

***TP - 2026***

| candidato, fecha de finalización del cuestionario. |  |  |
| --- | --- | --- |
| 6. El actor selecciona la opción “Exportar”. | 6.A. El actor presiona el botón exportar sin haber seleccionado una función. 6.A.1. El sistema presenta un mensaje indicando que se debe seleccionar una función. 6.A.2. El sistema retorna al paso 5 del flujo principal. |  |
| 7. El sistema presenta una pantalla donde el usuario selecciona en qué carpeta del sistema operativo desea guardar el archivo csv. |  |  |
| 8. El actor selecciona la carpeta y presiona el botón Guardar. |  |  |
| 9. El sistema guarda el archivo. |  |  |
| 10.El caso de uso finaliza y retorna a la pantalla principal del aplicativo. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : |  |  |
| CU | Condición |  |
| ▪ No posee |  |  |
| Requerimientos Especiales : ▪ El archivo csv a generar contendrá en la primer fila los nombres de las columnas a exportar. ▪ El archivo csv contendrá las siguientes columnas, separadas por punto y coma (;): o Nro candidato o Apellido o Nombre o Tipo documento o Nro documento o Código Competencia o Resultado o Nombre puesto o Código puesto o Fecha finalización cuestionario |  |  |
| Observaciones : ▪ Se considera que un candidato ha sido evaluado en una función cuando el estado del cuestionario es “Completo”. ▪ En cualquier paso del caso de uso el actor puede cancelar la operación y retornar a la pantalla principal. ▪ El nombre del archivo csv a exportar se conformará de la siguiente manera: aaaa-mm-dd-hh-mm-Empresa-NombreFunción.csv. |  |  |

***

***TP - 2026***

| Nombre : Gestionar Puestos o funciones | Nro : 008 |
| --- | --- |
| Actores : Consultor Objetivo : Este caso de uso permite realizar la Gestión de Puestos o funciones, buscando los mismos para modificarlos o eliminarlos o bien creando nuevos puestos. Precondiciones : ● Actor autenticado en el sistema | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Los datos de los puestos quedan en el estado deseado. |
| Flujo Principal 1. El actor desea gestionar los datos de los puestos y selecciona del menú la opción Puestos 2. El sistema presenta la pantalla en blanco para cargar datos de puestos que sirvan como criterios de búsqueda: ● Código, ● Nombre del Puesto y ● Empresa | Flujo Alternativo |
| 3. El actor ingresa datos en al menos uno de los criterios de búsqueda y presiona el botón Buscar. 4. El sistema presenta una lista de los puestos que coinciden con los criterios de búsqueda ingresados. Esta lista contiene: ● Código, ● Nombre del puesto, ● Empresa | 3.A. El actor presiona el botón Nuevo sin haber buscado ningún puesto. 3.A.1. El sistema invoca al caso de uso “ CU009 - Dar de alta Puesto o función ”. 3.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal. |
| 5. El actor selecciona un puesto de la lista. | 5.A. El actor presiona el botón Modificar. 5.A.1 El sistema invoca al caso de uso “ CU010 – Modificar Puesto o función ”. 5.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal 5.B. El actor presiona el botón Eliminar. 5.B.1 El sistema invoca al caso de uso “ CU011 – Dar de Baja Puesto o función ”. 5.B.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal |

***

***TP - 2026***

6. El caso de uso termina. **Asociaciones de Inclusión**: ▪ No posee **Asociaciones de Extensión**:

## CU Condición

CU009 – Dar de alta Puesto o función El actor presiona el botón Nuevo. CU010 – Modificar Puesto o función El actor presiona el botón Modificar. CU011 – Dar de baja Puesto o función El actor presiona el botón Eliminar.

**Requerimientos Especiales**:

- ▪ El criterio de búsqueda es “contiene”.

  **Observaciones**:

- ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir sin guardar los cambios realizados.

| Nombre : Dar de alta Puesto o función | Nro : 009 |
| --- | --- |
| Actores : Consultor Objetivo : Crear un nuevo puesto o función. Precondiciones : Actor autenticado en el sistema | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Los datos del nuevo puesto quedan en el sistema. |
| Flujo Principal 1. El sistema presenta la pantalla de alta de Puestos con los siguientes datos: ● Código, ● Nombre del Puesto, ● Descripción, ● Empresa, ● Características del Puesto (conformada por un conjunto de competencias y la ponderación deseada en cada una para este puesto). Además presenta un botón “Aceptar” y otro “Cancelar”. | Flujo Alternativo |
| 2. El actor ingresa TODOS los datos y presiona el botón “Aceptar”. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al punto 2 del flujo principal. 2.B. El código y/o nombre ya existen. |

***

***TP - 2026***

|  | 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al punto 2 del flujo principal. |  |
| --- | --- | --- |
| 3. El sistema registra el puesto y presenta un mensaje “El puesto Nombre Puesto se ha creado correctamente ¿Desea cargar otro? |  |  |
| 4. El sistema muestra dos botones: “SI” y “NO” |  |  |
| 5. El actor elige “NO” | 5.A. El actor elige “SI” 5.A.1. El CU vuelve al punto 1 del flujo principal. |  |
| 6. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| Observaciones : ▪ Cada competencia puede existir una sola vez para cada puesto. ▪ Las ponderaciones son valores entre 0 y 10. |  |  |

| Nombre | : Modificar Puesto o función |  | Nro : 010 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | : Modificar datos de un puesto. |  |  |
| Precondiciones | : Actor autenticado en el sistema. El puesto ya existe en el sistema. |  |  |
| Postcondiciones : | Éxito: ▪ sistema. | Los datos de un puesto quedan modificados en el |  |
|  | Fracaso: ▪ sistema | Los datos de un puesto quedan como estaban en el |  |
|  | Flujo Principal | Flujo Alternativo |  |
| 1. un puesto.El sistema presenta la se desea modificar. ▪ Código (no editable) ● Nombre delPuesto, ● Descripción, ● Empresa, | El actor desea modificar los datos de pantalla con los datos del puesto que |  |  |
|  |  |  | 17 |

***

***TP - 2026***

| ▪ Características del Puesto Además presenta un botón “Aceptar” y otro “Cancelar” |  |  |
| --- | --- | --- |
| 2. El actor modifica los datos que desea cambiar y presiona el botón “Aceptar”. | 2.A. El actor blanquea uno o más datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al punto 2 del flujo principal. 2.B. El Nombre ya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3 El CU va al punto 2 del flujo principal con foco en el campo Nombre. 2.C. Se desean agregar/quitar funciones al puesto y ya existen cuestionarios generados para el mismo. 2.C.1. El sistema avisa de tal situación. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3 El CU va al punto 2 del flujo principal con foco en el campo Nombre. |  |
| 3. El sistema actualiza el puesto y presenta el mensaje “La operación ha culminado con éxito” |  |  |
| 4. El CU termina |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. ▪ |  |  |

***

***TP - 2026***

**Observaciones**:

- ▪ Cada competencia puede existir una sola vez para cada puesto. ▪ Las ponderaciones son valores entre 0 y 10.

| Nombre : Dar de baja un Puesto o función | Nro : 011 |  |
| --- | --- | --- |
| Actores : Consultor Objetivo : Eliminar un puesto. Precondiciones : Actor autenticado en el sistema. El puesto ya existe en el sistema. | Versión: 1.1 |  |
| Postcondiciones : Fracaso: ▪ Los datos del puesto quedan como estaban en el sistema | Éxito: ▪ Los datos del puesto son eliminados del sistema. |  |
| Flujo Principal 1. El actor desea eliminar un puesto del sistema y presiona el botón Eliminar. | Flujo Alternativo |  |
| 2. El sistema constata que el puesto que se desea eliminar no esté en uso en la base de datos (no tenga evaluaciones asociados): El sistema presenta el mensaje “Los datos del puesto Nombre Puesto serán eliminados del sistema” y dos botones: “ELIMINAR” y “CANCELAR” | 2.A. El sistema constata que el puesto está en uso y presenta el mensaje “El puesto Nombre Puesto está siendo usado en la base de datos y no puede eliminarse”. 2.A.1 El actor presiona cualquier tecla. 2.A.2 El CU termina. |  |
| 3. El actor presiona la tecla “ELIMINAR”. El sistema elimina lógicamente el puesto y presenta el cartel: “Los datos del puesto Nombre Puesto han sido eliminados del sistema.” 4. El actor presiona cualquier tecla. 5. El CU termina Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : CU Condición ▪ No Posee Requerimientos Especiales : ▪ Se debe generar un registro de auditoría indicando el usuario que eliminó el puesto y fecha y hora de dicha eliminación. Observaciones : ▪ | 3.A.El actor presiona la tecla “CANCELAR” 3.A.1. El CU termina. |  |

| Nombre : Gestión de Competencias | Nro : 012 |
| --- | --- |
| Actores : Consultor Objetivo : Este caso de uso permite realizar la Gestión de Competencias, buscando las mismas para modificarlas o eliminarlas o bien creando nuevas competencias. Precondiciones : Actor autenticado en el sistema | Versión: 1.1 |

***

***TP - 2026***

| Postcondiciones : | Éxito: ▪ Los datos de las competencias quedan en el estado deseado. |
| --- | --- |
| Fracaso: ▪ La operación no se puede concretar. |  |
| Flujo Principal 1. El actor desea gestionar los datos de las competencias y selecciona del menú la opción Competencias 2. El sistema presenta la pantalla en blanco para cargar datos de competencias que sirvan como criterios de búsqueda: ▪ Nombre de competencia | Flujo Alternativo |
| 3. El actor ingresa datos en el campo Nombre y presiona el botón Buscar (si presiona el botón buscar sin ingresar datos en el campo nombre el sistema retorna todas las competencias existentes en la base de datos) 4. El sistema presenta una lista de las competencias que coinciden con el nombre ingresado. Esta lista contiene el código de la competencia y el nombre. | 3.A. El actor presiona el botón Nuevo. 3.A.1. El sistema invoca al caso de uso “ CU013 - Dar de alta Competencia ”. 3.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal |
| 5. El actor selecciona una competencia de la lista. 6. El caso de uso termina. Asociaciones de Inclusión : ▪ Asociaciones de Extensión : CU Condición CU013 – Dar de alta Competencia El actor presiona el botón Nuevo. CU014 – Modificar Competencia El actor presiona el botón Modificar. CU015 – Eliminar Competencia El actor presiona el botón Eliminar. Requerimientos Especiales : ▪ El criterio de búsqueda es “contiene”. | 5.A. El actor presiona el botón Eliminar. 5.A.1 El sistema invoca al caso de uso “ CU015 - Eliminar Competencia ”. 5.A.2El sistema actualiza la grilla y continúa en el paso 3 del flujo normal. 5.B. El actor presiona el botón Modificar. 5.B.1 El sistema invoca al caso de uso “ CU014 - Modificar Competencia ”. 5.B.2El sistema actualiza la grilla y continúa en el paso 3 del flujo normal. |

***

***TP - 2026***

**Observaciones**:

- ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir sin guardar los cambios realizados.

| Nombre : Dar de alta Competencia | Nro : 013 |  |
| --- | --- | --- |
| Actores : Consultor Objetivo : Crear una nueva competencia. Precondiciones : Actor autenticado en el sistema | Versión: 1.1 |  |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Los datos de la nueva competencia quedan en el sistema. |  |
| Flujo Principal 1. El sistema presenta la pantalla de alta de Competencia con los siguientes datos: Código, Nombre, Descripción. | Flujo Alternativo |  |
| 2. El actor ingresa TODOS los datos y presiona el botón “Aceptar”. 3. El sistema registra la competencia y presenta un mensaje “La competencia Nombre Competencia se ha creado correctamente ¿Desea cargar otra? 4. El sistema muestra dos botones: “SI” y “NO” | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. El código y/o nombre ya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. |  |
| 5. El actor elige “NO” 6. El CU termina. Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : | 5.A. El actor elige “SI” 5.A.1. El CU vuelve al paso 1 del flujo principal. |  |
| CU ▪ No posee | Condición |  |

***

***TP - 2026***

**Requerimientos Especiales**:

- ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas.

  **Observaciones**:

- ▪

| Nombre : Modificar Competencia | Nro : 014 |
| --- | --- |
| Actores : Consultor Objetivo : Modificar datos de una competencia. Precondiciones : Actor autenticado en el sistema. La competencia ya existe en el sistema. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ Los datos de una competencia quedan como estaban en el sistema | Éxito: ▪ Los datos de una competencia quedan modificados en el sistema. |
| Flujo Principal 1. El actor desea modificar los datos de una competencia.El sistema presenta la pantalla con los datos de la competencia que se desea modificar. ▪ Código (no editable) ▪ Nombre ▪ Descripción Además presenta un botón “Aceptar” y otro “Cancelar” | Flujo Alternativo |
| 2. El actor modifica los datos que desea cambiar y presiona el botón “Aceptar”. 3. El sistema actualiza la competencia y presenta un mensaje “La operación ha culminado con éxito” | 2.A. El actor blanquea uno o más datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. El Nombre ya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3 El CU va al paso 2 del flujo principal con foco en el campo Nombre. |

***

***TP - 2026***

4. El CU termina. **Asociaciones de Inclusión**: ▪ No posee **Asociaciones de Extensión**:

## CU Condición

- ▪

  **Requerimientos Especiales**:

- ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas.

  **Observaciones**:

- ▪

| Nombre : Dar de baja Competencia | Nro : 015 |
| --- | --- |
| Actores : Consultor Objetivo : Eliminar una competencia. Precondiciones : Actor autenticado en el sistema. La competencia ya existe en el sistema. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ Los datos de la competencia quedan como estaban en el sistema | Éxito: ▪ Los datos de la competencia son eliminados del sistema. |
| Flujo Principal 1. El actor desea eliminar una competencia del sistema y presiona el botón Eliminar. | Flujo Alternativo |
| 2. El sistema constata que la competencia que desea eliminar no esté en uso en la base de datos (no tenga factores asociados y no esté asociada a una función o puesto): El sistema presenta el mensaje “Los datos de la competencia Nombre Competencia serán eliminados del sistema” y dos botones: “ELIMINAR” y “CANCELAR” | 2.A. El sistema constata que la competencia está en uso en el sistema y presenta el mensaje “La competencia Nombre Competencia está siendo usada en la base de datos y no puede eliminarse”. 2.A.1 El actor presiona cualquier tecla. 2.A.2 El CU termina. |
| 3. El actor presiona el botón “ELIMINAR”. El sistema elimina lógicamente la competencia y presenta el cartel: “Los datos de la competencia Nombre Competencia han sido eliminados del sistema.” 4. El actor presiona cualquier tecla. 5. El CU termina. Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : | 3.A.El actor presiona la tecla “CANCELAR” 3.A.1. El CU termina. |

***

***TP - 2026***

## CU Condición

- ▪ No posee

  **Requerimientos Especiales**:

- ▪ Se debe generar un registro de auditoría indicando el usuario que eliminó la competencia y fecha y hora de dicha eliminación.

  **Observaciones**:

- ▪

| Nombre : Gestión de Factores | Nro : 016 |
| --- | --- |
| Actores : Consultor Objetivo : Este caso de uso permite realizar la Gestión de Factores, buscando los mismos para modificarlos o eliminarlos o bien creando nuevos factores. Precondiciones : Actor autenticado en el sistema Competencias existentes en el sistema | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Los datos de los factores quedan en el estado deseado. |
| Flujo Principal 1. El actor desea gestionar los datos de los factores y selecciona del menú la opción Factores 2. El sistema presenta la pantalla en blanco para cargar datos de factores que sirvan como criterios de búsqueda: ▪ Nombre de competencia ▪ Nombre de factor | Flujo Alternativo |
| 3. El actor ingresa datos en el/los campos de filtrado y presiona el botón Buscar. 4. El sistema presenta una lista de los factores que coinciden con los valores ingresados en los filtros. Esta lista contiene el código de factor, nombre de factor y competencia. La lista se muestra ordenada por nombre de factor. | 3.A. El actor presiona el botón Nuevo. 3.A.1. El sistema invoca al caso de uso “ CU017 - Dar de alta Factor ”. 3.A.2El sistema actualiza la grilla y continúa en el paso 3 del flujo normal. |
| 5. El actor selecciona un factor de la lista. | 5.A. El actor presiona el botón Eliminar. 5.A.1 El sistema invoca al caso de uso “ CU019 - Eliminar Factor ”. 5.A.2El sistema actualiza la grilla y continúa en el paso 3 del flujo normal. 5.B. El actor presiona el botón Modificar. 5.B.1 El sistema invoca al caso de uso “ CU018 - Modificar Factor ”. |

***

***TP - 2026***

5.B.2El sistema actualiza la grilla y continúa en el paso 3 del flujo normal.
6\. El caso de uso termina. **Asociaciones de Inclusión**: ▪ **Asociaciones de Extensión**:

## CU Condición

CU017 – Dar de alta Factor El actor presiona el botón Nuevo. CU018 – Modificar Factor El actor presiona el botón Modificar. CU019 – Eliminar Factor El actor presiona el botón Eliminar.

**Requerimientos Especiales**:

- ▪ El criterio de búsqueda es “contiene”. ▪

  **Observaciones**:

- ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir sin guardar los cambios realizados.

| Nombre : Dar de alta Factor | Nro : 017 |
| --- | --- |
| Actores : Consultor Objetivo : Crear un nuevo factor. Precondiciones : Actor autenticado en el sistema Al menos una competencia creada en el sistema. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Los datos del nuevo factor quedan en el sistema. |
| Flujo Principal 1. El sistema presenta la pantalla de alta de Factor con los siguientes datos: Código, Nombre, Descripción, Competencia (selección), Nro de orden (selección). | Flujo Alternativo |
| 2. El actor ingresa TODOS los datos y presiona el botón “Aceptar”. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. El código y/onombreya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. 2.C. El nro de orden ya existe para la competencia seleccionada. |

***

***TP - 2026***

|  | 2.C.1. El sistema avisa de tal situación. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3. El CU vuelve al paso 2 del flujo principal. |  |
| --- | --- | --- |
| 3. El sistema registra el factor y presenta un mensaje “El factor Nombre\\_Factor se ha creado correctamente ¿Desea cargar otro? |  |  |
| 4. El sistema muestra dos botones: “SI” y “NO” |  |  |
| 5. El actor elige “NO” | 5.A. El actor elige “SI” 5.A.1. El CU vuelve al paso 1 del flujo principal. |  |
| 6. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : |  |  |
| CU | Condición |  |
| ▪ No posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. ▪ Se debe contemplar un mecanismo que facilite gestionar el orden del factor dentro de la competencia. |  |  |
| Observaciones : ▪ |  |  |

| Nombre : Modificar Factor |  |  | Nro : 018 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | : Modificar datos de un factor. |  |  |
| Precondiciones : | Actor autenticado en el sistema. El factor ya existe en el sistema. |  |  |
| Postcondiciones : | Éxito: ▪ sistema. | Los datos de un factor quedan modificados en el |  |
|  | Fracaso: ▪ sistema | Los datos de un factor quedan como estaban en el |  |
|  | Flujo Principal | Flujo Alternativo |  |
| 1. El actor desea modificar un factor.El sistema presenta la se desea modificar. ▪ Código (no editable) ▪ Nombre, ▪ Descripción, ▪ Competencia (selección), | los datos de pantalla con los datos del factor que |  |  |
|  |  |  | 26 |

***

***TP - 2026***

| ▪ Nro de orden(selección) Además presenta un botón “Aceptar” y otro “Cancelar” |  |  |
| --- | --- | --- |
| 2. El actor modifica los datos que desea cambiar y presiona el botón “Aceptar”. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. El código y/o nombre ya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. 2.C. El nro de orden ya existe para la competencia seleccionada. 2.C.1. El sistema avisa de tal situación. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3. El CU vuelve al paso 2 del flujo principal. |  |
| 3. El sistema actualiza el factor y presenta un mensaje “La operación ha culminado con éxito” |  |  |
| 4. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : |  |  |
| CU | Condición |  |
| ▪ |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| Observaciones : ▪ |  |  |

| Nombre : Dar de baja Factor | Nro : 019 |
| --- | --- |
| Actores : Consultor Objetivo : Eliminar un factor. | Versión: 1.1 |

***

***TP - 2026***

| Precondiciones : Actor autenticado en el sistema. El factor ya existe en el sistema. |  |  |
| --- | --- | --- |
| Postcondiciones : | Éxito: ▪ Los datos del factor son eliminados del sistema. |  |
| Fracaso: ▪ Los datos del factor quedan como estaban en el sistema |  |  |
| Flujo Principal | Flujo Alternativo |  |
| 1. El actor desea eliminar un factor del sistema y presiona el botón Eliminar. |  |  |
| 2. El sistema presenta el mensaje “Los datos del factor Nombre Factor y sus preguntas serán eliminados del sistema” y dos botones: “ELIMINAR” y “CANCELAR” |  |  |
| 3. El actor presiona la tecla “ELIMINAR”. El sistema elimina lógicamente el factor y presenta el cartel: “Los datos del factor Nombre Factor han sido eliminados del sistema.” | 3.A.El actor presiona la tecla “CANCELAR” 3.A.1. El CU termina. |  |
| 4. El actor presiona cualquier tecla. |  |  |
| 5. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : |  |  |
| CU | Condición |  |
| ▪ No posee |  |  |
| Requerimientos Especiales : ▪ Se debe generar un registro de auditoría indicando el usuario que eliminó el factor y fecha y hora de dicha eliminación. ▪ Cuando se elimina un factor, se eliminan lógicamente todas las preguntas asociadas al mismo, y se genera un registro de auditoría por cada pregunta eliminada, indicando el usuario que eliminó la pregunta y fecha y hora de dicha eliminación |  |  |
| Observaciones : |  |  |

| Nombre : Gestionar Preguntas |  |  | Nro : 020 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | mismas para modificarlas o eliminarlas o bien creando nuevas preguntas. | : Este caso de uso permite realizar la Gestión de Preguntas, buscando las |  |
| Precondiciones : | Actor autenticado en el sistema. Las opciones de respuestas deben haber sido previamente cargadas. |  |  |
| Postcondiciones : | Éxito: ▪ deseado. | Los datos de las preguntas quedan en el estado |  |
|  | Fracaso: ▪ | La operación no se puede concretar. |  |
|  | Flujo Principal | Flujo Alternativo |  |
| 1. la opción Preguntas | El actor desea gestionar los datos de las preguntas y selecciona del menú |  |  |

***

***TP - 2026***

| 2. El sistema presenta la pantalla en blanco para cargar los criterios de búsqueda: ● Competencia, ● Factor y ● Nombre de Pregunta |  |
| --- | --- |
| 3. El actor ingresa datos en al menos uno de los criterios de búsqueda y presiona el botón Buscar | 3.A. El actor presiona el botón Nuevo sin haber buscado ninguna pregunta. 3.A.1. El sistema invoca al caso de uso “ CU021 - Dar de alta Pregunta ”. 3.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal |
| 4. El sistema presenta una lista de las preguntas que coinciden con los criterios de búsqueda ingresado. Esta lista contiene: ● Competencia, ● Factor, ● Pregunta |  |
| 5. El actor selecciona una pregunta de la lista | 5.A. El actor presiona el botón Modificar. 5.A.1 El sistema invoca al caso de uso “ CU022 – Modificar Pregunta ”. 5.A.2 El sistema actualiza la grilla y continúa en el paso 4 del flujo normal 5.B. El actor presiona el botón Eliminar. 5.B.1 El sistema invoca al caso de uso “ CU023 – Dar de Baja Pregunta ”. 5.B.2 El sistema actualiza la grilla y continúa en el paso 4 del flujo normal |
| 6. El caso de uso termina. |  |
| Asociaciones de Inclusión : ▪ No posee |  |
| Asociaciones de Extensión : CU Condición CU021 – Dar de alta Pregunta El actor presiona el botón Nuevo. CU022 – Modificar Pregunta El actor presiona el botón Modificar. CU023 – Dar de baja Pregunta El actor presiona el botón Eliminar. |  |
| Requerimientos Especiales : ▪ El criterio de búsqueda es “contiene”. |  |
| Observaciones : ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir sin guardar los cambios realizados. |  |

| Nombre : Dar alta Pregunta | Nro : 021 |
| --- | --- |
| Actores : Consultor | Versión: 1.1 |
|  | 29 |

***

***TP - 2026***

| Objetivo : Crear una nueva pregunta. |  |
| --- | --- |
| Precondiciones : Actor autenticado en el sistema |  |
| Postcondiciones : | Éxito: ▪ Los datos de la nueva pregunta quedan en el sistema. |
| Fracaso: ▪ La operación no se puede concretar. |  |
| Flujo Principal | Flujo Alternativo |
| 1. El sistema presenta la pantalla de alta de Preguntas con los siguientes datos: ● Competencia (Selección), ● Factor (Selección), ● Nombre de Pregunta ● Pregunta, ● Descripción (Opcional), ● Opción de Respuesta (Selección). Además presenta un botón “Aceptar” y otra “Cancelar”. | 1.A El actor presiona el botón “Generar con IA” 1.A.1 Se inicia el CU 33 - Generar con IA. |
| 2. El actor ingresa TODOS los datos y presiona el botón “Aceptar”. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. Alguno de los campos excede su longitud máxima 2.B.1. El sistema muestra un mensaje de error detallando TODAS los campos en los que se ha incurrido, sin tapar campos ni botones. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. 2.C. La pregunta ya existe definida para esa competencia y factor (1). 2.C.1. El sistema avisa de tal situación. Clarificando cuál es la validación no superada. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3. El CU vuelve al paso 2 del flujo principal. |
| 3. El sistema registra la nueva pregunta y presenta un mensaje “La Pregunta |  |

***

***TP - 2026***

| Nombre Pregunta se ha creado correctamente ¿Desea cargar otra? |  |  |
| --- | --- | --- |
| 4. El sistema muestra dos botones: “SI” y “NO” |  |  |
| 5. El actor elige “NO” | 5.A. El actor elige “SI” 5.A.1. El CU vuelve al paso 1 del flujo principal. |  |
| 6. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| Observaciones : ▪ (1) La validación de existencia se realiza tanto en lo que respecta al nombre de la pregunta como de la pregunta en cuestión. ▪ Las ponderaciones de las opciones de respuesta deben sumar 10 puntos y son siempre 0 o positivas. ▪ Cuando se selecciona una Competencia, el sistema carga todos los Factores correspondientes a la Competencia. ▪ Al momento de seleccionar la Opción de Respuesta, se deberá mostrar la grilla de Opciones 3 columnas: Orden (solo lectura), Descripción (Sólo lectura) y Ponderación (editable) ordenada por orden |  |  |

| Nombre : Modificar Pregunta |  |  | Nro : 022 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | : Modificar datos de una pregunta. |  |  |
| Precondiciones | : Actor autenticado en el sistema. La pregunta ya existe en el sistema. |  |  |
| Postcondiciones : | Éxito: ▪ sistema. | Los datos de una pregunta quedan modificados en el |  |
|  | Fracaso: ▪ sistema | Los datos de una pregunta quedan como estaban en el |  |
|  | Flujo Principal | Flujo Alternativo |  |
| 1. que se desea modificar. ● ● Factor (Sólo Lectura), ● Nombre de Pregunta ● Pregunta, ● Descripción (Opcional), | El actor desea modificar los datos de una pregunta.El sistema presenta la pantalla con los datos de la pregunta Competencia (Sólo Lectura), |  |  |
|  |  |  | 31 |

***

***TP - 2026***

| ● Opción de Respuesta (Selección). Además presenta un botón “Aceptar” y otro “Cancelar” |  |  |
| --- | --- | --- |
| 2. El actor modifica los datos que desea cambiar y presiona el botón “Aceptar”. | 2.A. El actor blanquea uno o más datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al punto 2 del flujo principal. 2.B. Alguno de los campos excede su longitud máxima 2.B.1. El sistema muestra un mensaje de error detallando TODAS los campos en los que se ha incurrido, sin tapar campos ni botones. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al punto 2 del flujo principal. 2.C. La pregunta ya existe definida para esa competencia y factor (1). 2.C.1. El sistema avisa de tal situación. Clarificando cuál es la validación no superada. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3 El CU va al punto 2 del flujo principal con foco en el campo Nombre. |  |
| 3. El sistema actualiza la pregunta y presenta un mensaje “La operación ha culminado con éxito” |  |  |
| 4. El CU termina |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |

***

***TP - 2026***

**Observaciones**:

- ▪ (1) La validación de existencia se realiza tanto en lo que respecta al nombre de la pregunta como de la pregunta en cuestión. ▪ Las ponderaciones de las opciones de respuesta deben sumar 10 puntos y son siempre 0 o positivas. ▪ Cuando se selecciona una Competencia, el sistema carga todos los Factores correspondientes a la Competencia. ▪ Al momento de seleccionar la Opción de Respuesta, se deberá mostrar la grilla de Opcionesy3 columnas: Orden (solo lectura), Descripción (Sólo lectura) y Ponderación (editable) ordenada por orden

| Nombre : Dar de baja una Pregunta | Nro : 023 |  |
| --- | --- | --- |
| Actores : Consultor Objetivo : Eliminar una pregunta. Precondiciones : Actor autenticado en el sistema. La pregunta ya existe en el sistema. | Versión: 1.1 |  |
| Postcondiciones : Fracaso: ▪ Los datos de la pregunta quedan como estaban en el sistema | Éxito: ▪ Los datos de la pregunta son eliminados del sistema. |  |
| Flujo Principal 1. El actor desea eliminar una pregunta del sistema y presiona el botón Eliminar. 2. El sistema presenta el mensaje “Los datos dela pregunta Nombre Pregunta serán eliminados del sistema” y dos botones: “ELIMINAR” y “CANCELAR” | Flujo Alternativo |  |
| 3. El actor presiona el botón “ELIMINAR”. El sistema elimina lógicamente lapregunta y presenta el cartel: “Los datos delapregunta Nombre Pregunta han sido eliminados del sistema.” 4. El actor presiona cualquier tecla. 5. El CU termina Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : CU Condición ▪ No Posee Requerimientos Especiales : ● Se debe generar un registro de auditoría indicando el usuario que eliminó la pregunta y fecha y hora de dicha eliminación. Observaciones : ▪ | 3.A.El actor presiona la tecla “CANCELAR” 3.A.1. El CU termina. |  |

**Nombre**: Gestionar Opciones de Respuestas **Nro**: 024 **Actores**: Consultor **Versión: 1.1**

***

***TP - 2026***

| Objetivo : Este caso de uso permite realizar la Gestión de las distintas opciones de respuestas que pueden ser vinculadas a las preguntas, buscando las mismas para modificarlas o eliminarlas o bien creando nuevas. |  |
| --- | --- |
| Precondiciones : ● Actor autenticado en el sistema. |  |
| Postcondiciones : | Éxito: ▪ Los datos de las opciones de respuesta quedan en el estado deseado. |
| Fracaso: ▪ La operación no se puede concretar. |  |
| Flujo Principal | Flujo Alternativo |
| 1. El actor desea gestionar los datos de las opciones de respuestay selecciona del menú la opción Opciones de Respuestas |  |
| 2. El sistema presenta la pantalla en blanco para cargar los criterios de búsqueda: ● Nombre de la opción de respuesta |  |
| 3. El actor ingresa datos en el campo Nombre y presiona el botón Buscar (si presiona el botón buscar sin ingresar datos en el campo nombre el sistema retorna todas las opciones de respuesta existentes en la base de datos) | 3.A. El actor presiona el botón Nuevo sin haber buscado ningunaopción de respuesta. 3.A.1. El sistema invoca al caso de uso “ CU029 - Dar de alta Opción de Respuesta ”. 3.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal |
| 4. El sistema presenta una lista de las opciones de respuestas que coinciden con los criterios de búsqueda ingresado. Esta lista contiene: ● Nombre, ● Descripción |  |
| 5. El actor selecciona una opción de respuesta de la lista | 5.A. El actor presiona el botón Modificar. 5.A.1 El sistema invoca al caso de uso “ CU030 – Modificar Opción de Respuesta ”. 5.A.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal 5.B. El actor presiona el botón Eliminar. 5.B.1 El sistema invoca al caso de uso “ CU031 – Dar de Baja Opción de Respuesta ”. 5.B.2 El sistema actualiza la grilla y continúa en el paso 3 del flujo normal |
| 6. El caso de uso termina. |  |

***

***TP - 2026***

**Asociaciones de Inclusión**:

- ▪ No posee

  **Asociaciones de Extensión**: **CU Condición** CU029 – Dar de alta Opción de El actor presiona el botón Nuevo.

| CU029 – Dar de alta Opción de | El actor presiona el botón Nuevo. |
| --- | --- |
| Respuesta |  |
| CU030 – Modificar Opción de Respuesta | El actor presiona el botón Modificar. |
| CU031 – Dar de baja Opción de | El actor presiona el botón Eliminar. |

Respuesta

**Requerimientos Especiales**:

- ▪ El criterio de búsqueda es “contiene”.

  **Observaciones**:

- ▪ En cualquier flujo del caso de uso el actor puede presionar el botón Salir sin guardar los cambios realizados.

| Nombre : Evaluar a Candidato | Nro : 025 |
| --- | --- |
| Actores : Consultor Objetivo : Evaluar a candidatos. Precondiciones : Actor autenticado en el sistema. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ No se pueden evaluar candidatos. | Éxito: ▪ Candidatos evaluados. |
| Flujo Principal 1. El actor desea evaluar a candidatos y selecciona del menú la opción Evaluar Candidatos. 2. El sistema presenta la pantalla de Búsqueda de Candidatos con los siguientes filtros: Apellido, Nombre, Número de Candidato 3. El actor completa el/los filtros y el sistema completa la grilla con los Candidatos que cumplan con los criterios de búsqueda. | Flujo Alternativo |
| 4. El actor selecciona uno o más candidatos y presiona el botón Agregar. | 4.A. No se han seleccionado candidatos. 4.A.1. El sistema presenta un mensaje indicando que se debe seleccionar uno o más candidatos. 4.A.2. El sistema retorna al paso 4 del flujo principal. |
| 5. El sistema agrega el/los candidatos en la grilla “Candidatos a evaluar”. | 5.A. El sistema selecciona uno o más candidatos de la grilla de candidatos a Evaluar y presiona el botón Quitar. 5.A.1. El sistema quita los candidatos de la grilla “Candidatos a Evaluar”. 5.A.2. El sistema retorna al paso 4 del flujo principal. |

***

***TP - 2026***

| 6. El actor presiona el botón “Siguiente”. | 6.A. Existen uno o más candidatos que tienen cuestionarios activos o en proceso. 6.A.1. El sistema presenta un mensaje indicando quién/quiénes poseen cuestionarios activos o en proceso y que no pueden ser evaluados nuevamente. 6.A.2. El sistema retorna al paso 4 del flujo principal. |
| --- | --- |
| 7. El sistema presenta la pantalla de Evaluación de Candidatos. |  |
| 8. El actor selecciona la función o puesto en la cual desea evaluar a los candidatos seleccionados, mostrando el nombre de la función o puesto y el nombre de la empresa. |  |
| 9. El sistema presenta a modo informativo la lista de competencias que tiene asociadas la función seleccionada, junto a la ponderación requerida en cada una de ellas. | 9.A. Si alguna de las competencias que posee la función o puesto NO posee al menos un factor que cumpla las condiciones para ser evaluado 9.A.1. El sistema muestra el/los nombres de las competencias que no pueden ser evaluadas. 9.A.2. El actor presiona Aceptar. 9.A.3. El caso de uso finaliza y vuelve a la pantalla principal. |
| 10.El actor presiona el botón “Siguiente”. 11.El sistema presenta la lista de Candidatos a evaluar (Apellido, Nombre, Tipo documento, Nrodocumento, Clave de ingreso) | 10.A. El actor no selecciona ninguna Función. 10.A.1. El sistema presenta un mensaje indicando que se debe seleccionar una función. 10.A.2. El sistema retorna al paso 8 del flujo principal. |
| 12.El actor presiona el botón Finalizar. 13.El sistema guarda la información ingresada y presenta un mensaje al usuario solicitando si desea exportar la lista de candidatos con sus claves a un archivo excel. | 12.A. El actor presiona el botón Cancelar. 12.A.1. El sistema retorna a la pantalla principal. 12.A.2. El caso de uso finaliza. |
| 14.El actor selecciona que desea exportar los datos de candidatos con sus claves a un archivo excel. 15.El sistema presenta una pantalla donde el usuario selecciona en qué | 14.A. El actor selecciona que no desea exportar los datos de candidatos con sus claves a un archivo excel. 14.A.1. El caso de uso finaliza. |

***

***TP - 2026***

- carpeta del sistema operativo desea guardar el archivo excel.

  16.El actor selecciona la carpeta y 16.A. El actor presiona el botón Cancelar. presiona el botón Guardar. 16.A.1. El caso de uso continúa
  en el paso 14 del flujo principal.

17.El sistema guarda el archivo.
18.El caso de uso finaliza y retorna a la pantalla principal del aplicativo. **Asociaciones de Inclusión**: ▪ No posee **Asociaciones de Extensión**:

## CU Condición

- ▪ No posee

  **Requerimientos Especiales**:

- ▪ Las claves de ingreso se generarán aleatoriamente y deberán contener letras y dígitos. Además serán de longitud de 8 caracteres. ▪ El nombre del archivo excel a guardar se conformará de la siguiente manera: aaaa-mm-dd-hh-mm-NombreFunción.xls. ▪ Al momento de generarse la evaluación, se debe garantizar que los cuestionarios generados para los candidatos se basen en el mismo modelo (competencias, factores, preguntas, opciones de respuestas). ▪ Cuando se genera la evaluación, se deberán crear los cuestionarios en estado “activo” para cada candidato.

  **Observaciones**:

- ▪

| Nombre : Completar Cuestionario | Nro : 026 |
| --- | --- |
| Actores : Candidato Objetivo : Realizar o completar el cuestionario creado para un candidato en un puesto dado. Precondiciones : ● Actor autenticado en el sistema ● Cuestionario existente para el candidato en estado activo o en proceso. ● No se haya superado la cantidad máxima de accesos permitidos al usuario. Si es así el sistema deberá presentar un mensaje indicando la situación, colocar al cuestionario en estado “incompleto”. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ Imposibilidad de concluir la realización del cuestionario. | Éxito: ▪ El cuestionario ha sido respondido. |
| Flujo Principal | Flujo Alternativo |
| 1. El cuestionario se encuentra en el estado activo. El sistema presenta la pantalla “Cuestionario – Instrucciones” Además presenta un botón “Aceptar” y otra “Cancelar”. | 1.A El usuario superó el tiempo máximo para completarel cuestionario (2) 1.A.1 El sistema coloca al cuestionario en estado “incompleto” y actualiza la hora y fecha en que fue puesto como incompleto 1.A.2. El sistema avisa de tal situación. 1.A.3. El sistema emite una suave alerta auditiva. 1.A.4 El CU finaliza. |

***

***TP - 2026***

|  | 1.B El cuestionario superó el tiempo máximo permitido para el estado activo 1.A.1 El sistema coloca al cuestionario en estado “Sin Contestar” y actualiza la hora y fecha en que fue puesto en ese estado 1.A.2. El sistema avisa de tal situación. 1.A.3. El sistema emite una suave alerta auditiva. 1.A.4 El CU finaliza. 1.C El actor ya había ingresado a completar el cuestionario previamente 1.A.1El sistema recupera de la base de datos el cuestionario que está en proceso de llenado por parte del usuario y se posiciona en el bloque que aún no ha sido respondido. 1.A.2 El CU continúa por el paso 5. |
| --- | --- |
| 2. El actor lee las instrucciones y presiona el botón Aceptar. | 2.A El actor presiona el botón Cancelar 2.A.1. El CU termina. |
| 3. El sistema recupera el cuestionarioy recupera las competencias que serán evaluadas. Por cada competencia recupera los factores asociados a las mismas y por cada factor (que disponga de al menos 2 preguntas), recupera las preguntas, con sus respectivas opciones de respuestas. El sistema asocia al cuestionario las preguntas (elegidas al azar) que serán utilizadas en el mismo (1). El sistema guarda el cuestionario que completará el usuario en la base de datos a fin de que si vuelve a ingresar el mismo pueda ser presentado de forma idéntica. Se registra en el cuestionario el día y hora de comienzo del mismo. El sistema actualiza el estado del cuestionario a “En Proceso”. |  |
| 4. El sistema presenta la pantalla de Cuestionario (wizard), posicionándose en el bloque (paso |  |

***

***TP - 2026***

| del wizard) que aún no ha sido respondido |  |  |
| --- | --- | --- |
| 5. El actor contesta todas las preguntas que se le presentan en pantalla y presiona el botón Siguiente (o Finalizar si es el último paso) |  |  |
| 6. El sistema valida que el actor haya respondido a todas las preguntas del bloque | 6.A. El actor no respondió a todas las preguntas 6.A.1. El sistema muestra un mensaje de error indicando que preguntas no han sido respondidas. 6.A.2. El sistema emite una suave alerta auditiva. 6.A.3. El CU vuelve al punto 5 manteniendo el bloque y las preguntas que ya se habían respondido. |  |
| 7. El sistema guarda el bloque del cuestionario contestado en la base de datos. |  |  |
| 8. Es el último paso del wizard. El sistema guarda los resultados del cuestionario (3) El sistema presenta un mensaje indicando que el Cuestionario ha sido completado con éxito El sistema cambia el estado del cuestionario, pasándola a “Completo” | 8.A Existen más bloques de preguntas. 8.A.1El sistema presenta la siguiente pantalla del wizard con un nuevo bloque de preguntas a contestar. 8.A.2 El CU continúa en el paso 5. |  |
| 9. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |

***

***TP - 2026***

**Observaciones**:

- ▪ (1) Cabe remarcar que cada cuestionario no tendrá las mismas preguntas, sino que aleatoriamente se tomarán 2 preguntas por factor de cada competencia a evaluar. Si la competencia a evaluar no posee definidos factores con al menos 2 preguntas, dicha competencia no podrá ser evaluada. Las preguntas en cada cuestionario se mostrarán en forma aleatoria, mezclando preguntas de los diferentes factores de las diferentes competencias.Con estas consideraciones el sistema ordena las preguntas y los agrupa en bloques (tomando el parámetro de cantidad de preguntas por cada paso). ▪ (2) El tiempo máximo se mide de la siguiente manera: se toma la fecha y hora actual y se le resta la fecha y hora de la primera vez que ingresó a realizar el cuestionario, si este resultado es menor o igual que el tiempo máximo que tiene definido el cuestionario puede ingresar a realizarlo. ▪ (3) (Algoritmo para el cálculo del puntaje de los factores y competencias): Por cada competencia {

  Por cada factor de la competencia { Sumar todas lasponderacionesde las opciones de respuestas seleccionadas para laspreguntas de dicho factor existentes en el cuestionario del candidato. Almacenar dicha suma dividido 2como total del factor, en el factor. } Sumar todos los totales de los factores y dividirlo por la cantidad de factores evaluados para la competencia y guardarlos como total de la competencia. }

- ▪ Las instrucciones del Cuestionario y los parámetros se toman de un repositorio de configuraciones del sistema.

| Nombre | : Emitir Reporte Comparativo |  | Nro : 027 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo obtenido por un candidato. |  | : Emitir un reporte comparativo entre lo solicitado para una función y lo |  |
| Precondiciones | : Actor autenticado en el sistema. Candidatos con cuestionario completos. |  |  |
| Postcondiciones : | Éxito: ▪ | Reporte comparativo generado. |  |
|  | Fracaso: ▪ | Imposibilidad de generar el reporte comparativo. |  |
| 1. El actor desea obtener un reporte comparativo de un candidato para una función o puesto. 2. El sistema presenta la pantalla de Búsqueda de Funciones con los siguientes filtros: Nombre de Función, Empresa 3. El actor completa el/los filtros y el sistema completa la grilla con | Flujo Principal | Flujo Alternativo |  |

***

***TP - 2026***

| lasfunciones que cumplan con los criterios de búsqueda. |  |  |
| --- | --- | --- |
| 4. El actor selecciona unafunción. |  |  |
| 5. El sistema presenta la lista de candidatos que han sido evaluados en la función seleccionada. Se muestra apellido, nombre, tipo de documento, documento, nro candidato, fecha de finalización del cuestionario. |  |  |
| 6. El actor selecciona un candidato de la lista y presiona el botón “Comparar” | 6.A. El actor no ha seleccionado un candidato de la lista 6.A.1. El sistema presenta un mensaje indicando que al menos se debe seleccionar un candidato. 6.A.2. El sistema retorna al paso 5 del flujo principal. 6.B. El actor presiona el botón Cancelar. 6.B.1. El caso de uso finaliza. |  |
| 7. El sistema presenta una grilla y un gráfico comparando los valores requeridos para cada competencia de la función o puesto vs los valores obtenidos por el candidato en cada competencia. |  |  |
| 8. El actor presiona el botón Imprimir y el sistema imprimir un reporte con el gráfico y la tabla de valores. | 8.A. El actor presiona el botón Cancelar. 8.A.1. El sistema retorna al paso 5 del flujo principal. |  |
| 9. El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : |  |  |
| CU | Condición |  |
| ▪ No posee |  |  |
| Requerimientos Especiales : ▪ |  |  |
| Observaciones : ▪ Se considera que un candidato ha sido evaluado en una función cuando el estado del cuestionario es “Completo”. |  |  |

| Nombre | : Emitir Orden de mérito | Nro : 028 |
| --- | --- | --- |
| Actores : Consultor |  | Versión: 1.1 |
| Objetivo por todos los candidatos. | : Emitir para un puesto o función particular el orden de mérito alcanzado |  |
| Precondiciones : ● | Actor autenticado en el sistema |  |
| Postcondiciones : | Éxito: ▪ El Orden de mérito se emite para el puesto seleccionado. Fracaso: ▪ Imposibilidad de emitir el orden de mérito. |  |
|  |  | 41 |

***

***TP - 2026***

| Flujo Principal | Flujo Alternativo |
| --- | --- |
| 1. El actor desea emitir el orden de mérito y selecciona del menú la opción Orden de Méritos. |  |
| 2. El sistema presenta la pantalla en blanco para cargar los criterios de búsqueda del puesto para el cual se quiere emitir el orden de mérito. ● Código, ● Nombre del Puesto y ● Empresa Además presenta un botón Aceptar y otro Cancelar |  |
| 3. El actor ingresa datos en al menos uno de los criterios de búsqueda y presiona el botón Buscar 4. El sistema presenta una lista de los puestos que coinciden con los criterios de búsqueda ingresado. Esta lista contiene: ● Código, ● Nombre del puesto, ● Empresa, ● Candidatos ● Evaluaciones Completas 5. El actor se posiciona en la grilla y selecciona un puesto pulsando en “Emitir el orden de mérito” | 2.A El actor presiona el botón Cancelar 2.A.1. El CU termina. |
| 6. El sistema solicita se seleccione para que evaluación/es se decide emitir el orden de mérito o la opción TODAS. 7. El sistema emite el listado de los candidatos evaluados para el puesto. ● En el reporte se deben mostrar todos los candidatos asociados a las evaluación/es seleccionada/s. ● Se considera que el orden de mérito está conformado por aquellos candidatos cuyos cuestionarios han sido completados y que hayan alcanzado las ponderaciones mínimas definidas para cada competencia en el puesto.Ordenando por puntaje obtenido. | 6.A El puesto no posee evaluaciones 6.A.1 El sistema muestra un mensaje indicando dicha situación. 6.A.2 El CU continúa en el paso 4 del flujo principal |

***

***TP - 2026***

| ● El resto de candidatos se debe agrupar por estado del cuestionario. ● Se deben mostrar también aquellos candidatos que no hayan alcanzado las ponderaciones mínimas definidas para cada competencia. Por cada candidatose debe mostrar: ● Tipo de documento ● Número de documento ● Apellido ● Nombre ● Puntaje Obtenido(1) ● Datos Adicionales (2) |  |  |
| --- | --- | --- |
| 8. El actor selecciona imprimir |  |  |
| 9. El sistema envía el listado a la impresora. |  |  |
| 10.El CU termina. |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| Observaciones : ▪ (1) En el caso de cuestionarios que no estén Completos no se muestra este dato. ▪ (2) En el caso de cuestionarios completos se debe indicar: Fecha Inicio, Fecha de Fin, Cantidad de Accesos. En el caso de cuestionarios incompletos: Fecha de Inicio, Último ingreso, Cantidad de Accesos. |  |  |

| Nombre | : Dar de alta Opción de Respuesta |  | Nro : 029 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | : Crear una nueva opción de respuesta. |  |  |
| Precondiciones | : Actor autenticado en el sistema |  |  |
| Postcondiciones : | Éxito: ▪ sistema. | Los datos de la nueva opción de respuesta quedan en el |  |
|  | Fracaso: ▪ | La operación no se puede concretar. |  |
|  | Flujo Principal | Flujo Alternativo |  |

***

***TP - 2026***

| 1. El sistema presenta la pantalla de alta de Opción de Respuesta con los siguientes datos: ● Nombre, ● Descripción, ● Lista de Opciones (orden de visualización, descripción). Además presenta un botón “Aceptar” y otro “Cancelar”. |  |
| --- | --- |
| 2. El actor ingresa TODOS los datos y presiona el botón “Aceptar”. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al punto 2 del flujo principal. 2.B. El nombre ya existe. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. 2.C. El Orden de visualización no es consecutivo. 2.C.1. El sistema avisa de tal situación. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3. El CU vuelve al paso 2 del flujo principal. 2.D. La descripción de opción esta más de una vez en la Opción de Respuesta. 2.D.1. El sistema avisa de tal situación. 2.D.2. El sistema emite una suave alerta auditiva. 2.D.3. El CU vuelve al punto 2 del flujo principal. |
| 3. El sistema registra la opción de respuesta y presenta un mensaje “La opción Nombre se ha creado correctamente ¿Desea cargar otra? |  |
| 4. El sistema muestra dos botones: “SI” y “NO” |  |
| 5. El actor elige “NO” | 5.A. El actor elige “SI” |

***

***TP - 2026***

5.A.1. El CU vuelve al punto 1 del flujo principal.
6\. El CU termina. **Asociaciones de Inclusión**: ▪ No posee **Asociaciones de Extensión**:

## CU Condición

- ▪ No Posee

  **Requerimientos Especiales**:

- ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas.

  **Observaciones**:

- ▪

| Nombre : Modificar Opción de Respuesta | Nro : 030 |
| --- | --- |
| Actores : Consultor Objetivo : Modificar datos de una opción de respuesta. Precondiciones : Actor autenticado en el sistema. La Opción de respuesta ya existe en el sistema. | Versión: 1.1 |
| Postcondiciones : Fracaso: ▪ Los datos de una opción de respuesta quedan como estaban en el sistema | Éxito: ▪ Los datos de una opción de respuesta quedan modificados en el sistema. |
| Flujo Principal 1. El actor desea modificar los datos de una opción de respuesta.El sistema presenta la pantalla con los datos que se desea modificar. ● Nombre (Sólo Lectura), ● Descripción (editable), ● Lista de Opciones (orden de visualización, descripción).No se pueden agregar o quitar opciones. Además presenta un botón “Aceptar” y otro “Cancelar” | Flujo Alternativo |
| 2. El actor modifica los datos que desea cambiar y presiona el botón “Aceptar”. | 2.A. El actor blanquea uno o más datos y presiona el botón “Aceptar” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. |

***

***TP - 2026***

|  | 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. 2.B. El Orden de visualización no es consecutivo. 2.B.1. El sistema avisa de tal situación. 2.B.2. El sistema emite una suave alerta auditiva. 2.B.3. El CU vuelve al paso 2 del flujo principal. 2.C. La descripción de opción esta más de una vez en la Opción de Respuesta. 2.C.1. El sistema avisa de tal situación. 2.C.2. El sistema emite una suave alerta auditiva. 2.C.3. El CU vuelve al paso 2 del flujo principal. |  |
| --- | --- | --- |
| 3. El sistema actualiza la opción de respuesta y presenta un mensaje “La operación ha culminado con éxito” |  |  |
| 4. El CU termina |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| Observaciones : ▪ |  |  |

| Nombre | : Dar de baja una opción de Respuesta |  | Nro : 031 |
| --- | --- | --- | --- |
| Actores : Consultor |  |  | Versión: 1.1 |
| Objetivo | : Eliminar una opción de respuesta. |  |  |
| Precondiciones | : Actor autenticado en el sistema. La opción de respuesta ya existe en el sistema. |  |  |
| Postcondiciones : | Éxito: ▪ sistema. | Los datos dela opción de respuesta son eliminados del |  |
|  | Fracaso: ▪ estaban en el sistema | Los datos dela opción de respuesta quedan como |  |
|  | Flujo Principal | Flujo Alternativo |  |
|  |  |  | 46 |

***

***TP - 2026***

| 1. El actor desea eliminar una opción de respuesta del sistema y presiona el botón Eliminar. |  |  |
| --- | --- | --- |
| 2. El sistema constata que la opción de respuesta que se desea eliminar no esté en uso en la base de datos (no tenga preguntas asociadas): El sistema presenta el mensaje “Los datos dela opción de respuesta Nombre Opción serán eliminados del sistema” y dos botones: “ELIMINAR” y “CANCELAR” | 2.A. El sistema constata que la opción de respuesta está en uso en el sistema y presenta el mensaje “La opción de respuesta Nombre Opción está siendo usada en la base de datos y no puede eliminarse”. 2.A.1 El actor presiona cualquier tecla. 2.A.2 El CU termina. |  |
| 3. El actor presiona la tecla “ELIMINAR”. El sistema elimina lógicamente la opción de respuesta y presenta el cartel: “Los datos dela opción de respuesta Nombre Opción han sido eliminados del sistema.” | 3.A.El actor presiona la tecla “CANCELAR” 3.A.1. El CU termina. |  |
| 4. El actor presiona cualquier tecla. |  |  |
| 5. El CU termina |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ Se debe generar un registro de auditoría indicando el usuario que eliminó la opción de respuesta y fecha y hora de dicha eliminación. |  |  |
| Observaciones : ▪ |  |  |

| Nombre | : Finalizar cuestionarios |  | Nro : 032 |
| --- | --- | --- | --- |
| Actores : Sistema |  |  | Versión: 1.1 |
| Objetivo Precondiciones : | cuyos plazos definidos para ser completados se han superado. | : Actualizar la información y dar por finalizados aquellos cuestionarios |  |
| Postcondiciones : | Éxito: ▪ al estado correspondiente. | Todos los cuestionarios que han superado los plazos para ser completas al momento de la ejecución, pasan |  |
|  | Fracaso: ▪ | Imposibilidad de actualizar los cuestionarios |  |
| 1. El actor en forma periódica desea actualizar los cuestionarios cuyos plazos para ser completados han vencido. | Flujo Principal | Flujo Alternativo |  |

***

***TP - 2026***

| 2. El sistema recupera aquellos cuestionarios en los cuales se superó el tiempo máximo para completar el cuestionario. El sistema actualizada los cuestionarios alestado “incompleto” y actualiza la hora y fecha del cambio de estado |  |  |
| --- | --- | --- |
| 3. El sistema recupera todos los cuestionarios que han superado el tiempo máximo permitido para el estado activo El sistema colocalos cuestionarios en estado “Sin Contestar” y actualiza la hora y fecha del cambio de estado |  |  |
| 4. El CU termina |  |  |
| Asociaciones de Inclusión : ▪ No posee |  |  |
| Asociaciones de Extensión : CU Condición ▪ No Posee |  |  |
| Requerimientos Especiales : ▪ Se debe generar un registro de ejecución del proceso informando fecha y hora y acción (inicio o finalización de la ejecución) |  |  |
| Observaciones : ▪ |  |  |

***

***TP - 2026***

| Nombre : Generar pregunta con Inteligencia Artificial | Nro : 033 |
| --- | --- |
| Actores : Consultor Objetivo : Crear una nueva pregunta utilizando IA Precondiciones : Actor autenticado en el sistema. El Consultor presionó el botón “Generar con IA” | Versión: 1.0 |
| Postcondiciones : Fracaso: ▪ La operación no se puede concretar. | Éxito: ▪ Se crea una nueva pregunta usando IA. |
| Flujo Principal 1. El sistema presenta una ventana emergente con los siguientes datos: ● Competencia (Selección), ● Factor (Selección), Además presenta un botón “Generar pregunta” y otra “Cancelar”. | Flujo Alternativo |
| 2. El actor ingresa TODOS los datos y presiona el botón “Generar pregunta”. 3. Se invoca un servicio externo de IA pasando los parámetros cargados: competencia, factor, descripción y opciones de respuesta. | 2.A. El actor NO ingresa TODOS los datos y presiona el botón “Generar pregunta” 2.A.1. El sistema muestra un mensaje de error detallando TODAS las omisiones en las que se ha incurrido, sin tapar campos ni botones. 2.A.2. El sistema emite una suave alerta auditiva. 2.A.3. El CU vuelve al paso 2 del flujo principal. |
| 4. Se cargan automáticamente (según lo respondido por el servicio) los datos faltantes ● Nombre de Pregunta ● Pregunta ● Descripción (Opcional) ● Opción de Respuesta (Selección) 5. Se vuelve al paso 2 del Flujo Principal del CU 21. Asociaciones de Inclusión : ▪ No posee Asociaciones de Extensión : CU Condición ▪ No Posee | 4.A No se puede conectar con el servicio. Se muestra un mensaje emergente “Ocurrió un error al comunicarse con el servicio de IA. Intente nuevamente más tarde” |

***

***TP - 2026***

| Requerimientos Especiales : ▪ La combinación de teclas <shift></shift><tab></tab> retrocederán un campo. ▪ El click y la tecla <enter></enter> tendrán el mismo efecto sobre un botón. ▪ No importando el estado de la tecla \\<bloqmayús>, el ingreso de datos literales será SIEMPRE en mayúsculas. |  |  |
| --- | --- | --- |
| Observaciones : ▪ (1) La validación de existencia se realiza tanto en lo que respecta al nombre de la pregunta como de la pregunta en cuestión. ▪ Las ponderaciones de las opciones de respuesta deben sumar 10 puntos y son siempre 0 o positivas. ▪ Cuando se selecciona una Competencia, el sistema carga todos los Factores correspondientes a la Competencia. |  |  |