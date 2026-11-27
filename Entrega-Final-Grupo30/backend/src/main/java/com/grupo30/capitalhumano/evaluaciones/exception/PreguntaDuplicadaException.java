package com.grupo30.capitalhumano.evaluaciones.exception;

public class PreguntaDuplicadaException extends RuntimeException {

    private final String campo;

    public PreguntaDuplicadaException(String campo) {
        super("Ya existe una pregunta con el mismo " + campo + " para esa competencia y factor");
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
