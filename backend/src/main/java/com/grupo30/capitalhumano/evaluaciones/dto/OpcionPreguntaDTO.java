package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;

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
