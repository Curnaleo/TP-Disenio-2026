package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio para Usuario (corresponde a IUsuarioDAO del diagrama de clases).


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT u FROM Usuario u WHERE UPPER(u.nombre) = UPPER(:user)")
    Optional<Usuario> buscarPorUser(@Param("user") String user);
}