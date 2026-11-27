package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FactorDTO {
    
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer nroOrden;
    private Boolean activo;

    private String nombreCompetencia;
    private Integer idCompetencia;
}
