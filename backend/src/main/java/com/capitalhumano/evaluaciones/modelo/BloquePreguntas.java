package com.capitalhumano.evaluaciones.modelo;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BloquePreguntas {
    private int idBloque;
    private int nroBloque;
    private boolean completado;
    private List<PreguntaCuestionario> preguntas = new ArrayList<>();
}
