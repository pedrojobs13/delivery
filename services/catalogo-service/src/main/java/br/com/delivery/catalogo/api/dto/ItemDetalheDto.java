package br.com.delivery.catalogo.api.dto;

public record ItemDetalheDto(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        Double precoUnitario,
        Double subtotal
) {
}
