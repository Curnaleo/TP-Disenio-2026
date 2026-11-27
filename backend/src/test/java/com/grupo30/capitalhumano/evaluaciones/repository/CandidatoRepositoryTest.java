package com.grupo30.capitalhumano.evaluaciones.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.grupo30.capitalhumano.evaluaciones.dao.ICandidatoDAO;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
class CandidatoRepositoryTest {

    @Autowired private ICandidatoDAO candidatos;
    @Autowired private EntityManager entityManager;

    @Test
    void cu025_combinaFiltrosParcialesSinDistinguirMayusculasYExcluyeBajas() {
        agregar(990010, "PRUEBA TORRES", "ANA", false);
        agregar(990011, "PRUEBA TORRES", "DIANA", false);
        agregar(990012, "PRUEBA TORRES", "MARIO", false);
        agregar(990013, "PRUEBA TORRES", "ANA", true);
        agregar(990014, "PRUEBA VIDAL", "ANA", false);
        entityManager.flush();
        entityManager.clear();

        assertThat(candidatos.buscar(" prueba torres ", "ana", null))
                .extracting(Candidato::getNroCandidato).containsExactly(990010, 990011);
        assertThat(candidatos.buscar(" prueba torres ", "ana", 990011))
                .extracting(Candidato::getNroCandidato).containsExactly(990011);
        assertThat(candidatos.buscar(" prueba torres ", "ana", 990013)).isEmpty();
    }

    private void agregar(int numero, String apellido, String nombre, boolean eliminado) {
        entityManager.persist(Candidato.builder().nroCandidato(numero).apellido(apellido).nombre(nombre)
                .nroDocumento(Integer.toString(numero)).tipoDocumento(TipoDocumento.DNI).eliminado(eliminado)
                .build());
    }
}
