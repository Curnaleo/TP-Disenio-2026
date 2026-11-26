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
public class PuestoDTO {

	private Integer puestoId;
	private String codigo;
	private String nombre;
	private String descripcion;
	private String empresa;
	private Boolean activo;
	private List<CaracteristicaPuestoDTO> caracteristicas;
	private Integer cantidadCandidatos;
	private Integer evaluacionesCompletas;
}
