package br.com.delivery.pedidos.api.dto;

import br.com.delivery.pedidos.domain.FormaPagamento;
import br.com.delivery.pedidos.domain.Pedido;
import br.com.delivery.pedidos.domain.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String clienteNome,
        String enderecoEntrega,
        Boolean pago,
        StatusPedido status,
        FormaPagamento formaPagamento,
        LocalDateTime pagoEm,
        BigDecimal valorTotal,
        LocalDateTime criadoEm,
        List<ItemPedidoResponse> itens
) {
    public static PedidoResponse from(Pedido pedido) {
        List<ItemPedidoResponse> itens = pedido.getItens() != null
                ? pedido.getItens().stream().map(ItemPedidoResponse::from).toList()
                : List.of();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getClienteNome(),
                pedido.getEnderecoEntrega(),
                pedido.getPago(),
                pedido.getStatus(),
                pedido.getFormaPagamento(),
                pedido.getPagoEm(),
                pedido.getValorTotal(),
                pedido.getCriadoEm(),
                itens
        );
    }
}
