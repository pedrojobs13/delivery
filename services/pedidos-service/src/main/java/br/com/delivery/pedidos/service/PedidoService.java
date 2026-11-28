package br.com.delivery.pedidos.service;

import br.com.delivery.pedidos.api.dto.CriarItemPedidoRequest;
import br.com.delivery.pedidos.api.dto.CriarPedidoRequest;
import br.com.delivery.pedidos.api.dto.PedidoResponse;
import br.com.delivery.pedidos.infrastructure.CatalogoClient;
import br.com.delivery.pedidos.domain.FormaPagamento;
import br.com.delivery.pedidos.domain.ItemPedido;
import br.com.delivery.pedidos.domain.Pedido;
import br.com.delivery.pedidos.domain.StatusPedido;
import br.com.delivery.pedidos.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;

    public PedidoService(PedidoRepository pedidoRepository, CatalogoClient catalogoClient) {
        this.pedidoRepository = pedidoRepository;
        this.catalogoClient = catalogoClient;
    }

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        Pedido pedido = new Pedido();
        pedido.setClienteNome(request.clienteNome());
        pedido.setEnderecoEntrega(request.enderecoEntrega());
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setPago(false);

        for (CriarItemPedidoRequest itemRequest : request.itens()) {
            ItemPedido item = new ItemPedido();
            item.setProdutoId(itemRequest.produtoId());
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(itemRequest.precoUnitario());

            String nomeProduto = itemRequest.nomeProduto();
            if (nomeProduto == null || nomeProduto.isBlank()) {
                try {
                    var produtoCatalogo = catalogoClient.buscarProduto(itemRequest.produtoId());
                    if (produtoCatalogo != null && produtoCatalogo.nome() != null) {
                        nomeProduto = produtoCatalogo.nome();
                    }
                } catch (Exception e) {
                    nomeProduto = "Produto #" + itemRequest.produtoId();
                }
            }
            item.setNomeProduto(nomeProduto);

            pedido.adicionarItem(item);
        }

        Pedido salvo = pedidoRepository.save(pedido);
        return PedidoResponse.from(salvo);
    }

    @Transactional
    public PedidoResponse simularPagamento(Long pedidoId, FormaPagamento formaPagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado com ID: " + pedidoId));

        pedido.marcarComoPago(formaPagamento);
        Pedido salvo = pedidoRepository.save(pedido);
        return PedidoResponse.from(salvo);
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado com ID: " + pedidoId));
        return PedidoResponse.from(pedido);
    }

    @Transactional
    public PedidoResponse cancelar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado com ID: " + pedidoId));
        pedido.cancelar();
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse alterarSituacao(Long pedidoId, StatusPedido novaSituacao) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new NoSuchElementException("Pedido não encontrado com ID: " + pedidoId));
        pedido.alterarSituacao(novaSituacao);
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar(StatusPedido status, Boolean pago) {
        List<Pedido> pedidos;
        if (status != null && pago != null) {
            pedidos = pedidoRepository.findByStatusAndPago(status, pago);
        } else if (status != null) {
            pedidos = pedidoRepository.findByStatus(status);
        } else if (pago != null) {
            pedidos = pedidoRepository.findByPago(pago);
        } else {
            pedidos = pedidoRepository.findAll();
        }
        return pedidos.stream().map(PedidoResponse::from).toList();
    }
}
