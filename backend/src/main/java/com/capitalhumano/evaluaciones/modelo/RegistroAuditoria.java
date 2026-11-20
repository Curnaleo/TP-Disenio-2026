package com.capitalhumano.evaluaciones.modelo;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroAuditoria {
    private int idRegistro;
    private LocalDateTime fechaHora;
    private String usuario;
    private String accion;
    private String entidad;
    private int idEntidad;
}
