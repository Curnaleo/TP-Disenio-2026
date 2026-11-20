package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpcionRespuesta {
    private int idOpcionRespuesta;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private List<ItemOpcionRespuesta> items = new ArrayList<>();
}
