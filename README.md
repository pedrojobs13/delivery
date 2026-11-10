# Delivery — Microserviços

Base inicial do trabalho da disciplina, desenvolvida com **Java 21**, **Spring Boot** e **Maven**.

> Esta etapa cria somente a fundação técnica. Catálogo utiliza dados temporários em memória e ainda não existem regras finais de negócio, persistência, autenticação ou gateway.

## Estrutura

```text
services/
  catalogo-service/  # Porta 8081
  pedidos-service/   # Porta 8082
postman/
  delivery-microservices.postman_collection.json
```

Os serviços permanecem independentes. O `pedidos-service` consulta o `catalogo-service` por HTTP para realizar uma pré-validação de pedido.

## Pré-requisitos

- JDK 21
- Maven 3.6.3 ou superior

## Executar localmente

Em um terminal, inicie o Catálogo:

```bash
mvn -pl services/catalogo-service spring-boot:run
```

Em outro terminal, inicie Pedidos:

```bash
mvn -pl services/pedidos-service spring-boot:run
```

## Endpoints iniciais

```http
GET http://localhost:8081/catalogo/produtos/1
```

```http
POST http://localhost:8082/pedidos/pre-validacoes
Content-Type: application/json

{
  "produtoId": 1,
  "quantidade": 2
}
```

Importe a coleção em `postman/delivery-microservices.postman_collection.json` para testar os dois endpoints.

## Próximos incrementos

1. Definir as regras reais do domínio de delivery.
2. Implementar persistência isolada para cada serviço.
3. Criar testes e tratamento de erros.
4. Avaliar gateway depois que a comunicação direta estiver validada.
