package com.grupo30.capitalhumano.evaluaciones.dto;

import java.time.LocalDate;

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
public class EvaluacionDTO {

	private Integer idEvaluacion;
	private String nombreEvaluacion;
	private String nombrePuesto;
	private LocalDate fechaCreacion;
	private Integer cantidadCandidatos;
}
