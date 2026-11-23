package br.com.delivery.pedidos_query.repository;

import br.com.delivery.pedidos_query.model.ProdutoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoConsultaRepository extends JpaRepository<ProdutoConsulta, Long> {

    List<ProdutoConsulta> findByDisponivelTrue();

    List<ProdutoConsulta> findByDisponivelTrueAndNomeContainingIgnoreCase(String nome);

    Optional<ProdutoConsulta> findByIdAndDisponivelTrue(Long id);
}