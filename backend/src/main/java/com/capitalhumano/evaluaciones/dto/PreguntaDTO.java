package com.capitalhumano.evaluaciones.dto;

import java.util.List;

public record PreguntaDTO(
        int idPregunta,
        String nombre,
        String textoPregunta,
        String descripcion,
        boolean activo,
        String nombreCompetencia,
        String nombreFactor,
        List<OpcionPreguntaDTO> opciones
) {
}
