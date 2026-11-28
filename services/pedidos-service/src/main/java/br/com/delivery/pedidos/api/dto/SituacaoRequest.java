package br.com.delivery.pedidos.api.dto;

import br.com.delivery.pedidos.domain.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record SituacaoRequest(@NotNull(message = "A situação é obrigatória") StatusPedido situacao) {
}
