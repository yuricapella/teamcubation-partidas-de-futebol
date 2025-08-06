# Como executar o projeto

## Requisitos

- Java 21 ou superior
- Maven 3.8+
- RabbitMQ rodando localmente (ou via Docker)
- Banco de dados relacional (MySQL ou H2 embedded)

---

## Passo a passo

1. **Clone o projeto:**

    ```bash
    git clone https://github.com/seu-usuario/teamcubation_partidas_de_futebol.git
    cd teamcubation_partidas_de_futebol
    ```

2. **Suba dependências externas**

   Exemplo para subir o RabbitMQ com Docker (porta padrão 5672 e UI em 15672):

    ```bash
    docker run -d --hostname rabbit --name some-rabbit \
      -p 5672:5672 -p 15672:15672 rabbitmq:3-management
    ```

   Outros serviços como banco de dados podem ser configurados via Docker se necessário.

3. **Configure os arquivos de propriedades**

   Edite os arquivos `application-dev.properties` em cada módulo (`partidas-de-futebol-api` e `partidas-de-futebol-mensageria`), ajustando conforme seu ambiente:

    - URLs e credenciais do banco (`spring.datasource.url`, `username`, `password`)
    - Host e credenciais do RabbitMQ (`spring.rabbitmq.host`, `username`, `password`)
    - Cron dos jobs (exemplo: `job.calcular-ranking-diario.cron`, `job.teste-print.cron`)
    - Variáveis relacionadas a filas/routing para Rabbit

4. **Build do projeto**

    Rode o build completo na raiz com Maven:

    ```bash
    mvn clean install
    ```

5. **Inicie a API principal**

    Na pasta do módulo principal:

    ```bash
    cd partidas-de-futebol-api
    mvn spring-boot:run
    ```

    A API vai rodar em [http://localhost:8080](http://localhost:8080) por padrão.

6. **Inicie o serviço de mensageria**

    Em outro terminal:

    ```bash
    cd partidas-de-futebol-mensageria
    mvn spring-boot:run
    ```

    Mensageria roda, por padrão, em [http://localhost:8082](http://localhost:8082).

7. **Acesse o Swagger da API**

    - [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

8. **Exemplo básico de uso**

    Use o Swagger ou a [Coleção Postman](../postman/teamcubation-partidas-de-futebol.postman_collection.json) para testar CRUD de clubes, estádios, partidas, eventos Rabbit, buscas e rankings.

---

## Principais configurações (.properties)

| Propriedade                      | Descrição                                       |
| -------------------------------- |-------------------------------------------------|
| spring.rabbitmq.host             | Host do RabbitMQ                                |
| spring.rabbitmq.username         | Usuário RabbitMQ                                |
| spring.rabbitmq.password         | Senha RabbitMQ                                  |
| spring.datasource.url            | URL do banco de dados                           |
| spring.datasource.username       | Usuário do banco                                |
| spring.datasource.password       | Senha do banco                                  |
| job.teste-print.cron             | Cron para job de teste (ex: `*/10 * * * * *`)   |
| job.calcular-ranking-diario.cron | Cron para ranking diário (`0 0 0 * * *`)        |
| rabbitmq.exchange.clube          | Exchange do domínio clube                       |
| rabbitmq.routing-key.criar.clube | Routing key para criar clube                    |
| rabbitmq.queue.criar-clube       | Fila principal para criação de clube            |
| rabbitmq.queue.criar-clube.dlq   | Fila DLQ (Dead Letter) para criar clube         |

Consulte todos os arquivos properties dos módulos para detalhes e mais configurações.

---

## Observações

- A API usa Spring Data: a base pode ser MySQL ou H2; ajuste as configs no properties do módulo principal.
- Alguns jobs/logs podem ser vistos no console (especialmente os de ranking e teste).
- Mensageria usa RabbitMQ, por padrão em `localhost:5672` (exponha a porta se estiver usando docker).
- Para resetar, pare e remova containers/ramais conforme seu fluxo normal.

---
