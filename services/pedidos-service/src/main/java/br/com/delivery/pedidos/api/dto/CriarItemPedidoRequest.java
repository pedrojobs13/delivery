package br.com.delivery.pedidos.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarItemPedidoRequest(
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        String nomeProduto,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade mínima é 1")
        Integer quantidade,

        @NotNull(message = "O preço unitário é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
        BigDecimal precoUnitario
) {}
