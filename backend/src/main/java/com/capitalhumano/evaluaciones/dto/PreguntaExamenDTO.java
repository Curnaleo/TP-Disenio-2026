package com.capitalhumano.evaluaciones.dto;

import java.util.List;

public record PreguntaExamenDTO(
        int idPregCuest,
        int nroOrden,
        String textoPregunta,
        String descripcion,
        List<OpcionPreguntaDTO> opciones
) {
}
