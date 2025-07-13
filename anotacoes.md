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
- [x] adicionar service para atualizar
- [x] adicionar dto e mapper
- [x] Em PartidaValidator no metodo validarAtualizacaoDePartidas adiciona if para impedir 
verificação de dados que não foram atualizados e lançar exceção desnecessária.


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
- [x] Id da partida obrigatório (PathVariable na controller)

### Regras a validar manualmente (service/validator):
- [x] Partida não existe (PartidaNaoEncontradaException, 404 NOT FOUND)

---

## 9. Buscar uma partida (GET)
- [x] adicionar service para buscar
- [x] Id da partida obrigatório (PathVariable na controller)

### Regras a validar manualmente (service/validator):
- [x] Partida não existe (PartidaNaoEncontradaException, 404 NOT FOUND)

---

## 10. Listar partidas (GET)

- [x] Adicionar service para listar partidas 
- [x] adicionar metodo para listar partidas com filtros e paginação
- [x] Adicionar método GET na controller para listar partidas
- [x] Permitir filtrar por clube e estádio via parâmetros opcionais
- [x] Parâmetros de paginação/ordenação (`page`, `size`, `sort`) tratados automaticamente pelo Spring Data
- [x] Sem resultado: deve retornar lista vazia com status 200 OK (não é exceção, padrão de API REST)

---

## 11. Cadastrar um estádio (POST)

- [x] Adicionar service para cadastro de estádio
- [x] Adicionar DTO e mapper para cadastro
- [x] Adicionar método POST na controller para criar estádio
- [x] Validar nome obrigatório e mínimo de 3 letras (400 BAD REQUEST)
- [x] Validar nome único (409 CONFLICT)

---

## 12. Editar um estádio (PUT)

- [x] Adicionar service para atualização de estádio
- [x] Adicionar DTO e mapper para atualização
- [x] Adicionar método PUT na controller para editar estádio
- [x] Validar nome mínimo de 3 letras (400 BAD REQUEST)
- [x] Validar nome único (409 CONFLICT)
- [x] Validar se estádio existe (404 NOT FOUND)

---

## 13. Buscar um estádio (GET)

- [x] Adicionar service para busca de estádio por id
- [x] Adicionar método GET na controller para buscar estádio por id
- [x] Validar se estádio existe (404 NOT FOUND)

---

## 14. Listar estádios (GET)

- [x] Adicionar service para listagem de estádios com paginação
- [x] Adicionar método GET na controller para listar estádios
- [x] Permitir paginação e ordenação por nome
- [x] Sem resultado: retornar lista vazia com status 200 OK

---

## Busca Avançada 1: Retrospecto Geral de um Clube (GET)

### **Descrição técnica**
Implementado endpoint para retornar o retrospecto geral de um clube, somando **todas as partidas** 
que o clube participou (seja como mandante, seja como visitante).  
A resposta apresenta:
- Dados resumidos do clube
- Total de vitórias
- Empates
- Derrotas
- Gols feitos
- Gols sofridos

### **Funcionalidade/Endpoint**
- **Método:** GET
- **Path:** `/api/clube/{id}/retrospecto`
- **Exemplo de resposta:**
  ```json
  {
    "clube": {
      "nome": "clube de time atualizado",
      "siglaEstado": "AM",
      "dataCriacao": "2025-05-13"
    },
    "vitorias": 2,
    "derrotas": 1,
    "empates": 1,
    "golsFeitos": 7,
    "golsSofridos": 5
  }
  ```

### **Cenários tratados**
- Se o clube não existe, retorna 404 NOT FOUND.
- Se o clube existe mas não possui partidas, retorna todos os valores zerados e status 200 OK.

### **Checklist de Implementação**
- [x] Busca por ID do clube com validação de existência e erro 404
- [x] Recuperação de todas as partidas do clube (como mandante ou visitante)
- [x] Cálculo de vitórias, empates, derrotas, gols feitos e sofridos, considerando o papel do clube em cada partida
- [x] Uso de DTO (ClubeResponseDTO) para expor somente dados necessários do clube
- [x] Resposta única (não lista), com todos os campos zerados caso o clube não tenha partidas
- [x] Chamada da service pelo controller com endpoint RESTful, padrão status code e resposta JSON

---

## Busca Avançada 2: Retrospecto contra adversários (GET)

### **Descrição técnica**
Implementado endpoint para retornar o retrospecto de um clube contra **cada um** de seus adversários já enfrentados.  
Para cada adversário, são calculados:
- Nome do adversário
- Estado do adversário
- Total de partidas (jogos)
- Vitórias
- Derrotas
- Empates
- Gols feitos
- Gols sofridos

O resultado entregue é um objeto JSON com o nome do clube central, o estado e uma **lista** de retrospectos, cada um apontando para um adversário diferente.

### **Funcionalidade/Endpoint**
- **Endpoint:**  
  `GET /api/clube/{id}/retrospectos-adversarios`
- **Response:**
  ```json
  {
    "nomeClube": "clube de time atualizado",
    "estadoClube": "SP",
    "retrospectoContraAdversarios": [
      {
        "nomeAdversario": "clube de time",
        "estadoAdversario": "SP",
        "jogos": 2,
        "vitorias": 1,
        "derrotas": 0,
        "empates": 1,
        "golsFeitos": 5,
        "golsSofridos": 4
      },
      ...
    ]
  }
  ```

### **Cenários tratados**
- Se o clube não existir, retorna 404 NOT FOUND.
- Se o clube existir mas não tiver partidas, a lista `"retrospectoContraAdversarios"` é **vazia**, com status 200 OK.

### **Checklist do que foi implementado:**

- [x] Busca por ID do clube, validando existência antes de processar.
- [x] Recuperação de todas as partidas em que o clube foi mandante ou visitante.
- [x] Agrupamento automático por adversário utilizando Streams/Map.
- [x] Para cada adversário, cálculo do retrospecto (jogos, estatísticas e agora também o estado(pois tem nomes repetidos de clubes)).
- [x] Construção do DTO de resposta: nome do clube, estado do clube, lista de retrospectos com nome e estado do adversário.
- [x] Retorno de lista vazia e status 200 OK se não houver jogos; 404 se clube não existe.

---


## Melhorias futuras:
- [] Ao retornar a exceção ClubesComPartidasEmHorarioMenorQue48HorasException, 
listar as datas conflituosas dos clubes e calcular qual o tempo correto para mostrar ao usuario e facilitar o cadastro.

- [] Tentar usar polimorfismo com interface nos dtos de clube e partida para deixar os metodos dos Validators mais genéricos

## Estrutura
A estrutura do projeto acabou ficando no modelo chamado Domain Package Structure (DPS), 
mas só descobri esse termo ao verificar boas praticas. 
Antes, eu organizava meus projetos dessa forma simplesmente porque achava mais prático e organizado 
para visualizar tudo de cada domínio em um só lugar do que colocar pastas com os nomes do dominio em cada package, 
service(clube,partida), repository(clube,partida) que seria o modelo DDD(Domain-driven Design).