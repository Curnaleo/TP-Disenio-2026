package com.grupo30.capitalhumano.evaluaciones.dao;

import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;

import java.util.List;
import java.util.Optional;

public interface IPuestoDAO {

	List<Puesto> buscar(
		String codigo,
		String nombre,
		String empresa
	);

	Puesto findById(int puestoId);
	
	void guardar(Puesto puesto);

	void actualizar(Puesto puesto);

	void eliminarLogico(int puestoId);

	boolean validarEnUso(int puestoId);
}
