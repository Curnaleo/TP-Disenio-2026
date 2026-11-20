package com.capitalhumano.evaluaciones.servicio;

import com.capitalhumano.evaluaciones.dto.CompetenciaDTO;
import com.capitalhumano.evaluaciones.dto.FactorDTO;
import com.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;
import com.capitalhumano.evaluaciones.dto.PreguntaDTO;

public interface IServicioIA {

    PreguntaDTO generarPregunta(CompetenciaDTO comp, FactorDTO factor, OpcionRespuestaDTO opciones);
}
