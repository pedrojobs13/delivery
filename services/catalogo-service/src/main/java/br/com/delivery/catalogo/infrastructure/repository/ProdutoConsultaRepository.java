package br.com.delivery.catalogo.infrastructure.repository;

import br.com.delivery.catalogo.domain.model.ProdutoConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoConsultaRepository extends JpaRepository<ProdutoConsulta, Long> {

    List<ProdutoConsulta> findByDisponivelTrue();

    List<ProdutoConsulta>
    findByDisponivelTrueAndNomeContainingIgnoreCase(String nome);

    Optional<ProdutoConsulta> findByIdAndDisponivelTrue(Long id);
}