package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "puesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Puesto {

	@Id
	@Column(nullable = false, length = 20)
	private String codigo;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(nullable = false, length = 255)
	private String descripcion;

	@Column(nullable = false, length = 100)
	private String empresa;

	@Column(nullable = false)
	private Boolean activo = true;

	@OneToMany(
		mappedBy = "puesto",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<CaracteristicaPuesto> caracteristicas =
		new ArrayList<>();

}
