package br.com.delivery.catalogo.exceptions;

public class ProdutoNaoEncontradoException extends Exception {

    public ProdutoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}