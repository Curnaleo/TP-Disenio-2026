package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.CompetenciaDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorCompetencias {

    public abstract List<CompetenciaDTO> buscarCompetencias(String nombre);
}
