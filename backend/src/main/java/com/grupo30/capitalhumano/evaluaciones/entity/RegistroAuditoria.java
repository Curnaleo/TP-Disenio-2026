package com.grupo30.capitalhumano.evaluaciones.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "registro_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RegistroAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro")
    private Integer id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 100)
    private String usuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(nullable = false, length = 100)
    private String entidad;
    
    @Column(name = "id_entidad", nullable = false)
    private Integer idEntidad;

}
