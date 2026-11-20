package com.capitalhumano.evaluaciones.dto;

public record OpcionPreguntaDTO(
        int idOpcionPregunta,
        int orden,
        String descripcion,
        double ponderacion
) {
}
