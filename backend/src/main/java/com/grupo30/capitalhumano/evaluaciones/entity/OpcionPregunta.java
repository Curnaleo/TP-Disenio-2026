package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table (name = "opcion_pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OpcionPregunta {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion_pregunta")
    private Integer id;

    @Column(nullable = false)
    private Double ponderacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_item_opcion_respuesta", nullable = false)
    private ItemOpcionRespuesta itemOpcionRespuesta;
}
