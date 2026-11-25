package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Integer> {
	default List<OpcionRespuesta> buscarOpciones(String nombre) {
		return findByNombreContainingIgnoreCase(nombre);
	}

	@EntityGraph(attributePaths = "items")
	List<OpcionRespuesta> findByNombreContainingIgnoreCase(String nombre);

	default void guardar(OpcionRespuesta opcion) {
		save(opcion);
	}

	default void actualizar(OpcionRespuesta opcion) {
		save(opcion);
	}

	@Transactional
	@Modifying
	@Query("update OpcionRespuesta o set o.activo = false where o.id = :id")
	void eliminarLogico(@Param("id") Integer id);

	@Query("""
		select case when count(p) > 0 then true else false end
		from Pregunta p
		where p.opcionRespuesta.id = :id
		""")
	boolean validarEnUso(@Param("id") Integer id);
}
