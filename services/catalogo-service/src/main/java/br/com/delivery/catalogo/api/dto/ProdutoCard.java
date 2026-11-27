package br.com.delivery.catalogo.api.dto;

public record ProdutoCard(
        Long id,
        String nome,
        String descricao,
        Double precoVenda,
        String imagemUrl
) {
}