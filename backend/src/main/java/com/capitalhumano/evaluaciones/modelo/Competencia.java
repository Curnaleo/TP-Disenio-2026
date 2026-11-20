package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Competencia {
    private String codigo;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private List<Factor> factores = new ArrayList<>();
}
