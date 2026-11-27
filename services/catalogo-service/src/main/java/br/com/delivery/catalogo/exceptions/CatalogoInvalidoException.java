package br.com.delivery.catalogo.exceptions;

public class CatalogoInvalidoException extends Exception {

    public CatalogoInvalidoException(String mensagem) {
        super(mensagem);
    }
}