package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class CuestionarioRepository implements ICuestionarioDAO {

	private final EntityManager entityManager;

	@Override
	public Optional<Cuestionario> buscarActivoPorCandidato(Integer idCandidato) {

		List<Cuestionario> resultados =
			entityManager.createQuery("""
				SELECT q
				FROM Cuestionario q
				WHERE q.candidato.nroCandidato = :idCandidato
				AND q.estado IN (:activo, :enProceso)
				""", Cuestionario.class)
			.setParameter("idCandidato", idCandidato)
			.setParameter("activo", EstadoCuestionario.ACTIVO)
			.setParameter("enProceso", EstadoCuestionario.EN_PROCESO)
			.getResultList();

		return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public Optional<Cuestionario> buscarPorClaveYDoc(TipoDocumento tipo, String nro, String clave) {

		List<Cuestionario> resultados =
			entityManager.createQuery("""
				SELECT q
				FROM Cuestionario q
				JOIN FETCH q.candidato c
				WHERE c.tipoDocumento = :tipo
				AND c.nroDocumento = :nro
				AND c.eliminado = false
				AND q.clave = :clave
				AND q.estado IN (:activo, :enProceso)
				""", Cuestionario.class)
			.setParameter("tipo", tipo)
			.setParameter("nro", nro)
			.setParameter("clave", clave)
			.setParameter("activo", EstadoCuestionario.ACTIVO)
			.setParameter("enProceso", EstadoCuestionario.EN_PROCESO)
			.getResultList();

		return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public Optional<Cuestionario> findById(Integer id) {

		Cuestionario cuestionario =
			entityManager.find(Cuestionario.class, id);

		return Optional.ofNullable(cuestionario);
	}

	@Override
	public void guardar(Cuestionario cuestionario) {
		entityManager.persist(cuestionario);
	}

	@Override
	public void actualizar(Cuestionario cuestionario) {
		entityManager.merge(cuestionario);
	}

	@Override
	public List<Cuestionario> listarVencidos() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Cuestionario> listarPorPuestoYEvaluacion(Integer idPuesto, Integer idEvaluacion) {

		throw new UnsupportedOperationException();
	}
}
