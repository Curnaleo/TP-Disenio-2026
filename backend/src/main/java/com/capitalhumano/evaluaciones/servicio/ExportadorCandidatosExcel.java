package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Candidato;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class ExportadorCandidatosExcel {

    public abstract void exportar(List<Candidato> candidatos, String carpeta);
}
