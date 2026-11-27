package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioDTO {
    
    private Integer idUsuario;
    private String nombre;
    private Boolean activo;
}