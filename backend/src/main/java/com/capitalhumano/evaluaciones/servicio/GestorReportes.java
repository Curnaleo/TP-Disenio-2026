package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.CandidatoOrdenMeritoDTO;
import com.capitalhumano.evaluaciones.dto.EvaluacionDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorReportes {

    public abstract List<EvaluacionDTO> listarEvaluaciones(String codigoPuesto);

    public abstract List<CandidatoOrdenMeritoDTO> generarOrdenMerito(String codigoPuesto, List<Integer> idEvaluaciones);
}
