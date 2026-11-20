package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Factor {
    private String codigo;
    private String nombre;
    private String descripcion;
    private int nroOrden;
    private boolean activo;
    private List<Pregunta> preguntas = new ArrayList<>();
}
