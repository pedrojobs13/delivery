package br.com.delivery.pedidos.api;

public record PreValidacaoPedidoResponse(
        Long produtoId,
        Integer quantidade,
        String produto,
        boolean produtoDisponivel
) {
}
