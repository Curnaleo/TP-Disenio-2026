package com.capitalhumano.evaluaciones.modelo;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Candidato {
    private int nroCandidato;
    private String apellido;
    private String nombre;
    private String nroDocumento;
    private TipoDocumento tipoDocumento;
    private LocalDate fechaNacimiento;
    private Genero genero;
    private String nacionalidad;
    private String email;
    private String escolaridad;
    private boolean eliminado;
}
