package com.capitalhumano.evaluaciones.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evaluacion {
    private int idEvaluacion;
    private LocalDate fechaCreacion;
    private Puesto puesto;
    private Usuario usuario;
    private List<Cuestionario> cuestionarios = new ArrayList<>();
}
