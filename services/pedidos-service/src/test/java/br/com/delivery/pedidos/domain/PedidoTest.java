package br.com.delivery.pedidos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    @DisplayName("Deve inicializar pedido com status PENDENTE e pago como false")
    void deveInicializarPedidoPendenteENaoPago() {
        Pedido pedido = new Pedido();

        assertEquals(StatusPedido.PENDENTE, pedido.getStatus());
        assertFalse(pedido.getPago());
        assertNull(pedido.getFormaPagamento());
        assertNull(pedido.getPagoEm());
    }

    @Test
    @DisplayName("Quando o pedido é pago deve avançar de PENDENTE para CONFIRMADO")
    void quandoPedidoPagoDeveAvancarDePendenteParaConfirmado() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.PENDENTE);

        pedido.marcarComoPago(FormaPagamento.PIX);

        assertTrue(pedido.getPago());
        assertEquals(StatusPedido.CONFIRMADO, pedido.getStatus());
        assertEquals(FormaPagamento.PIX, pedido.getFormaPagamento());
        assertNotNull(pedido.getPagoEm());
    }

    @Test
    @DisplayName("Não deve permitir pagar um pedido que já está pago")
    void naoDevePermitirPagarPedidoJaPago() {
        Pedido pedido = new Pedido();
        pedido.marcarComoPago(FormaPagamento.CARTAO_CREDITO);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                pedido.marcarComoPago(FormaPagamento.PIX)
        );
        assertEquals("Pedido já se encontra pago.", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve permitir pagar um pedido que não está com status PENDENTE")
    void naoDevePermitirPagarPedidoComStatusDiferenteDePendente() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.CANCELADO);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                pedido.marcarComoPago(FormaPagamento.DINHEIRO)
        );
        assertTrue(ex.getMessage().contains("Apenas pedidos com status PENDENTE podem ser pagos"));
    }

    @Test
    @DisplayName("Deve recalcular o valor total ao adicionar itens")
    void deveRecalcularValorTotal() {
        Pedido pedido = new Pedido();

        ItemPedido item1 = new ItemPedido();
        item1.setQuantidade(2);
        item1.setPrecoUnitario(new BigDecimal("15.50"));

        ItemPedido item2 = new ItemPedido();
        item2.setQuantidade(1);
        item2.setPrecoUnitario(new BigDecimal("10.00"));

        pedido.adicionarItem(item1);
        pedido.adicionarItem(item2);

        assertEquals(new BigDecimal("41.00"), pedido.getValorTotal());
    }
}
