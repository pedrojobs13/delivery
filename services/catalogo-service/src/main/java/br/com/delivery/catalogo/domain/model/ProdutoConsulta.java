package br.com.delivery.catalogo.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produto_consulta")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProdutoConsulta {

    @Id
    private Long id;

    private String nome;
    private String descricao;
    private Double precoVenda;
    private String imagemUrl;
    private Boolean disponivel;


}