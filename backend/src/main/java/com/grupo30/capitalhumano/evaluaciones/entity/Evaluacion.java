package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_evaluacion")
	private Integer id;

	@Column(name = "nombre_evaluacion",
		nullable = false,
		length = 100)
	private String nombreEvaluacion;

	@Column(nullable = false,
		length = 255)
	private String descripcion;

	@Column(name = "fecha_creacion",
		nullable = false)
	private LocalDate fechaCreacion;

	@Column(nullable = false)
	private Boolean activo = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_puesto",
		nullable = false
	)
	private Puesto puesto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_usuario",
		referencedColumnName = "id_usuario",
		nullable = false
	)
	private Usuario usuario;

	@OneToMany(
		mappedBy = "evaluacion",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<Cuestionario> cuestionarios =
		new ArrayList<>();
}
