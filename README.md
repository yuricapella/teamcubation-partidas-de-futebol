# Teamcubation Partidas de Futebol

[![Java](https://img.shields.io/badge/java-21-blue?logo=java)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/spring%20boot-3.5.3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Tests](https://img.shields.io/badge/tests-passing-brightgreen)]()
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

API RESTful modular para gestão de partidas de futebol, clubes, estádios, ranking, retrospecto, integração RabbitMQ e jobs agendados.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Documentação detalhada](#documentação-detalhada)
- [Como contribuir](#como-contribuir)
- [Checklist de onboarding](#checklist-de-onboarding)
- [Licença](#licença)
- [Contato](#contato)
- [Links úteis](#links-úteis)

---

## Visão Geral

Projeto individual Java 21 + Spring Boot 3.5.3 com arquitetura multi-módulo Maven:
- `partidas-de-futebol-api`: API principal (clubes, partidas, estádios, ranking, retrospecto; consumers Rabbit).
- `partidas-de-futebol-mensageria`: Microserviço para produção de eventos RabbitMQ.
- Documentação Swagger/OpenAPI.
- Suporte a jobs agendados.
- Estrutura, convenções e anotações de domínio documentadas em arquivos separados.

> Consulte documentação completa de config, execução, mensageria, jobs, swagger e estrutura do projeto em [docs/](docs/).

---

## Funcionalidades

- CRUD completo para clubes, estádios e partidas, com validações (DTO & rules).
- Busca avançada: filtros (goleada, mandante, visitante, etc), paginação/ordenação, retrospecto (clubes, adversários, confrontos).
- Ranking dinâmico via enum, Strategy e endpoint parametrizado.
- Mensageria: producer (outro módulo) envia eventos de clube via RabbitMQ; API consome, processa e possui DLQ.
- Integração externa: consulta ViaCep para informações detalhadas de endereço nos estádios.
- Agendamento de jobs: cálculo e impressão de rankings via cron configs.
- Tratamento avançado de erros (`@ControllerAdvice`, exceptions customizadas).
- Documentação completa e exemplos detalhados pelo Swagger/OpenAPI.

---

## Documentação detalhada

- [Como executar e configurar o projeto](docs/como-executar.md)
- [Estrutura de pastas e arquivos do projeto](docs/estrutura.md)
- [Documentação Mensageria RabbitMQ](docs/rabbitmq.md)
- [Documentação de Jobs / Agendamentos](docs/jobs.md)
- [Documentação Swagger/OpenAPI e exemplos](docs/swagger.md)
- [Anotações de regras e domínios](docs/anotacoes-requisitos.md)
- [Anotações sobre testes](docs/anotacoes-teste.md)
- [Coleção Postman](postman/teamcubation-partidas-de-futebol.postman_collection.json)

---

## Como contribuir

1. Crie branches por feature ou correção (`feature/*`, `fix/*`)
2. Siga o padrão de commits [conventional commits](https://www.conventionalcommits.org/pt-br/v1.0.0/).
3. Sempre cubra novas lógicas com testes e anotações.
4. Atualize documentação Swagger para novos endpoints ou contratos.
5. Para detalhes de onboarding, leia [checklist de onboarding](#checklist-de-onboarding).

---

## Checklist de onboarding

- [ ] Clonar o projeto e rodar o build (`mvn clean install`)
- [ ] Subir dependências externas (RabbitMQ, banco)
- [ ] Configurar `application-dev.properties` conforme ambiente local
- [ ] Testar a API localmente, acessando o Swagger e executando operações CRUD
- [ ] Consultar anotações do domínio em `anotacoes.md`/`anotacoes-teste.md`
- [ ] Validar execução dos jobs (observar prints no console)
- [ ] Validar os contratos da mensageria, se necessário alterar/consumir eventos

---

## Licença

MIT (Veja o arquivo LICENSE).

---

## Contato

- [Yuri Capella] — [GITHUB](https://github.com/yuricapella)

---

## Links úteis

- [Swagger UI local](http://localhost:8080/swagger-ui.html)
- [RabbitMQ Management](http://localhost:15672)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Springdoc OpenAPI](https://springdoc.org/)
- [ModelMapper](https://modelmapper.org/)
- [Postman](https://www.postman.com/)

---
