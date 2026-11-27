package br.com.delivery.catalogo.exceptions;

public class ResponseStatusException extends Exception {

    public ResponseStatusException(String mensagem) {
        super(mensagem);
    }
}
