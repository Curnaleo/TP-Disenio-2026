package com.grupo30.capitalhumano.evaluaciones.dao;

import java.util.Optional;

import com.grupo30.capitalhumano.evaluaciones.entity.ConfiguracionSistema;

public interface IConfiguracionSistemaDAO {

	Optional<ConfiguracionSistema> obtener(String clave);
}
