package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.PuestoDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorPuestos {

    public abstract List<PuestoDTO> buscarPuestos(String codigo, String nombre, String empresa);
}
