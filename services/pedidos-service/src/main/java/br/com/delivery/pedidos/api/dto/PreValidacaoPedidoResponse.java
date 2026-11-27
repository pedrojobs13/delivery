package br.com.delivery.pedidos.api.dto;

public record PreValidacaoPedidoResponse(
        Long produtoId,
        Integer quantidade,
        String produto,
        boolean produtoDisponivel
) {
}
