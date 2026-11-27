package com.grupo30.capitalhumano.evaluaciones.autenticacion.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

// Request de autenticación para Consultor (CU001 - Flujo Principal).

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoginConsultorRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 15, message = "El identificador de usuario no puede superar los 15 caracteres.")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 20, message = "La contraseña no puede superar los 20 caracteres.")
    private String contrasena;

}