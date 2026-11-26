package com.grupo30.capitalhumano.evaluaciones.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreguntaExamenDTO {
    private int idPregCuest;
    private int nroOrden;
    private String textoPregunta;
    private String descripcion;
    private List<OpcionPreguntaDTO> opciones;
}
