package com.grupo30.capitalhumano.evaluaciones.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor

@Getter 
@Setter
@NoArgsConstructor
@Builder

public class OpcionRespuestaDTO{

	private Integer id;
	private String nombre;
	private String descripcion;
	private Boolean activo;
	private List<ItemOpcionRespuestaDTO> items;

}
