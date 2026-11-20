package com.capitalhumano.evaluaciones.dto;

import java.util.List;

public record OpcionRespuestaDTO(
        int idOpcionRespuesta,
        String nombre,
        String descripcion,
        boolean activo,
        List<ItemOpcionRespuestaDTO> items
) {
}
