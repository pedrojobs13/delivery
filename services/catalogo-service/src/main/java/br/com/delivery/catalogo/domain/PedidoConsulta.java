package br.com.delivery.catalogo.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "pedido_consulta")
@Entity
@Builder(toBuilder = true)
public class PedidoConsulta {

    @Id
    private Long id;

    private LocalDateTime dataHora;
    private Long clienteId;
    private String clienteNome;
    private String enderecoEntrega;

    @Enumerated(EnumType.STRING)
    private SituacaoPedido situacao;

    private Boolean pago;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private Double total;

    @ElementCollection
    @CollectionTable(
            name = "pedido_consulta_itens",
            joinColumns = @JoinColumn(name = "pedido_id")
    )
    private List<ItemDetalhe> itens = new ArrayList<>();
}
