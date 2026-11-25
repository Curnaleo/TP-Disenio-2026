package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;

public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Integer> {
	List<OpcionRespuesta> buscarOpciones(String nombre);
	OpcionRespuesta findByID(Integer id); 
	void guardar(OpcionRespuesta op);
	void actualizar(OpcionRespuesta op);
	void eliminarLogico(Integer id);
	boolean validarEnUso(Integer id);
}
