package com.capitalhumano.evaluaciones.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cuestionario {
    private int idCuestionario;
    private String clave;
    private EstadoCuestionario estado;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private LocalDateTime fechaHoraUltimoIngreso;
    private int cantidadAccesos;
    private int maximoAccesos;
    private int tiempoMaximo;
    private Candidato candidato;
    private List<BloquePreguntas> bloques = new ArrayList<>();
    private List<ResultadoFactor> resultadosFactor = new ArrayList<>();
    private List<ResultadoCompetencia> resultadosCompetencia = new ArrayList<>();
}
