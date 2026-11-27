package com.grupo30.capitalhumano.evaluaciones.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity 
@Table(name = "candidato")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
@Builder 

public class Candidato {
    @Id
    @Column(name = "nro_candidato")
    private Integer nroCandidato;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "nro_documento", nullable = false, length = 30)
    private String nroDocumento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false, length = 10)
    private TipoDocumento tipoDocumento;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private Genero genero;

    @Column(length = 80)
    private String nacionalidad;

    @Column(length = 150)
    private String email;

    @Column(length = 100)
    private String escolaridad;

    @Column(nullable = false)
    @Builder.Default
    private Boolean eliminado = false;
    
    @OneToMany(mappedBy = "candidato", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cuestionario> cuestionarios = new ArrayList<>();

}
