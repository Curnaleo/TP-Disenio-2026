package com.capitalhumano.evaluaciones.dao;

import com.capitalhumano.evaluaciones.modelo.ConfiguracionSistema;

public interface IConfiguracionSistemaDAO {

    ConfiguracionSistema obtener(String clave);
}
