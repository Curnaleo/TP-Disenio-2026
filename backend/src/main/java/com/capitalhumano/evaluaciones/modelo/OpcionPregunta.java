package com.capitalhumano.evaluaciones.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpcionPregunta {
    private int idOpcionPregunta;
    private double ponderacion;
    private ItemOpcionRespuesta itemOpcionRespuesta;
}
