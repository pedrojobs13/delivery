package br.com.delivery.catalogo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/catalogo/produtos")
public class CatalogoController {

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        // Dados temporários: persistência e regras do catálogo serão definidas pelo grupo.
        ProdutoResponse produto = new ProdutoResponse(id, "Produto de demonstração", true);
        return ResponseEntity.ok(produto);
    }
}
