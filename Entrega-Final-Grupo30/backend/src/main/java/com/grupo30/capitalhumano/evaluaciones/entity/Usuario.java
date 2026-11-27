package com.grupo30.capitalhumano.evaluaciones.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Table(name = "usuario")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
@Builder 

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, unique = true, length = 15)
    private String nombre;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
