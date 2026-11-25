package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.grupo30.capitalhumano.evaluaciones.entity.OpcionRespuesta;


public interface IOpcionRespuestaDAO extends JpaRepository<OpcionRespuesta, Integer> {
	
	List<OpcionRespuesta> buscarOpciones(String nombre);

    Optional<OpcionRespuesta> findById(Integer id);

    void guardar(OpcionRespuesta opcion);

    void actualizar(OpcionRespuesta opcion);

    void eliminarLogico(Integer id);

    boolean validarEnUso(Integer id);
	
}
