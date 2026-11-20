package com.capitalhumano.evaluaciones.dto;

public record FactorDTO(
        String codigo,
        String nombre,
        String descripcion,
        int nroOrden,
        boolean activo,
        String nombreCompetencia
) {
}
