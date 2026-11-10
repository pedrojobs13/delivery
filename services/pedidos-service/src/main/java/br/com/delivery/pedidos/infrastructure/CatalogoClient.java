package br.com.delivery.pedidos.infrastructure;

import br.com.delivery.pedidos.api.PreValidacaoPedidoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CatalogoClient {

    private final RestClient restClient;

    public CatalogoClient(
            RestClient.Builder builder,
            @Value("${catalogo-service.base-url}") String catalogoBaseUrl
    ) {
        this.restClient = builder.baseUrl(catalogoBaseUrl).build();
    }

    public ProdutoCatalogoResponse buscarProduto(Long produtoId) {
        return restClient.get()
                .uri("/catalogo/produtos/{id}", produtoId)
                .retrieve()
                .body(ProdutoCatalogoResponse.class);
    }

    public record ProdutoCatalogoResponse(Long id, String nome, boolean disponivel) {
    }
}
