package br.com.delivery.pedidos.api;

import br.com.delivery.pedidos.api.dto.*;
import br.com.delivery.pedidos.infrastructure.CatalogoClient;
import br.com.delivery.pedidos.domain.StatusPedido;
import br.com.delivery.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    private final CatalogoClient catalogoClient;
    private final PedidoService pedidoService;

    public PedidosController(CatalogoClient catalogoClient, PedidoService pedidoService) {
        this.catalogoClient = catalogoClient;
        this.pedidoService = pedidoService;
    }

    @PostMapping("/pre-validacoes")
    public ResponseEntity<PreValidacaoPedidoResponse> preValidar(
            @Valid @RequestBody PreValidacaoPedidoRequest request
    ) {
        CatalogoClient.ProdutoCatalogoResponse produto =
                catalogoClient.buscarProduto(request.produtoId());

        PreValidacaoPedidoResponse resposta = new PreValidacaoPedidoResponse(
                produto.id(),
                request.quantidade(),
                produto.nome(),
                produto.disponivel()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody CriarPedidoRequest request) {
        PedidoResponse response = pedidoService.criarPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar(
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) Boolean pago
    ) {
        return ResponseEntity.ok(pedidoService.listar(status, pago));
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<PedidoResponse> pagarPedido(
            @PathVariable Long id,
            @Valid @RequestBody PagamentoRequest request
    ) {
        PedidoResponse response = pedidoService.simularPagamento(id, request.formaPagamento());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<PedidoResponse> alterarSituacao(
            @PathVariable Long id, @Valid @RequestBody SituacaoRequest request) {
        return ResponseEntity.ok(pedidoService.alterarSituacao(id, request.situacao()));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}
