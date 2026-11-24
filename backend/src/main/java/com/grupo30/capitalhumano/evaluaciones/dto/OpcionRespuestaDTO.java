package com.grupo30.capitalhumano.evaluaciones.dto;

import java.util.List;
import lombok.*;

@AllArgsConstructor

public class OpcionRespuestaDTO{

	private Integer id;

	private String nombre;

	private String descripcion;

	private Boolean activo;

	private List<ItemOpcionRespuestaDTO> items;

}
