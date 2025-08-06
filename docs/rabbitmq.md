# Documentação RabbitMQ – Partidas de Futebol

## Arquitetura RabbitMQ Multi-módulo

- **Producer:** `partidas-de-futebol-mensageria` (serviço separado)
    - Endpoint POST `/api/v1/clube/criar`
    - Recebe DTO, valida, envia evento para exchange configurada.
    - Publisher utiliza RabbitTemplate, configs via `RabbitMQExchangeClubeConfig`, properties externalizados.
    - Eventos padronizados com enums de status/type para interoperabilidade.

- **Consumer:** `partidas-de-futebol-api`
    - Consome fila principal e DLQ via @RabbitListener.
    - Procesa evento acionando service; falhas vão para DLQ.
    - Contrato de DTO/evento padronizado com o producer.
    - Configuração (filas, exchanges, binding, DLQ) totalmente externa.
    - Logging detalhado de consumo e erro

## Fluxo
1. POST para mensageria → evento produzido
2. Evento roda pela exchange/routing → fila principal
3. API consome e executa lógica de negócio (criação de clube)
4. Falhas vão para DLQ (Dead Letter Queue)

## Configuração
- Rotas, nomes de filas/exchanges/routing são externalizados nos properties das aplicações.

## Estrutura dos pacotes:
- Novos pacotes dedicados para config, controller, dto, producer, consumer, util de enums.

## Observações:
- Não há testes automatizados neste momento para a mensageria.
- Consulte `anotacoes-rabbit.md` e os fontes para mais detalhes técnicos e de código dos eventos, configs e consumers.

---