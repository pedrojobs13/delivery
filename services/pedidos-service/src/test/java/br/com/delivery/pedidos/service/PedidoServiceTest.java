package br.com.delivery.pedidos.service;

import br.com.delivery.pedidos.api.dto.CriarItemPedidoRequest;
import br.com.delivery.pedidos.api.dto.CriarPedidoRequest;
import br.com.delivery.pedidos.api.dto.PedidoResponse;
import br.com.delivery.pedidos.infrastructure.CatalogoClient;
import br.com.delivery.pedidos.domain.FormaPagamento;
import br.com.delivery.pedidos.domain.Pedido;
import br.com.delivery.pedidos.domain.StatusPedido;
import br.com.delivery.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve criar pedido com sucesso e recalcular total")
    void deveCriarPedidoComSucesso() {
        CriarPedidoRequest request = new CriarPedidoRequest(
                "João Santos",
                "Av. Brasil, 500",
                List.of(
                        new CriarItemPedidoRequest(1L, "Produto 1", 2, new BigDecimal("25.00")),
                        new CriarItemPedidoRequest(2L, "Produto 2", 1, new BigDecimal("50.00"))
                )
        );

        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido p = invocation.getArgument(0);
            p.setId(10L);
            return p;
        });

        PedidoResponse response = pedidoService.criarPedido(request);

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("João Santos", response.clienteNome());
        assertEquals(StatusPedido.PENDENTE, response.status());
        assertFalse(response.pago());
        assertEquals(new BigDecimal("100.00"), response.valorTotal());
        assertEquals(2, response.itens().size());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void deveSalvarPedidoCancelado() {
        Pedido pedido = new Pedido();
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        PedidoResponse resposta = pedidoService.cancelar(1L);
        assertEquals(StatusPedido.CANCELADO, resposta.status());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void naoDeveCancelarPedidoInexistente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class, () -> pedidoService.cancelar(1L));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveSalvarSituacaoAlterada() {
        Pedido pedido = new Pedido();
        pedido.setStatus(StatusPedido.CONFIRMADO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoResponse resposta = pedidoService.alterarSituacao(1L, StatusPedido.EM_PREPARO);

        assertEquals(StatusPedido.EM_PREPARO, resposta.status());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void naoDeveAlterarSituacaoDePedidoInexistente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(java.util.NoSuchElementException.class,
                () -> pedidoService.alterarSituacao(1L, StatusPedido.EM_PREPARO));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve simular pagamento avançando de PENDENTE para CONFIRMADO")
    void deveSimularPagamentoComSucesso() {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setPago(false);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PedidoResponse response = pedidoService.simularPagamento(1L, FormaPagamento.PIX);

        assertTrue(response.pago());
        assertEquals(StatusPedido.CONFIRMADO, response.status());
        assertEquals(FormaPagamento.PIX, response.formaPagamento());
        assertNotNull(response.pagoEm());
        verify(pedidoRepository).save(pedido);
    }
}
