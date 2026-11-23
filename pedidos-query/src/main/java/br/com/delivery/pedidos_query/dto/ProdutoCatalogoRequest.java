package br.com.delivery.pedidos_query.dto;

import java.math.BigDecimal;

public record ProdutoCatalogoRequest(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoVenda,
        String imagemUrl,
        Boolean disponivel
) {
}