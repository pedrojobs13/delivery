package br.com.delivery.catalogo.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record ItemDetalhe(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        Double precoUnitario,
        Double subtotal
) {
}
