package br.com.delivery.pedidos.api;

import br.com.delivery.pedidos.infrastructure.CatalogoClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {

    private final CatalogoClient catalogoClient;

    public PedidosController(CatalogoClient catalogoClient) {
        this.catalogoClient = catalogoClient;
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
}
