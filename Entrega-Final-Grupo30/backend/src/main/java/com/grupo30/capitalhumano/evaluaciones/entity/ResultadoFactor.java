package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resultado_factor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoFactor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_resultado_factor")
	private Integer idResultadoFactor;

	@Column(name = "puntaje_obtenido",
		nullable = false)
	private Double puntajeObtenido;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cuestionario",
		nullable = false)
	private Cuestionario cuestionario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigo_factor",
		referencedColumnName = "codigo",
		nullable = false)
	private Factor factor;
}
