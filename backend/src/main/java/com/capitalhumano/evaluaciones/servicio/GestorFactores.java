package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.FactorDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorFactores {

    public abstract List<FactorDTO> buscarFactores(String comp, String nombre);
}
