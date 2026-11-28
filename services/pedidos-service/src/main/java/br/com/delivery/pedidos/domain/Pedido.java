package br.com.delivery.pedidos.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clienteNome;

    private String enderecoEntrega;

    private Boolean pago = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PENDENTE;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private LocalDateTime pagoEm;

    @Column(nullable = false)
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void alterarSituacao(StatusPedido novaSituacao) {
        if (novaSituacao == null) {
            throw new IllegalArgumentException("A nova situação é obrigatória.");
        }
        if ((status == StatusPedido.CONFIRMADO && novaSituacao == StatusPedido.EM_PREPARO)
                || (status == StatusPedido.EM_PREPARO && novaSituacao == StatusPedido.SAIU_PARA_ENTREGA)
                || (status == StatusPedido.SAIU_PARA_ENTREGA && novaSituacao == StatusPedido.ENTREGUE)) {
            this.status = novaSituacao;
            return;
        }
        throw new IllegalStateException("Não é possível alterar a situação de " + status + " para " + novaSituacao + ".");
    }

    public void cancelar() {
        if (status != StatusPedido.PENDENTE && status != StatusPedido.CONFIRMADO
                && status != StatusPedido.EM_PREPARO) {
            throw new IllegalStateException("Não é possível cancelar pedido com situação " + status + ".");
        }
        this.status = StatusPedido.CANCELADO;
    }

    public void marcarComoPago(FormaPagamento formaPagamento) {
        if (Boolean.TRUE.equals(this.pago)) {
            throw new IllegalStateException("Pedido já se encontra pago.");
        }
        if (this.status != StatusPedido.PENDENTE) {
            throw new IllegalStateException("Apenas pedidos com status PENDENTE podem ser pagos. Status atual: " + this.status);
        }
        if (formaPagamento == null) {
            throw new IllegalArgumentException("Forma de pagamento é obrigatória.");
        }
        this.pago = true;
        this.formaPagamento = formaPagamento;
        this.pagoEm = LocalDateTime.now();
        this.status = StatusPedido.CONFIRMADO;
    }
}
