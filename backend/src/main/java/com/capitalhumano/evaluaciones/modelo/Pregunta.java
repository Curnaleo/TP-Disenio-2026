package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pregunta {
    private int idPregunta;
    private String nombre;
    private String textoPregunta;
    private String descripcion;
    private boolean activo;
    private OpcionRespuesta opcionRespuesta;
    private List<OpcionPregunta> opciones = new ArrayList<>();
}
