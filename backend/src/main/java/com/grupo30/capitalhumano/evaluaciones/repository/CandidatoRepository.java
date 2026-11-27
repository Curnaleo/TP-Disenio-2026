package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class CandidatoRepository implements ICandidatoDAO {

	private final EntityManager entityManager;

	@Override
	public List<Candidato> buscar(String apellido, String nombre, Integer nroCandidato) { // GRANDE CHAT
		String porNumero = nroCandidato == null ? "" : "AND c.nroCandidato = :nroCandidato ";
		TypedQuery<Candidato> consulta = entityManager.createQuery(
			"SELECT c FROM Candidato c WHERE c.eliminado = false "
				+ "AND LOCATE(LOWER(:apellido), LOWER(c.apellido)) > 0 "
				+ "AND LOCATE(LOWER(:nombre), LOWER(c.nombre)) > 0 "
				+ porNumero + "ORDER BY c.apellido, c.nombre, c.nroCandidato",
			Candidato.class);
		consulta.setParameter("apellido", apellido == null ? "" : apellido.trim());
		consulta.setParameter("nombre", nombre == null ? "" : nombre.trim());
		if (nroCandidato != null) {
			consulta.setParameter("nroCandidato", nroCandidato);
		}
		return consulta.getResultList();
	}

	@Override
	public Optional<Candidato> buscarPorDocumento(TipoDocumento tipo, String nroDocumento) {

	List<Candidato> resultados =
		entityManager.createQuery(
			"SELECT c " +
			"FROM Candidato c " +
			"WHERE c.tipoDocumento = :tipo " +
			"AND c.nroDocumento = :nro " +
			"AND c.eliminado = false",
			Candidato.class)
		.setParameter("tipo", tipo)
		.setParameter("nro", nroDocumento)
		.getResultList();

	         return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public Optional<Candidato> findById(Integer nroCandidato) {

		Candidato candidato =
			entityManager.find(
				Candidato.class,
				nroCandidato);

		return Optional.ofNullable(candidato);
	}

	@Override
	public void guardar(Candidato candidato) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void actualizar(Candidato candidato) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void eliminarLogico(Integer nroCandidato) {
		throw new UnsupportedOperationException();
	}

}
