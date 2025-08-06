# Documentação Swagger/OpenAPI

Acesse via: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Recursos Documentados

- **Clubes:** cadastro, atualização, busca, inativação, validações por campo/documentação dos erros (201, 400, 404, 409).
- **Estádios:** CRUD completo + integração ViaCep, exemplos de entrada, resposta de erro.
- **Partidas:** criação, atualização, busca, deleção, filtros diversos (goleada, mandante/visitante), exemplos de sucesso/erro.
- **Ranking:** endpoint dinâmico por tipo, listas ordenadas/exemplo de saída, uso de enums detalhado.
- **Retrospecto:** geral, contra adversários, confrontos entre clubes, com exemplos de contratos e todos os cenários (com/vazio/erro).

## Destaques

- Respostas de sucesso e erro documentadas para cada endpoint, incluindo exemplos reais.
- Schemas dos DTOs anotados com @Schema/@ExampleObject
- Parâmetros/enums detalhados para que clientes criem integrações corretas
- Organização clara por tags: Clubes, Estádios, Partidas, Retrospecto, Ranking.
- Produces/consumes corretos nos controllers e exemplos parametrizados.

> Detalhes completos diretamente em sua instância Swagger.

---