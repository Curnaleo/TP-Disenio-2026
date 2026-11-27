package com.grupo30.capitalhumano.evaluaciones.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IRegistroAuditoriaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.RegistroAuditoria;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class RegistroAuditoriaRepository implements IRegistroAuditoriaDAO {

	private final EntityManager entityManager;

	@Override
	public void guardar(RegistroAuditoria registro) {
		entityManager.persist(registro);
	}
}
