package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CompetenciaDTO {
    
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
