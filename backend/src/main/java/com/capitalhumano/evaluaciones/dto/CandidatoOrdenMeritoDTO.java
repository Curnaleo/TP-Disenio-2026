package com.capitalhumano.evaluaciones.dto;

import java.time.LocalDateTime;
import com.capitalhumano.evaluaciones.modelo.EstadoCuestionario;
import com.capitalhumano.evaluaciones.modelo.TipoDocumento;

public record CandidatoOrdenMeritoDTO(
        TipoDocumento tipoDocumento,
        String nroDocumento,
        String apellido,
        String nombre,
        double puntajeObtenido,
        EstadoCuestionario estadoCuestionario,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        LocalDateTime fechaHoraUltimoIngreso,
        int cantidadAccesos
) {
}
