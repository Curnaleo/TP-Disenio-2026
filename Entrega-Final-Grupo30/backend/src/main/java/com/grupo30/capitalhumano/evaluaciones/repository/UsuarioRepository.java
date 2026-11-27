package com.grupo30.capitalhumano.evaluaciones.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.IUsuarioDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsuarioRepository implements IUsuarioDAO {

	private final EntityManager entityManager;

	@Override
	public Optional<Usuario> buscarPorUser(String user) {

		List<Usuario> resultados =
			entityManager.createQuery(
				"SELECT u " +
				"FROM Usuario u " +
				"WHERE UPPER(u.nombre) = UPPER(:user) " +
				"AND u.activo = true",
				Usuario.class)
			.setParameter("user", user)
			.getResultList();

		return resultados.isEmpty()
			? Optional.empty()
			: Optional.of(resultados.getFirst());
	}

	@Override
	public Optional<Usuario> findById(Integer id) {

		Usuario usuario =
			entityManager.find(Usuario.class, id);

		return Optional.ofNullable(usuario);
	}
}
