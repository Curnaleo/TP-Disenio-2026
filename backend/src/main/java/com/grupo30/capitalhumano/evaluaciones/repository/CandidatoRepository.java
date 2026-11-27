package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class CandidatoRepository implements ICandidatoDAO {

	private final EntityManager entityManager;

	@Override
	public List<Candidato> buscar(
			    String apellido,
			        String nombre,
				    Integer nroCandidato) {

		    StringBuilder jpql = new StringBuilder(
				            """
					            SELECT c
						            FROM Candidato c
							            WHERE c.eliminado = false
								            """
									        );

		        if (apellido != null && !apellido.isBlank()) {
				        jpql.append(
							            " AND UPPER(c.apellido) LIKE UPPER(:apellido)"
								            );
					    }

			    if (nombre != null && !nombre.isBlank()) {
				            jpql.append(
							                " AND UPPER(c.nombre) LIKE UPPER(:nombre)"
									        );
					        }

			        if (nroCandidato != null) {
					        jpql.append(
								            " AND c.nroCandidato = :nroCandidato"
									            );
						    }

				    jpql.append(
						            " ORDER BY c.apellido, c.nombre"
							        );

				        var query = entityManager.createQuery(
							        jpql.toString(),
								        Candidato.class
									    );

					    if (apellido != null && !apellido.isBlank()) {
						            query.setParameter(
									                "apellido",
											            "%" + apellido.trim() + "%"
												            );
							        }

					        if (nombre != null && !nombre.isBlank()) {
							        query.setParameter(
										            "nombre",
											                "%" + nombre.trim() + "%"
													        );
								    }

						    if (nroCandidato != null) {
							            query.setParameter(
										                "nroCandidato",
												            nroCandidato
													            );
								        }

						        return query.getResultList();
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
