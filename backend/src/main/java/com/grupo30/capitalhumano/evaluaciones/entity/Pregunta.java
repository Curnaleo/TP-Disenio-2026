package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity 
@Table (name = "pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Pregunta {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String textoPregunta;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_factor", 
    	referencedColumnName = "codigo", 
	nullable = false)
    private Factor factor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_opcion_respuesta", nullable = false)
    private OpcionRespuesta opcionRespuesta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OpcionPregunta> opcionesPregunta = new ArrayList<>();
}
