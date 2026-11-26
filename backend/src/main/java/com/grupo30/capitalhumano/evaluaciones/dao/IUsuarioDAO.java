package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;

public interface IUsuarioDAO {

	    Optional<Usuario> buscarPorUser(String user);

	        Optional<Usuario> findById(Integer id);
}
