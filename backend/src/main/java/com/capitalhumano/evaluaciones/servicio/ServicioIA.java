package com.capitalhumano.evaluaciones.servicio;

import com.capitalhumano.evaluaciones.dto.CompetenciaDTO;
import com.capitalhumano.evaluaciones.dto.FactorDTO;
import com.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.capitalhumano.evaluaciones.dto.PreguntaDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class ServicioIA implements IServicioIA {

    @Override
    public abstract PreguntaDTO generarPregunta(CompetenciaDTO comp, FactorDTO factor, OpcionRespuestaDTO opciones);
}
