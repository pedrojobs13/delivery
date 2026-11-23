package br.com.delivery.pedidos_query.dto;

import br.com.delivery.pedidos_query.model.ProdutoConsulta;
import java.math.BigDecimal;

public record ProdutoCard(
        Long id,
        String nome,
        String descricao,
        BigDecimal precoVenda,
        String imagemUrl
) {
    public static ProdutoCard fromEntity(ProdutoConsulta produto) {
        return new ProdutoCard(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPrecoVenda(),
                produto.getImagemUrl()
        );
    }
}