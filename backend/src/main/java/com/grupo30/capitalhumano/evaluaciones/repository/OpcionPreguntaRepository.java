package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IOpcionPreguntaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionPregunta;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OpcionPreguntaRepository
        implements IOpcionPreguntaDAO {

	private final EntityManager entityManager;

	@Override
	public Optional<OpcionPregunta> findById(
		Integer id) {

		return Optional.ofNullable(
			entityManager.find(
				OpcionPregunta.class,
				id));
	}
}
