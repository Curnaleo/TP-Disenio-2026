package com.grupo30.capitalhumano.evaluaciones.entity;

import lombok.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity 
@Table(name = "cuestionario")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder

public class Cuestionario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cuestionario")
    private Integer idCuestionario;

    @Column(nullable = false, length = 100)
    private String clave;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuestionario estado;

    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "fecha_hora_ultimo_ingreso")
    private LocalDateTime fechaHoraUltimoIngreso;

    @Column(name = "cantidad_accesos", nullable = false)
    @Builder.Default
    private Integer cantidadAccesos = 0;

    @Column(name = "maximo_accesos")
    private Integer maximoAccesos;

    @Column(name = "tiempo_maximo")
    private Integer tiempoMaximo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_candidato", nullable = false)
    private Candidato candidato;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
	        name = "id_evaluacion",
		        nullable = false
		   )
	private Evaluacion evaluacion;
}
