package com.grupo30.capitalhumano.evaluaciones.dto;

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

public class OpcionPreguntaDTO {
    private Integer idOpcionPregunta;
    private Integer orden;
    private String descripcion;
    private Double ponderacion;
}
