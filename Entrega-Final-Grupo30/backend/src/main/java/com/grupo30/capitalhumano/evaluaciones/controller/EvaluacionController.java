package com.grupo30.capitalhumano.evaluaciones.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.service.GestorEvaluaciones;
import com.grupo30.capitalhumano.evaluaciones.service.GestorEvaluaciones.VistaPrevia;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluacion")
@RequiredArgsConstructor
public class EvaluacionController {
    private final GestorEvaluaciones gestorEvaluaciones;

    @GetMapping("/candidatos")
	public List<CandidatoGestionDTO> buscarCandidatos(
		@RequestParam(required = false) String apellido,
		@RequestParam(required = false) String nombre,
		@RequestParam(required = false) Integer nroCandidato) {

		return gestorEvaluaciones.buscarCandidatos(
			apellido,
			nombre,
			nroCandidato
		);
	}

    //+generarEvaluacion(idPuesto: int, candidatos: List<CandidatoGestionDTO>) : void
    
    @PostMapping("/{idPuesto}")
    public ResponseEntity<Void> generarEvaluacion(
        @PathVariable Integer idPuesto,
        @RequestBody List<CandidatoGestionDTO> candidatos){
            
            gestorEvaluaciones.generarEvaluacion(idPuesto, candidatos);

            return ResponseEntity
			.status(HttpStatus.CREATED).build();
    }
     
    //+validarCandidatosActivos(candidatos: List<CandidatoGestionDTO>) : List<CandidatoGestionDTO>
    @PostMapping("/candidatos/validar")
    public List<CandidatoGestionDTO> validarCandidatosActivos(
        @RequestBody List<CandidatoGestionDTO> candidatos){

        return gestorEvaluaciones.validarCandidatosActivos(candidatos);
    }

    @GetMapping("/puestos/{idPuesto}/validar")
    public ResponseEntity<Void> validarPuesto(@PathVariable Integer idPuesto) {
        gestorEvaluaciones.validarPuestoEvaluable(idPuesto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idPuesto}/vista-previa")
    public ResponseEntity<VistaPrevia> prepararEvaluacion(
        @PathVariable Integer idPuesto,
        @RequestBody List<CandidatoGestionDTO> candidatos) {

        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
            .body(gestorEvaluaciones.prepararEvaluacion(idPuesto, candidatos));
    }

    @PostMapping("/{idPuesto}/confirmar/{idVista}")
    public ResponseEntity<Map<String, Integer>> confirmarEvaluacion(
        @PathVariable Integer idPuesto,
        @PathVariable UUID idVista) {

        int idEvaluacion = gestorEvaluaciones.confirmarEvaluacion(idPuesto, idVista);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idEvaluacion", idEvaluacion));
    }

    @GetMapping("/{idEvaluacion}/claves")
    public ResponseEntity<byte[]> exportarClavesExcel(@PathVariable int idEvaluacion) throws IOException {
        Path carpeta = Files.createTempDirectory("cu025-");
        try {
            gestorEvaluaciones.exportarClavesExcel(idEvaluacion, carpeta.toString());
            Path archivo;
            try (var archivos = Files.list(carpeta)) {
                archivo = archivos.findFirst()
                    .orElseThrow(() -> new IOException("No se generó el archivo Excel."));
            }
            String disposicion = ContentDisposition.attachment()
                .filename(archivo.getFileName().toString(), StandardCharsets.UTF_8).build().toString();
            return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposicion)
                .body(Files.readAllBytes(archivo));
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } finally {
            FileSystemUtils.deleteRecursively(carpeta);
        }
    }
}
