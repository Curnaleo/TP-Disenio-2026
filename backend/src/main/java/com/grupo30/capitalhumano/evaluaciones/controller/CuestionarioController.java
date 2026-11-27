package com.grupo30.capitalhumano.evaluaciones.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grupo30.capitalhumano.evaluaciones.autenticacion.exception.AutenticacionException;
import com.grupo30.capitalhumano.evaluaciones.dao.IBloquePreguntasDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.IConfiguracionSistemaDAO;
import com.grupo30.capitalhumano.evaluaciones.dao.ICuestionarioDAO;
import com.grupo30.capitalhumano.evaluaciones.dto.RespuestaCandidatoDTO;
import com.grupo30.capitalhumano.evaluaciones.entity.BloquePreguntas;
import com.grupo30.capitalhumano.evaluaciones.entity.Cuestionario;
import com.grupo30.capitalhumano.evaluaciones.entity.EstadoCuestionario;
import com.grupo30.capitalhumano.evaluaciones.exception.PlazoInicioVencidoException;
import com.grupo30.capitalhumano.evaluaciones.service.GestorCuestionario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cuestionario")
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class CuestionarioController {
    private final GestorCuestionario gestorCuestionario;
    private final ICuestionarioDAO cuestionarioDAO;
    private final IBloquePreguntasDAO bloquePreguntasDAO;
    private final IConfiguracionSistemaDAO configuracionDAO;

    @GetMapping("/actual")
    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, Object>> consultar(Authentication autenticacion) {
        Cuestionario cuestionario = obtenerCuestionario(autenticacion);
        var candidato = cuestionario.getCandidato();
        String instrucciones = configuracionDAO.obtener("INSTRUCCIONES_CUESTIONARIO")
            .orElseThrow(() -> new IllegalStateException("Falta configurar las instrucciones del cuestionario."))
            .getValor();

        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of(
            "idCuestionario", cuestionario.getIdCuestionario(),
            "nombreCandidato", candidato.getNombre() + " " + candidato.getApellido(),
            "estado", cuestionario.getEstado(),
            "instrucciones", instrucciones));
    }

    @PostMapping("/iniciar")
    public ResponseEntity<Map<String, Object>> iniciar(Authentication autenticacion) {
        Cuestionario cuestionario = obtenerCuestionario(autenticacion);
        if (yaCerrado(cuestionario)) {
            return cerrado(cuestionario.getEstado().name());
        }
        int id = cuestionario.getIdCuestionario();
        if (gestorCuestionario.verificarTiempoMaximo(id)) {
            return cerrado(EstadoCuestionario.INCOMPLETO.name());
        }
        if (gestorCuestionario.verificarAccesosMaximos(id)) {
            return cerrado("ACCESOS_SUPERADOS");
        }

        try {
            int nroBloque = gestorCuestionario.iniciarCuestionario(id);
            return ResponseEntity.ok(Map.of("nroBloque", nroBloque));
        } catch (PlazoInicioVencidoException ex) {
            return cerrado(EstadoCuestionario.SIN_CONTESTAR.name());
        }
    }

    @GetMapping("/bloques/{nroBloque}")
    public ResponseEntity<Map<String, Object>> recuperarBloque(
            Authentication autenticacion, @PathVariable int nroBloque) {
        Cuestionario cuestionario = obtenerCuestionario(autenticacion);
        if (yaCerrado(cuestionario)) {
            return cerrado(cuestionario.getEstado().name());
        }
        if (gestorCuestionario.verificarTiempoMaximo(cuestionario.getIdCuestionario())) {
            return cerrado(EstadoCuestionario.INCOMPLETO.name());
        }
        verificarEnProceso(cuestionario);

        List<BloquePreguntas> bloques = bloques(cuestionario);
        if (pendiente(bloques).filter(nro -> nro == nroBloque).isEmpty()) {
            throw new IllegalArgumentException("Solo podés consultar el bloque pendiente.");
        }
        var preguntas = gestorCuestionario.recuperarBloque(cuestionario.getIdCuestionario(), nroBloque);
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(Map.of(
            "nroBloque", nroBloque, "totalBloques", bloques.size(), "preguntas", preguntas));
    }

    @PutMapping("/bloques/{nroBloque}")
    public ResponseEntity<Map<String, Object>> guardarBloque(Authentication autenticacion,
            @PathVariable int nroBloque, @RequestBody List<RespuestaCandidatoDTO> respuestas) {
        Cuestionario cuestionario = obtenerCuestionario(autenticacion);
        if (yaCerrado(cuestionario)) {
            return cerrado(cuestionario.getEstado().name());
        }
        if (gestorCuestionario.verificarTiempoMaximo(cuestionario.getIdCuestionario())) {
            return cerrado(EstadoCuestionario.INCOMPLETO.name());
        }

        List<BloquePreguntas> bloques = bloques(cuestionario);
        BloquePreguntas solicitado = bloques.stream()
            .filter(bloque -> bloque.getNroBloque() == nroBloque)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No existe el bloque indicado."));
        if (!Boolean.TRUE.equals(solicitado.getCompletado())) {
            verificarEnProceso(cuestionario);
            if (pendiente(bloques).filter(nro -> nro == nroBloque).isEmpty()) {
                throw new IllegalArgumentException("Solo podés guardar el bloque pendiente.");
            }
            gestorCuestionario.guardarBloque(cuestionario.getIdCuestionario(), respuestas);
        }
        return pendiente(bloques)
            .map(nro -> ResponseEntity.ok(Map.<String, Object>of(
                "estado", cuestionario.getEstado(), "siguienteBloque", nro)))
            .orElseGet(() -> ResponseEntity.ok(Map.of("estado", cuestionario.getEstado())));
    }

    private Cuestionario obtenerCuestionario(Authentication autenticacion) {
        if (autenticacion == null || !(autenticacion.getCredentials() instanceof Map<?, ?> datos)
                || !(datos.get("idCuestionario") instanceof Number id)
                || !(datos.get("nroCandidato") instanceof Number nroCandidato)) {
            throw new AutenticacionException("Ingresá como candidato para acceder al cuestionario.");
        }
        return cuestionarioDAO.findById(id.intValue())
            .filter(cuestionario -> cuestionario.getCandidato().getNroCandidato() == nroCandidato.intValue())
            .orElseThrow(() -> new AutenticacionException("El cuestionario no está disponible para este candidato."));
    }

    private List<BloquePreguntas> bloques(Cuestionario cuestionario) {
        return bloquePreguntasDAO.recuperarBloquesPorCuestionario(cuestionario.getIdCuestionario());
    }

    private Optional<Integer> pendiente(List<BloquePreguntas> bloques) {
        return bloques.stream()
            .filter(bloque -> Boolean.FALSE.equals(bloque.getCompletado()))
            .map(BloquePreguntas::getNroBloque)
            .min(Comparator.naturalOrder());
    }

    private void verificarEnProceso(Cuestionario cuestionario) {
        if (cuestionario.getEstado() != EstadoCuestionario.EN_PROCESO) {
            throw new IllegalArgumentException("El cuestionario debe estar en proceso.");
        }
    }

    private boolean yaCerrado(Cuestionario cuestionario) {
        return cuestionario.getEstado() == EstadoCuestionario.INCOMPLETO
            || cuestionario.getEstado() == EstadoCuestionario.SIN_CONTESTAR;
    }

    private ResponseEntity<Map<String, Object>> cerrado(String motivo) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("motivo", motivo));
    }

    @ExceptionHandler(ConcurrencyFailureException.class)
    public ResponseEntity<Map<String, Object>> manejarActualizacionConcurrente() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("mensaje", "El cuestionario se está actualizando. Volvé a intentar."));
    }
}
