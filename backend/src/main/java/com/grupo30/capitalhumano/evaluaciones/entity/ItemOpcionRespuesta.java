package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_opcion_respuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOpcionRespuesta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item")
	private Integer id;

	@Column(nullable = false)
	private Integer orden;

	@Column(nullable = false, length = 100)
	private String descripcion;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "id_opcion_respuesta",
		nullable = false
	)
	private OpcionRespuesta opcionRespuesta;
}
