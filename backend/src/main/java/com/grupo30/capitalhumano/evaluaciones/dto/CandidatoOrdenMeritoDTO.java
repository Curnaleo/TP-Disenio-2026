package com.grupo30.capitalhumano.evaluaciones.dto;

import java.time.LocalDateTime;

import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

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
public class CandidatoOrdenMeritoDTO {

	private TipoDocumento tipoDocumento;
	private String nroDocumento;
	private String apellido;
	private String nombre;
	private Double puntajeObtenido; // null si el estado no es COMPLETO
	private EstadoCuestionario estadoCuestionario;
	private Boolean cumplePonderacionMinima;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private LocalDateTime fechaHoraUltimoIngreso;
	private Integer cantidadAccesos;
}
