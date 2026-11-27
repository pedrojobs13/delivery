# Delivery — Microserviços

Base inicial do trabalho da disciplina, desenvolvida com **Java 21**, **Spring Boot** e **Maven**.

## Estrutura

```text
services/
  catalogo-service/  # Porta 8081 (SQLite encapsulado em services/catalogo-service/catalogo.db)
  pedidos-service/   # Porta 8082 (SQLite encapsulado em services/pedidos-service/pedido.db)
postman/
  delivery-microservices.postman_collection.json
```

Os serviços operam de forma desacoplada seguindo o padrão *Database per Service*. O `pedidos-service` consulta o `catalogo-service` via HTTP (`CatalogoClient`).

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

> **Isolamento de Banco SQLite:** O plugin `spring-boot-maven-plugin` está configurado com `workingDirectory` no diretório de cada módulo. Dessa forma, mesmo iniciando a partir da raiz com `-pl`, o banco de dados SQLite de cada serviço é mantido estritamente encapsulado dentro do seu próprio diretório.

## Endpoints

### 1. Catálogo (`http://localhost:8081`)

* `GET /catalogo/produtos/{id}`: Consulta dados de um produto no catálogo.

### 2. Pedidos (`http://localhost:8082`)

* `POST /pedidos/pre-validacoes`: Pré-valida disponibilidade do item no catálogo.
* `POST /pedidos`: Cria um novo pedido com status inicial `PENDENTE` e `pago = false`.
* `POST /pedidos/{id}/pagar`: Simula o pagamento do pedido (ex: `PIX`, `CARTAO_CREDITO`, `CARTAO_DEBITO`, `DINHEIRO`). Avança o status do pedido de `PENDENTE` para `CONFIRMADO` e define `pago = true` com timestamp `pagoEm`.
* `GET /pedidos/{id}`: Consulta os detalhes do pedido por ID.
* `GET /pedidos`: Lista pedidos com suporte a filtros opcionais por `status` e `pago` (ex: `/pedidos?status=CONFIRMADO&pago=true`).

A coleção completa para testes está disponível em `postman/delivery-microservices.postman_collection.json`.
