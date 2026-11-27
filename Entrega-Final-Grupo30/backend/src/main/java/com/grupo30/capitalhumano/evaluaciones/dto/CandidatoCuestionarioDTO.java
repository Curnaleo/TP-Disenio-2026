package com.grupo30.capitalhumano.evaluaciones.dto;

import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CandidatoCuestionarioDTO {
    
    private Integer idCuestionario;
    private Integer nroCandidato;
    private String apellido;
    private String nombre;
    private EstadoCuestionario estado;
}
