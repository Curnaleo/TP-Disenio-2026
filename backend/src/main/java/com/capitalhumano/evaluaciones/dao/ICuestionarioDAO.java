package com.capitalhumano.evaluaciones.dao;

import java.util.List;
import com.capitalhumano.evaluaciones.modelo.Cuestionario;
import com.capitalhumano.evaluaciones.modelo.TipoDocumento;

public interface ICuestionarioDAO {

    void guardar(Cuestionario cuestionario);

    void actualizar(Cuestionario cuestionario);

    Cuestionario findById(int id);

    Cuestionario buscarActivoPorCandidato(int idCandidato);

    Cuestionario buscarPorClaveYDoc(TipoDocumento tipo, String nro, String clave);

    List<Cuestionario> listarVencidos();

    List<Cuestionario> listarPorPuestoYEvaluacion(String codigoPuesto, int idEvaluacion);
}
