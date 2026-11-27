package br.com.delivery.catalogo.service;

import br.com.delivery.catalogo.api.dto.ItemDetalheDto;
import br.com.delivery.catalogo.api.dto.PedidoConsultaDto;
import br.com.delivery.catalogo.domain.ItemDetalhe;
import br.com.delivery.catalogo.domain.PedidoConsulta;
import br.com.delivery.catalogo.domain.SituacaoPedido;
import br.com.delivery.catalogo.exceptions.DadosInvalidosException;
import br.com.delivery.catalogo.infrastructure.repository.PedidoConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PedidoConsultaService {
    private final PedidoConsultaRepository pedidoConsultaRepository;


    public List<PedidoConsulta> listar() {
        return pedidoConsultaRepository.findAll();
    }

    public PedidoConsulta buscar(Long id) throws DadosInvalidosException {
        return pedidoConsultaRepository.findById(id)
                .orElseThrow(() -> new DadosInvalidosException(
                        "O ID da URL deve ser igual ao ID do pedido recebido"
                ));
    }

    public Map<SituacaoPedido, Long> contarPorSituacao() {

        Map<SituacaoPedido, Long> resultado = new HashMap<>();

        contagemTotalDeSituacaoDosPedidos(resultado);

        return resultado;
    }


    @Transactional(rollbackFor = DadosInvalidosException.class)
    public void atualizar(Long id, PedidoConsultaDto dados) throws DadosInvalidosException {
        if (dados == null || id == null) {

            throw new DadosInvalidosException(
                    "O ID da URL deve ser igual ao ID do pedido recebido"
            );
        }
        if(!id.equals(buscar(id).getId())){
            throw new DadosInvalidosException(
                    "O ID da URL deve ser igual ao ID do pedido recebido"
            );
        }
        PedidoConsulta pedidoConsulta = toDomain(id, dados);
        pedidoConsultaRepository.save(pedidoConsulta);
    }

    private void contagemTotalDeSituacaoDosPedidos(Map<SituacaoPedido, Long> resultado) {
        for (SituacaoPedido situacao : SituacaoPedido.values()) {

            long quantidade =
                    pedidoConsultaRepository.countBySituacao(situacao);

            resultado.put(situacao, quantidade);
        }
    }

    private PedidoConsulta toDomain(Long id, PedidoConsultaDto dados) {
        return PedidoConsulta.builder().id(id)
                .dataHora(dados.dataHora())
                .clienteId(dados.clienteId())
                .clienteNome(dados.clienteNome())
                .enderecoEntrega(dados.enderecoEntrega()).situacao(dados.situacao())
                .pago(dados.pago())
                .formaPagamento(dados.formaPagamento())
                .total(dados.total())
                .itens(toDomain(dados.itens())).build();
    }

    private List<ItemDetalhe> toDomain(List<ItemDetalheDto> itemDetalheDto) {
        if (itemDetalheDto == null) {
            return List.of();
        }

        return itemDetalheDto.stream()
                .map(dto -> new ItemDetalhe(
                        dto.produtoId(),
                        dto.produtoNome(),
                        dto.quantidade(),
                        dto.precoUnitario(),
                        dto.subtotal()
                ))
                .toList();
    }
}

