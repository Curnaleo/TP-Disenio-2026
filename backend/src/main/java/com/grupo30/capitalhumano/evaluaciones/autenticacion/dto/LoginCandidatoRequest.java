package com.grupo30.capitalhumano.evaluaciones.autenticacion.dto;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//Request de autenticación para Candidato (CU001 - Flujo Alternativo 3.A).

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginCandidatoRequest {
    @NotNull(message = "El tipo de documento es obligatorio.")
    private TipoDocumento tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio.")
    private String nroDocumento;
    
    @NotBlank(message = "La clave es obligatoria.")
    private String clave;
}