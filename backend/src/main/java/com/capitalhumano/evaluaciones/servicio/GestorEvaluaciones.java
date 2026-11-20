package com.capitalhumano.evaluaciones.servicio;

import java.util.List;
import com.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;

/** Contrato del diagrama para implementar al desarrollar el caso de uso. */
public abstract class GestorEvaluaciones {

    public abstract List<CandidatoGestionDTO> buscarCandidatos(String apellido, String nombre, int nro);

    public abstract List<CandidatoGestionDTO> validarCandidatosActivos(List<CandidatoGestionDTO> candidatos);

    public abstract void generarEvaluacion(String codigoPuesto, List<CandidatoGestionDTO> candidatos);

    public abstract void exportarClavesExcel(int idEvaluacion, String carpeta);
}
