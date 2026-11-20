package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Competencia;

public interface ICompetenciaDAO {

    List<Competencia> buscar(String nombre);

    Competencia buscarPorCodigo(String codigoCompetencia);

    void guardar(Competencia comp);

    void actualizar(Competencia comp);

    void eliminarLogico(String codigoCompetencia);

    boolean validarEnUso(String codigoCompetencia);
}
