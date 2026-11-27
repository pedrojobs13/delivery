package br.com.delivery.catalogo.infrastructure.repository;

import br.com.delivery.catalogo.domain.PedidoConsulta;
import br.com.delivery.catalogo.domain.SituacaoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoConsultaRepository extends JpaRepository<PedidoConsulta, Long> {
    @Query("""
        select distinct p
        from PedidoConsulta p
        left join fetch p.itens
        """)
    List<PedidoConsulta> listarComItens();

    @Query("""
    select count(p)
    from PedidoConsulta p
    where p.situacao = :situacao
    """)
    long countBySituacao(
            @Param("situacao") SituacaoPedido situacao
    );


}