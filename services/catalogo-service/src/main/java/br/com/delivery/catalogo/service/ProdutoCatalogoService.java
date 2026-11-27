package br.com.delivery.catalogo.service;

import br.com.delivery.catalogo.api.dto.ProdutoCard;
import br.com.delivery.catalogo.domain.model.ProdutoConsulta;
import br.com.delivery.catalogo.exceptions.CatalogoInvalidoException;
import br.com.delivery.catalogo.exceptions.ProdutoNaoEncontradoException;
import br.com.delivery.catalogo.infrastructure.repository.ProdutoConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProdutoCatalogoService {

    private final ProdutoConsultaRepository repository;

    public List<ProdutoCard> listar(String busca) {

        List<ProdutoConsulta> produtos;

        if (busca == null || busca.isBlank()) {
            produtos = repository.findByDisponivelTrue();
        } else {
            produtos = repository
                    .findByDisponivelTrueAndNomeContainingIgnoreCase(
                            busca.trim()
                    );
        }

        List<ProdutoCard> resultado = new ArrayList<>();

        for (ProdutoConsulta produto : produtos) {
            resultado.add(paraCard(produto));
        }

        return resultado;
    }

    public ProdutoCard buscar(Long id)
            throws ProdutoNaoEncontradoException {

        ProdutoConsulta produto = repository
                .findByIdAndDisponivelTrue(id)
                .orElseThrow(() ->
                        new ProdutoNaoEncontradoException(
                                "Produto não encontrado ou indisponível"
                        )
                );

        return paraCard(produto);
    }

    @Transactional(rollbackFor = CatalogoInvalidoException.class)
    public void atualizarCatalogo(List<ProdutoConsulta> produtos)
            throws CatalogoInvalidoException {

        validarCatalogo(produtos);

        repository.deleteAllInBatch();

        repository.saveAllAndFlush(produtos);
    }

    private void validarCatalogo(List<ProdutoConsulta> produtos)
            throws CatalogoInvalidoException {

        if (produtos == null) {
            throw new CatalogoInvalidoException(
                    "Envie uma lista de produtos"
            );
        }

        Set<Long> idsRecebidos = new HashSet<>();

        for (ProdutoConsulta produto : produtos) {

            if (produto == null) {
                throw new CatalogoInvalidoException(
                        "A lista não pode conter um produto nulo"
                );
            }

            if (produto.getId() == null || produto.getId() <= 0) {
                throw new CatalogoInvalidoException(
                        "Cada produto precisa de um ID positivo"
                );
            }

            if (!idsRecebidos.add(produto.getId())) {
                throw new CatalogoInvalidoException(
                        "ID repetido na lista: " + produto.getId()
                );
            }

            if (produto.getNome() == null
                    || produto.getNome().isBlank()) {

                throw new CatalogoInvalidoException(
                        "Informe o nome do produto " + produto.getId()
                );
            }

            if (produto.getPrecoVenda() == null
                    || !Double.isFinite(produto.getPrecoVenda())
                    || produto.getPrecoVenda() < 0) {

                throw new CatalogoInvalidoException(
                        "Preço inválido para o produto "
                                + produto.getId()
                );
            }

            if (produto.getDisponivel() == null) {
                throw new CatalogoInvalidoException(
                        "Informe disponivel como true ou false "
                                + "no produto " + produto.getId()
                );
            }
        }
    }

    private ProdutoCard paraCard(ProdutoConsulta produto) {

        return new ProdutoCard(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPrecoVenda(),
                produto.getImagemUrl()
        );
    }
}