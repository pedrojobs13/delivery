package br.com.delivery.pedidos.repository;

import br.com.delivery.pedidos.domain.Pedido;
import br.com.delivery.pedidos.domain.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByClienteNomeContainingIgnoreCase(String clienteNome);
}