package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table (name = "opcion_respuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OpcionRespuesta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_opcion_respuesta")
	private Integer id;

	@Column(nullable = false, unique = true, length = 100)
	private String nombre;

	@Column(nullable = false, length = 255)
	private String descripcion;

	@Column(nullable = false)
	private Boolean activo = true;

	@OneToMany(
		mappedBy = "opcionRespuesta",
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	private List<ItemOpcionRespuesta> items = new ArrayList<>();
}
