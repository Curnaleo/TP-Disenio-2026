package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Integer> {
	default List<OpcionRespuesta> buscarOpciones(String nombre) {
		return findByNombreContainingIgnoreCase(nombre);
	}

	OpcionRespuesta findByID(Integer id); // Con la ID en mayuscula sino choca con libreria!
	void guardar(OpcionRespuesta op);
	void actualizar(OpcionRespuesta op);
	void eliminarLogico(Integer id);
	boolean validarEnUso(Integer id);


	@EntityGraph(attributePaths = "items")
	List<OpcionRespuesta> findByNombreContainingIgnoreCase(String nombre);

	// boolean existsByNombreIgnoreCase(String nombre);
	// Optional<OpcionRespuesta> findByNombreIgnoreCase(String nombre);
	// List<OpcionRespuesta> findByActivoTrue();
}
