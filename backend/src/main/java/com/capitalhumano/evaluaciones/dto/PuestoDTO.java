package com.capitalhumano.evaluaciones.dto;

import java.util.List;

public record PuestoDTO(
        String codigo,
        String nombre,
        String descripcion,
        String empresa,
        boolean activo,
        List<CaracteristicaPuestoDTO> caracteristicas
) {
}
