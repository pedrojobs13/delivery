package br.com.delivery.catalogo.api;

import br.com.delivery.catalogo.api.dto.ProdutoCard;
import br.com.delivery.catalogo.domain.model.ProdutoConsulta;
import br.com.delivery.catalogo.exceptions.CatalogoInvalidoException;
import br.com.delivery.catalogo.exceptions.ProdutoNaoEncontradoException;
import br.com.delivery.catalogo.service.ProdutoCatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoCatalogoController {

    private final ProdutoCatalogoService service;

    @GetMapping()
    public ResponseEntity<List<ProdutoCard>> listar(
            @RequestParam(name = "busca", required = false)
            String busca) {

        return ResponseEntity.ok(service.listar(busca));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoCard> buscar(
            @PathVariable("id") Long id)
            throws ProdutoNaoEncontradoException {

        return ResponseEntity.ok(service.buscar(id));
    }

    @PutMapping("/internal/catalogo")
    public ResponseEntity<Void> atualizarCatalogo(
            @RequestBody List<ProdutoConsulta> produtos)
            throws CatalogoInvalidoException {

        service.atualizarCatalogo(produtos);

        return ResponseEntity.noContent().build();
    }
}