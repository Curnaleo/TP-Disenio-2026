package com.grupo30.capitalhumano.evaluaciones.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String codigoCompetencia;
    private String nombreFactor;
    private Integer idFactor; // codigoFactor
    private Integer idOpcionRespuesta;
    private String nombreOpcionRespuesta;
    private List<OpcionPreguntaDTO> opcionesPregunta;
}
