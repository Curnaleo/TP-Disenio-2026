package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Integer> {
	default List<OpcionRespuesta> buscarOpciones(String nombre) {
		return findByNombreContainingIgnoreCase(nombre);
	}


	// OpcionRespuesta findById(Integer id);
	// void guardar(OpcionRespuestaDTO dto);
	// void actualizar(Integer id, OpcionRespuestaDTO dto);
	// void eliminarLogico(Integer id);
	// boolean validarEnUso(Integer id);


	@EntityGraph(attributePaths = "items")
	List<OpcionRespuesta> findByNombreContainingIgnoreCase(String nombre);

	// boolean existsByNombreIgnoreCase(String nombre);
	// Optional<OpcionRespuesta> findByNombreIgnoreCase(String nombre);
	// List<OpcionRespuesta> findByActivoTrue();
}
