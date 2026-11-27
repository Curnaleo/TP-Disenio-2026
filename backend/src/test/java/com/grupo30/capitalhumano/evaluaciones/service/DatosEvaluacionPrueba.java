package com.grupo30.capitalhumano.evaluaciones.service;

import com.grupo30.capitalhumano.evaluaciones.dao.IPuestoDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.Puesto;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import jakarta.persistence.EntityManager;

final class DatosEvaluacionPrueba {

    private DatosEvaluacionPrueba() {
    }

    static Candidato crearCandidato(EntityManager entityManager, int numero) {
        Candidato candidato = Candidato.builder().nroCandidato(numero)
                .apellido("ALVAREZ").nombre("ANA")
                .tipoDocumento(TipoDocumento.DNI).nroDocumento(Integer.toString(numero))
                .build();
        entityManager.persist(candidato);
        return candidato;
    }

    static Puesto buscarPuestoEvaluable(IPuestoDAO puestos) {
        return puestos.buscar("P001", null, null).stream()
                .filter(puesto -> "P001".equals(puesto.getCodigo()))
                .findFirst().orElseThrow(() -> new AssertionError("Falta el puesto de prueba P001"));
    }
}
