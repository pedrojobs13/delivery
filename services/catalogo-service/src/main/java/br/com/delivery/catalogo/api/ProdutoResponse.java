package br.com.delivery.catalogo.api;

public record ProdutoResponse(
        Long id,
        String nome,
        boolean disponivel
) {
}
