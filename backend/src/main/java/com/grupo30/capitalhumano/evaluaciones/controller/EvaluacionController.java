package com.grupo30.capitalhumano.evaluaciones.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grupo30.capitalhumano.evaluaciones.dto.CandidatoGestionDTO;
import com.grupo30.capitalhumano.evaluaciones.service.GestorEvaluaciones;

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

    //+exportarClavesExcel(idEvaluacion: int, carpeta: String) : void
    
}
