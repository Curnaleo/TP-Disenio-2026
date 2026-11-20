package com.capitalhumano.evaluaciones.dto;

import com.capitalhumano.evaluaciones.modelo.TipoDocumento;

public record CandidatoGestionDTO(
        int nroCandidato,
        String apellido,
        String nombre,
        TipoDocumento tipoDocumento,
        String nroDocumento,
        String email
) {
}
