package br.com.delivery.catalogo.api;

import br.com.delivery.catalogo.exceptions.CatalogoInvalidoException;
import br.com.delivery.catalogo.exceptions.DadosInvalidosException;
import br.com.delivery.catalogo.exceptions.ProdutoNaoEncontradoException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<Map<String, String>> tratarDadosInvalidos(
            DadosInvalidosException exception) {

        return ResponseEntity
                .badRequest()
                .body(Map.of("mensagem", exception.getMessage()));
    }

    @ExceptionHandler(CatalogoInvalidoException.class)
    public ResponseEntity<Map<String, String>> tratarCatalogoInvalido(
            CatalogoInvalidoException exception) {

        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "mensagem", exception.getMessage()
                ));
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> tratarProdutoNaoEncontrado(
            ProdutoNaoEncontradoException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "mensagem", exception.getMessage()
                ));
    }
}
