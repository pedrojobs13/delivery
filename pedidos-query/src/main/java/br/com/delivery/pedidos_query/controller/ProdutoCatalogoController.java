package br.com.delivery.pedidos_query.controller;

import br.com.delivery.pedidos_query.dto.ProdutoCard;
import br.com.delivery.pedidos_query.dto.ProdutoCatalogoRequest;
import br.com.delivery.pedidos_query.service.ProdutoCatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProdutoCatalogoController {

    private final ProdutoCatalogoService service;

    public ProdutoCatalogoController(ProdutoCatalogoService service) {
        this.service = service;
    }

    @PutMapping("/internal/catalogo")
    public ResponseEntity<Void> atualizarCatalogo(@RequestBody List<ProdutoCatalogoRequest> produtos) {
        service.substituirCatalogo(produtos);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoCard>> listarProdutos(@RequestParam(required = false) String busca) {
        return ResponseEntity.ok(service.listarDisponiveis(busca));
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity<ProdutoCard> buscarProduto(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}