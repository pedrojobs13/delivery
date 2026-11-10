package br.com.delivery.pedidos.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PreValidacaoPedidoRequest(
        @NotNull Long produtoId,
        @NotNull @Min(1) Integer quantidade
) {
}
