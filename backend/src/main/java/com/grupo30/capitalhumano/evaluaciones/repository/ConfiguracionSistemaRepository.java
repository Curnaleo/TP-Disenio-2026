package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IConfiguracionSistemaDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.ConfiguracionSistema;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConfiguracionSistemaRepository implements IConfiguracionSistemaDAO {

	private final EntityManager entityManager;

	@Override
	public Optional<ConfiguracionSistema> obtener(String clave) {

		return Optional.ofNullable(
			entityManager.find(
				ConfiguracionSistema.class,
					clave));
	}
}
