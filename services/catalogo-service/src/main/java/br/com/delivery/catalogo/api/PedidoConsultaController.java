package br.com.delivery.catalogo.api;


import br.com.delivery.catalogo.domain.PedidoConsulta;
import br.com.delivery.catalogo.domain.SituacaoPedido;
import br.com.delivery.catalogo.api.dto.PedidoConsultaDto;
import br.com.delivery.catalogo.exceptions.DadosInvalidosException;
import br.com.delivery.catalogo.service.PedidoConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoConsultaController {
    private final PedidoConsultaService pedidoConsultaService;

    @GetMapping("")
    public ResponseEntity<List<PedidoConsulta>> listar() {

        List<PedidoConsulta> pedidos =
                pedidoConsultaService.listar();

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoConsulta> buscar(
            @PathVariable("id") Long id) throws DadosInvalidosException {

        PedidoConsulta pedido =
                pedidoConsultaService.buscar(id);

        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/contagens")
    public ResponseEntity<Map<SituacaoPedido, Long>> contarPorSituacao() {

        Map<SituacaoPedido, Long> contagens =
                pedidoConsultaService.contarPorSituacao();

        return ResponseEntity.ok(contagens);
    }

    @PutMapping("/internal/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable("id") Long id,
            @RequestBody PedidoConsultaDto dados)
            throws DadosInvalidosException {

        pedidoConsultaService.atualizar(id, dados);

        return ResponseEntity.noContent().build();
    }
}
