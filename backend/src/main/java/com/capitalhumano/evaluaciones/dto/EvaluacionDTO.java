package com.capitalhumano.evaluaciones.dto;

import java.time.LocalDate;

public record EvaluacionDTO(
        int idEvaluacion,
        String nombrePuesto,
        LocalDate fechaCreacion,
        int cantidadCandidatos
) {
}
