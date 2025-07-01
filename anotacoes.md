# Anotações & Ideias Futuras

## Clube

### Pendências:

- [x] Possibilidade de criar um arquivo utilitário com todos os estados brasileiros e validar a existência antes de criar um clube.
  (Enum EstadosUF)


- [ ] Ao editar a data de criação do clube, impedir atualização se for definida uma data posterior à de alguma partida já registrada, retornando 409 CONFLICT.


- Observação: A validação deverá ser feita apenas quando houver partidas cadastradas para o clube.
- Possíveis soluções:
  - Adicionar uma lista de partidas no Clube (OneToMany), facilitando a checagem.
  - Manter a relação do lado da entidade Partida (com id do clube) e realizar a busca via repository para verificar se há partidas e suas datas.
---

A estrutura do projeto acabou ficando no modelo chamado Domain Package Structure (DPS), mas só descobri esse termo agora ao verificar boas praticas. Antes, eu organizava meus projetos dessa forma simplesmente porque achava mais prático e organizado para visualizar tudo de cada domínio em um só lugar do que colocar pastas com os nomes do dominio em cada package, service(clube,partida), repository(clube,partida) que seria o modelo DDD(Domain-driven Design).
