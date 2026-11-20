package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.OpcionRespuestaDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorOpcionesRespuesta {

    public abstract List<OpcionRespuestaDTO> buscarOpciones(String nombre);
}
