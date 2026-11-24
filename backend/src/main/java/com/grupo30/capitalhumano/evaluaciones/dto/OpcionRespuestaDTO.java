package com.grupo30.capitalhumano.evaluaciones.dto;

import lombok.*;
import java.util.List;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class OpcionRespuestaDTO{

	private Integer id;
	private String nombre;
	private String descripcion;
	private Boolean activo;
	private List<ItemOpcionRespuestaDTO> items;

}
