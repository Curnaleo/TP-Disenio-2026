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
@Table(name = "bloque_preguntas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloquePreguntas {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_bloque")
	private Integer idBloque;

	@Column(name = "nro_bloque", nullable = false)
	private Integer nroBloque;

	@Column(nullable = false)
	@Builder.Default
	private Boolean completado = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_cuestionario",
		nullable = false)
	private Cuestionario cuestionario;
}
