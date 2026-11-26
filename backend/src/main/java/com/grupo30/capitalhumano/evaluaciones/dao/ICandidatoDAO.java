package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

public interface ICandidatoDAO {

	List<Candidato> buscar(String apellido, String nombre, Integer nroCandidato);

	Optional<Candidato> buscarPorDocumento(TipoDocumento tipo,String nroDocumento);

	Optional<Candidato> findById(Integer nroCandidato);

	void guardar(Candidato candidato);

	void actualizar(Candidato candidato);

	void eliminarLogico(Integer nroCandidato);
}
