package com.grupo30.capitalhumano.evaluaciones.repository;

import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio para Cuestionario (corresponde a ICuestionarioDAO del diagrama de clases).

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Integer> {
    @Query("""
            SELECT q FROM Cuestionario q
            WHERE q.candidato.nroCandidato = :idCandidato
              AND q.estado IN (
                  com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario.ACTIVO,
                  com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario.EN_PROCESO
              )
            """)
    Optional<Cuestionario> buscarActivoPorCandidato(@Param("idCandidato") Integer idCandidato);
    @Query("""
            SELECT q FROM Cuestionario q
            JOIN FETCH q.candidato c
            WHERE c.tipoDocumento = :tipo
              AND c.nroDocumento = :nro
              AND c.eliminado = false
              AND q.clave = :clave
              AND q.estado IN (
                  com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario.ACTIVO,
                  com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario.EN_PROCESO
              )
            """)
    Optional<Cuestionario> buscarPorClaveYDoc(
            @Param("tipo") TipoDocumento tipo,
            @Param("nro") String nro,
            @Param("clave") String clave);
}