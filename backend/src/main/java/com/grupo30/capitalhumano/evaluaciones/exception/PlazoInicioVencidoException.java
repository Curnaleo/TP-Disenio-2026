package com.grupo30.capitalhumano.evaluaciones.exception;

public class PlazoInicioVencidoException extends IllegalStateException {
    public PlazoInicioVencidoException() {
        super("Venció el plazo para iniciar el cuestionario");
    }
}
