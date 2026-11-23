package br.com.delivery.pedidos_query.service;

import br.com.delivery.pedidos_query.dto.ProdutoCard;
import br.com.delivery.pedidos_query.dto.ProdutoCatalogoRequest;
import br.com.delivery.pedidos_query.model.ProdutoConsulta;
import br.com.delivery.pedidos_query.repository.ProdutoConsultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProdutoCatalogoService {

    private final ProdutoConsultaRepository repository;

    public ProdutoCatalogoService(ProdutoConsultaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void substituirCatalogo(List<ProdutoCatalogoRequest> itens) {
        if (itens == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A lista de produtos não pode ser nula.");
        }

        Set<Long> idsVistos = new HashSet<>();
        for (ProdutoCatalogoRequest item : itens) {
            validarItem(item, idsVistos);
        }

        repository.deleteAllInBatch();
        repository.flush();

        if (!itens.isEmpty()) {
            List<ProdutoConsulta> entidades = itens.stream()
                    .map(dto -> new ProdutoConsulta(
                            dto.id(),
                            dto.nome().trim(),
                            dto.descricao().trim(),
                            dto.precoVenda(),
                            (dto.imagemUrl() == null || dto.imagemUrl().isBlank()) ? null : dto.imagemUrl().trim(),
                            dto.disponivel()
                    ))
                    .toList();

            repository.saveAll(entidades);
        }
    }

    private void validarItem(ProdutoCatalogoRequest item, Set<Long> idsVistos) {
        if (item == null || item.id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todo produto deve informar um ID.");
        }
        if (!idsVistos.add(item.id())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID duplicado na lista do catálogo: " + item.id());
        }
        if (item.nome() == null || item.nome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome é obrigatório para o produto " + item.id());
        }
        if (item.descricao() == null || item.descricao().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição é obrigatória para o produto " + item.id());
        }
        if (item.precoVenda() == null || item.precoVenda().compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preço de venda inválido para o produto " + item.id());
        }
        if (item.disponivel() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo 'disponivel' é obrigatório para o produto " + item.id());
        }
    }

    @Transactional(readOnly = true)
    public List<ProdutoCard> listarDisponiveis(String busca) {
        List<ProdutoConsulta> produtos = (busca == null || busca.isBlank())
                ? repository.findByDisponivelTrue()
                : repository.findByDisponivelTrueAndNomeContainingIgnoreCase(busca.trim());

        return produtos.stream()
                .map(ProdutoCard::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoCard buscarPorId(Long id) {
        return repository.findByIdAndDisponivelTrue(id)
                .map(ProdutoCard::fromEntity)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto não encontrado ou indisponível."
                ));
    }
}