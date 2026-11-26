package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pregunta_cuestionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreguntaCuestionario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_preg_cuest")
	private Integer idPregCuest;

	@Column(name = "nro_orden", nullable = false)
	private Integer nroOrden;

	@Column(nullable = false)
	@Builder.Default
	private Boolean respondida = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_bloque",
		nullable = false)
	private BloquePreguntas bloque;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_pregunta",
		nullable = false)
	private Pregunta pregunta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_opcion_pregunta")
	private OpcionPregunta opcionElegida;
}
