package com.grupo30.capitalhumano.evaluaciones.repository;
import com.grupo30.capitalhumano.evaluaciones.entity.Candidato;
import com.grupo30.capitalhumano.evaluaciones.entity.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repositorio para Candidato (corresponde a ICandidatoDAO del diagrama de clases).

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {
    @Query("SELECT c FROM Candidato c WHERE c.tipoDocumento = :tipo AND c.nroDocumento = :nro AND c.eliminado = false")
    Optional<Candidato> buscarPorDocumento(@Param("tipo") TipoDocumento tipo, @Param("nro") String nro);
}
