# Anotações & Ideias Futuras

## Clube

### Pendências:

- [x] Possibilidade de criar um arquivo utilitário com todos os estados brasileiros e validar a existência antes de criar um clube.
  (Enum EstadosUF)

---

## 1. **Cadastrar clube (POST, CriarClubeRequestDTO)**

### Bean Validation a implementar (automático):

- [x] `@NotBlank(message = "O nome não pode estar vazio.")` para nome  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@Size(min = 2, message = "O nome tem que ter no minimo duas letras;")` para nome  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")` para nome  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@NotBlank(message = "A sigla do estado não pode estar vazia.")` para siglaEstado  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@Size(min = 2, max = 2, message = "A sigla do estado só pode ter 2 letras.")` para siglaEstado  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@NotNull(message = "Data de criação obrigatória")` para dataCriacao  
  _Arquivo: CriarClubeRequestDTO_
- [x] `@PastOrPresent(message = "A data de criação não pode ser futura")` para dataCriacao  
  _Arquivo: CriarClubeRequestDTO_


### Regras a validar manualmente (service/validator):

- [x] Sigla de estado brasileiro deve ser válida (`EstadoInexistenteException`)  
  _Método: criar; Arquivo: CriarClubeService, ClubeValidator_
- [x] Nome duplicado para o mesmo estado (`ClubeComNomeJaCadastradoNoEstadoException`)  
  _Método: criar; Arquivo: CriarClubeService, ClubeValidator_
- [x] (Demais campos já são cobertos pelo Bean Validation)
- Bean Validation cobre com @NotNull/@NotBlank, retorna 400 BAD REQUEST

---

## 2. Editar clube (PUT, AtualizarClubeRequestDTO)

### Bean Validation a implementar (automático):

- [x] `@NotBlank(message = "O nome não pode estar vazio.")` para nome
- [x] `@Size(min = 2, message = "O nome tem que ter no minimo duas letras;")` para nome
- [x] `@Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "O nome deve conter apenas letras e espaços")` para nome
- [x] `@NotBlank(message = "A sigla do estado não pode estar vazia.")` para siglaEstado
- [x] `@Size(min = 2, max = 2, message = "A sigla do estado só pode ter 2 letras.")` para siglaEstado
- [x] `@NotNull(message = "Data de criação obrigatória")` para dataCriacao
- [x] `@PastOrPresent(message = "A data de criação não pode ser futura")` para dataCriacao

### Regras a validar manualmente (service/validator):

- [x] Nome duplicado para o mesmo estado (`ClubeComNomeJaCadastradoNoEstadoException`)  
  _Método: atualizar; Arquivo: AtualizarClubeService, ClubeValidator_
- [x] Estado válido (`EstadoInexistenteException`)  
  _Método: atualizar; Arquivo: AtualizarClubeService, ClubeValidator_
- [ ] Data inválida: data criação não pode ser posterior a alguma partida já registrada (`DataCriacaoInvalidaException` ou `ConflitoDataClubeException`)  
  _Método: atualizar; Arquivo: AtualizarClubeService, ClubeValidator_
- [x] Clube não existe: _já tratado por buscarClubeService, retorna 404_
- [x] Dados mínimos: Bean Validation cobre
- [x] Demais cenários de exceção: já cobertos ou tratados por 404 da busca

---

## 3. **Inativar clube (DELETE)**

### Regras a validar manualmente (service):

- [x] Clube não existe: _já tratado por buscarClubeService, retorna 404_

---

## 4. **Buscar clube por ID (GET)**

### Regras a validar manualmente:

- [x] Sem resultado encontrado: _já tratado por buscarClubeService, retorna 404_

---

## 5. **Listar clubes (GET com filtros)**

### Regras a validar manualmente:

- [x] Sem resultado: retornar lista vazia com status 200 OK


---
## Partida

## 6. **Cadastrar partida (POST, CriarPartidaRequestDTO)**

### Pendências:

## Bean Validation a implementar (automático):
Metodo POST, criar partida request dto
- [x] @NotNull com mensagens:
  - "O clube mandante é obrigatório"
  - "O clube visitante é obrigatório"
  - "O estádio é obrigatório"
  - "A data e hora da partida são obrigatórias"
- [x] @PositiveOrZero com mensagens:
  - "O número de gols do mandante não pode ser negativo"
  - "O número de gols do visitante não pode ser negativo"
- [x] @PastOrPresent com mensagem:
  - "A data da partida não pode ser futura"

## Regras a validar manualmente (service/validator):

- [x] Mandante e visitante não podem ser o mesmo clube
- [x] Clube mandante e visitante devem existir
(Em buscar clube, pede para retornar 404 Not Found e para cadastrar partida pede 400 Bad request se nao houver clubes
Pesquisando boas praticas, coloquei o tratamento de exceção 404 not found como global e retornará isso mesmo ao invés de 400.)

- [x] Estádio deve existir
  (Mesmo caso do clube, retornará 404 not found)
- [x] Não permitir dataHora anterior à data de criação de qualquer clube envolvido
- [x] Clube envolvido não pode estar inativo
- [x] Clube não pode ter outra partida marcada com diferença menor que 48 horas
(adicionado metodo em PartidaRepository com a query para verificação no banco de dados,
como o banco de dados só retorna 1 ou 0 e nao boolean(true or false), coloquei o retorno como Long, se retornar 1, 
quer dizer que tem datas dentro de 48 horas e entao lança a exceção em PartidaValidator)

- [x] Estádio não pode ter outra partida cadastrada para o mesmo dia
(adiciona metodo em PartidaRepository que conta quantas partidas existem puxando pelo estadio_id e a data informada, 
se for maior que 0, a exceção é lançada em PartidaValidator)
---
## 7. Editar uma partida (PUT, AtualizarPartidaRequestDTO)
- [] adicionar service para atualizar
- [] adicionar dto e mapper

### Bean Validation a implementar (automático):
- [x] @NotNull clubes e estádio
- [x] @PositiveOrZero para golsMandante e golsVisitante
- [x] @NotNull e @PastOrPresent para dataHora

### Regras a validar manualmente (service/validator):
- [x] Clubes iguais (ClubesIguaisException)
- [x] Clubes e estádio devem existir (404, usar BuscarXService)
- [x] Data de criação do clube não pode ser posterior à data da partida (DataPartidaAnteriorACriacaoDoClubeException, 409)
- [x] Clube inativo (ClubeInativoException, 409)
- [x] Horários próximos entre partidas dos clubes (ClubesComPartidasEmHorarioMenorQue48HorasException, 409)
- [x] Estádio já possui partida no mesmo dia (EstadioJaPossuiPartidaNoMesmoDiaException, 409)
- [x] Partida não existe (PartidaNaoEncontradaException, 404)

---

## 8. Remover uma partida (DELETE)
- [x] adicionar service para deletar

### Bean Validation a implementar (automático):
- [x] Id da partida obrigatório (PathVariable na controller)

### Regras a validar manualmente (service/validator):
- [x] Partida não existe (PartidaNaoEncontradaException, 404 NOT FOUND)

---

## 9. Buscar uma partida (GET)
- [x] adicionar service para buscar

### Bean Validation a implementar (automático):
- [x] Id da partida obrigatório (PathVariable na controller)

### Regras a validar manualmente (service/validator):
- [x] Partida não existe (PartidaNaoEncontradaException, 404 NOT FOUND)

---

## Melhorias futuras:
Ao retornar a exceção ClubesComPartidasEmHorarioMenorQue48HorasException, 
listar as datas conflituosas dos clubes e calcular qual o tempo correto para mostrar ao usuario e facilitar o cadastro.

## Estrutura
A estrutura do projeto acabou ficando no modelo chamado Domain Package Structure (DPS), 
mas só descobri esse termo ao verificar boas praticas. 
Antes, eu organizava meus projetos dessa forma simplesmente porque achava mais prático e organizado 
para visualizar tudo de cada domínio em um só lugar do que colocar pastas com os nomes do dominio em cada package, 
service(clube,partida), repository(clube,partida) que seria o modelo DDD(Domain-driven Design).