package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "competencia")
@Getter
@Setter 
@NoArgsConstructor 
@AllArgsConstructor


public class Competencia {

    @Id 
    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany (mappedBy = "competencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factor> factores = new ArrayList<>();
}
