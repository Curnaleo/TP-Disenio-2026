package com.capitalhumano.evaluaciones.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreguntaCuestionario {
    private int idPregCuest;
    private int nroOrden;
    private boolean respondida;
    private Pregunta pregunta;
    private OpcionPregunta opcionElegida;
}
