package br.com.delivery.pedidos.api;

import br.com.delivery.pedidos.domain.StatusPedido;
import br.com.delivery.pedidos.infrastructure.CatalogoClient;
import br.com.delivery.pedidos.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidosController.class)
class PedidosControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private PedidoService pedidoService;
    @MockitoBean
    private CatalogoClient catalogoClient;

    @Test
    void deveAceitarAlteracaoDeSituacao() throws Exception {
        mvc.perform(patch("/pedidos/1/situacao").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"situacao\":\"EM_PREPARO\"}"))
                .andExpect(status().isOk());
        verify(pedidoService).alterarSituacao(1L, StatusPedido.EM_PREPARO);
    }

    @Test
    void deveRejeitarSituacaoAusente() throws Exception {
        mvc.perform(patch("/pedidos/1/situacao").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(pedidoService);
    }

    @Test
    void deveRetornarNaoEncontradoAoCancelarPedidoInexistente() throws Exception {
        when(pedidoService.cancelar(1L)).thenThrow(new NoSuchElementException("Pedido não encontrado"));
        mvc.perform(patch("/pedidos/1/cancelar")).andExpect(status().isNotFound());
    }
}
