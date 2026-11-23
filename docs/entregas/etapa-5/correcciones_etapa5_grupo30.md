# Correcciones grupo 30 etapa 5


## CU 21
Buena resolucion del caso de uso. Mi unico interrogante tiene que ver con las opciones de respuesta de la pregunta. Como se crean dentro de esta pantalla, y como se persisten?
    Investiguen en tal caso, si es necesario persistirlas de manera transaccional con la pregunta


## CU 25

- Cuando muestran las caracteristicas del puesto, es decir las competencias, la resolucion es correcta si suponemos que estan usando un ORM que les trae la entidad Puesto y que configuraron un Eager Loading para las competencias. No obstante, si configuraron el ORM con Lazy loading para esas propiedades, o no estan usando un ORM, tendrian que mostrar como es el proceso de buscar las competencias para un puesto o que alternativa utilizarian, por ejemplo un JOIN para evitarse el problema de las N+1 Queries

Faltaria mostrar cuando crean la entidad evaluacion, cuando la entidad cuestionario y como asocian a la evaluacion los cuestionarios. Esto seria importante, porque muestra ademas como se produce la persistenacia de ambos, que puede ademas requerir persistir todo en una misma transaccion. Evaluen como persistieron la evaluacion y el cuestionario en su diagrama.

## Veredicto
Buen trabajo, muy completo. Evaluen las correcciones pedidas e incorporenlas para la entrega de la proxima etapa 7