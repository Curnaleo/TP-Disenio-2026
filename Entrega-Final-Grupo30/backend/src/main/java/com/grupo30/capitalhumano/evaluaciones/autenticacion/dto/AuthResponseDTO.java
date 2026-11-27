package com.grupo30.capitalhumano.evaluaciones.autenticacion.dto;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoCuestionarioDTO;
import com.grupo30.capitalhumano.evaluaciones.dto.UsuarioDTO;
import lombok.*;

//Respuesta unificada de autenticación con token JWT y datos del actor autenticado.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthResponseDTO {
    private String token;
    private String tipoToken;
    private String rol;
    private UsuarioDTO consultor;
    private CandidatoCuestionarioDTO candidato;

}
