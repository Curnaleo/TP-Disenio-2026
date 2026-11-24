package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;
import java.util.List;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PreguntaDTO {
    private Integer idPregunta;
    private String nombre;
    private String textoPregunta;
    private String descripcion;
    private Boolean activo;
    private String nombreCompetencia;
    private Integer idCompetencia;
    private String nombreFactor;
    private Integer idFactor;
    private Integer idOpcionRespuesta;
    private String nombreOpcionRespuesta;
    private List<OpcionPreguntaDTO> opcionesPregunta;
}
