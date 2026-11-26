package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resultado_competencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadoCompetencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_resultado_competencia")
	private Integer idResultadoCompetencia;

	@Column(name = "puntaje_obtenido", nullable = false)
	private Double puntajeObtenido;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cuestionario", nullable = false)
	private Cuestionario cuestionario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "codigo_competencia",
		referencedColumnName = "codigo",
		nullable = false
	)
	private Competencia competencia;
}
