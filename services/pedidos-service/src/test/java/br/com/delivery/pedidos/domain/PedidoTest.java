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
    void deveAvancarSituacoesNaOrdem() {
        Pedido pedido = new Pedido();
        pedido.marcarComoPago(FormaPagamento.PIX);
        pedido.alterarSituacao(StatusPedido.EM_PREPARO);
        assertEquals(StatusPedido.EM_PREPARO, pedido.getStatus());
        pedido.alterarSituacao(StatusPedido.SAIU_PARA_ENTREGA);
        assertEquals(StatusPedido.SAIU_PARA_ENTREGA, pedido.getStatus());
        pedido.alterarSituacao(StatusPedido.ENTREGUE);
        assertEquals(StatusPedido.ENTREGUE, pedido.getStatus());
    }

    @Test
    void naoDevePermitirTransicoesInvalidas() {
        Pedido pedido = new Pedido();
        assertThrows(IllegalStateException.class, () -> pedido.alterarSituacao(StatusPedido.CONFIRMADO));
        pedido.marcarComoPago(FormaPagamento.PIX);
        assertThrows(IllegalStateException.class, () -> pedido.alterarSituacao(StatusPedido.ENTREGUE));
        assertThrows(IllegalStateException.class, () -> pedido.alterarSituacao(StatusPedido.PENDENTE));
        assertThrows(IllegalArgumentException.class, () -> pedido.alterarSituacao(null));
        pedido.setStatus(StatusPedido.CANCELADO);
        assertThrows(IllegalStateException.class, () -> pedido.alterarSituacao(StatusPedido.EM_PREPARO));
        pedido.setStatus(StatusPedido.ENTREGUE);
        assertThrows(IllegalStateException.class, () -> pedido.alterarSituacao(StatusPedido.EM_PREPARO));
    }

    @Test
    void deveCancelarAteEmPreparoEPreservarPagamento() {
        Pedido pendente = new Pedido();
        pendente.cancelar();
        assertEquals(StatusPedido.CANCELADO, pendente.getStatus());
        assertFalse(pendente.getPago());

        Pedido confirmado = new Pedido();
        confirmado.marcarComoPago(FormaPagamento.PIX);
        var dataPagamento = confirmado.getPagoEm();
        confirmado.cancelar();
        assertEquals(StatusPedido.CANCELADO, confirmado.getStatus());
        assertTrue(confirmado.getPago());
        assertEquals(FormaPagamento.PIX, confirmado.getFormaPagamento());
        assertEquals(dataPagamento, confirmado.getPagoEm());

        Pedido emPreparo = new Pedido();
        emPreparo.marcarComoPago(FormaPagamento.PIX);
        emPreparo.alterarSituacao(StatusPedido.EM_PREPARO);
        emPreparo.cancelar();
        assertEquals(StatusPedido.CANCELADO, emPreparo.getStatus());
    }

    @Test
    void naoDeveCancelarPedidoEmEntregaEntregueOuCancelado() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
        assertThrows(IllegalStateException.class, pedido::cancelar);
        pedido.setStatus(StatusPedido.ENTREGUE);
        assertThrows(IllegalStateException.class, pedido::cancelar);
        pedido.setStatus(StatusPedido.CANCELADO);
        assertThrows(IllegalStateException.class, pedido::cancelar);
        assertThrows(IllegalStateException.class, () -> pedido.marcarComoPago(FormaPagamento.PIX));
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
