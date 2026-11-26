package com.grupo30.capitalhumano.evaluaciones.dto;

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
public class ResultadoComparativoDTO {

	private String competencia;
	private Double puntajeRequerido;
	private Double puntajeObtenido;
}
