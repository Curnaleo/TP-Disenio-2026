package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.CompetenciaDTO;
import com.capitalhumano.evaluaciones.dto.FactorDTO;
import com.capitalhumano.evaluaciones.dto.PreguntaDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorPreguntas {

    public abstract List<PreguntaDTO> buscarPreguntas(String comp, String factor, String nombre);

    public abstract void altaPregunta(PreguntaDTO pregunta);

    public abstract void modificarPregunta(PreguntaDTO pregunta);

    public abstract void bajaPregunta(int id);

    public abstract PreguntaDTO generarConIA(CompetenciaDTO comp, FactorDTO factor);
}
