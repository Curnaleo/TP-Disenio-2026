package com.grupo30.capitalhumano.evaluaciones.exception;

public class PreguntaDuplicadaException extends RuntimeException {

    public PreguntaDuplicadaException() {
        super("La pregunta ya existe");
    }
}
