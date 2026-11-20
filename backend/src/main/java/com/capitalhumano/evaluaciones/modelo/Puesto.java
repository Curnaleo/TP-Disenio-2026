package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Puesto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String empresa;
    private boolean activo;
    private List<CaracteristicaPuesto> caracteristicas = new ArrayList<>();
}
