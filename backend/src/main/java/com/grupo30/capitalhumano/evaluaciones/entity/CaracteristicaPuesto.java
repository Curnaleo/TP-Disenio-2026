package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "caracteristica_puesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaracteristicaPuesto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_caracteristica")
	private Integer id;

	@Column(name = "ponderacion_minima", nullable = false)
	private Double ponderacionMinima;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "codigo_puesto",
		referencedColumnName = "codigo",
		nullable = false
		)
	private Puesto puesto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "codigo_competencia",
		referencedColumnName = "codigo",
		nullable = false
		)
	private Competencia competencia;
}
