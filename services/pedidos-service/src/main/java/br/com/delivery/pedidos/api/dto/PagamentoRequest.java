package br.com.delivery.pedidos.api.dto;

import br.com.delivery.pedidos.domain.FormaPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento formaPagamento
) {}
