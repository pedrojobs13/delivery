package br.com.delivery.catalogo.api.dto;

import br.com.delivery.catalogo.domain.FormaPagamento;
import br.com.delivery.catalogo.domain.ItemDetalhe;
import br.com.delivery.catalogo.domain.SituacaoPedido;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoConsultaDto(
        LocalDateTime dataHora,
        Long clienteId,
        String clienteNome,
        String enderecoEntrega,
        SituacaoPedido situacao,
        Boolean pago, FormaPagamento formaPagamento,
        Double total,
        List<ItemDetalheDto> itens
) {
}
