package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

public interface ICuestionarioDAO {

	void guardar(Cuestionario cuestionario);

	void actualizar(Cuestionario cuestionario);

	Optional<Cuestionario> findById(Integer id);

	Optional<Cuestionario> buscarActivoPorCandidato(Integer idCandidato);

	Optional<Cuestionario> buscarPorClaveYDoc(
		TipoDocumento tipo,
		String nroDocumento,
		String clave);

	List<Cuestionario> listarVencidos();

	List<Cuestionario> listarPorPuestoYEvaluacion(
			Integer idPuesto,
			Integer idEvaluacion);
}
