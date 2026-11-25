package com.grupo30.capitalhumano.evaluaciones.exception;

// Excepción lanzada cuando fallan las credenciales o condiciones de acceso en CU001.
public class AutenticacionException extends RuntimeException {
    public AutenticacionException(String message) {
        super(message);
    }
}