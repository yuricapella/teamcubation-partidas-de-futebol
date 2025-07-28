# Anotações & Ideias Futuras

# Clube

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
- [x] Data inválida: data criação não pode ser posterior a alguma partida já registrada (`DataCriacaoPosteriorDataPartidaException`)  
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
# Partida

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

## 11. Estádio (POST)

#### Requisito do projeto:
- [x] Permitir o cadastro de um estádio, exigindo obrigatoriamente o nome e o cep do estádio
- [x] Implementar método POST para cadastrar o estádio
- [x] Retornar status 201 CREATED no sucesso
- [x] Impedir cadastro e retornar 400 BAD REQUEST se o nome não for informado, tiver menos de 3 letras ou o cep estiver ausente ou em formato inválido
- [x] Impedir cadastro e retornar 409 CONFLICT se já existir um estádio cadastrado com o mesmo nome

#### **Descrição técnica**
Implementa o fluxo de cadastro de estádio, recebendo nome e cep obrigatórios via DTO, validando o nome (mínimo de 3 letras, obrigatório e único) e o formato do cep (apenas 8 dígitos numéricos). Realiza mapeamento do DTO para a entidade Estadio, persiste o estádio e, ao final, retorna um DTO de resposta contendo o nome do estádio e o endereço completo obtido dinamicamente a partir do cep, integrando-se à API ViaCep. Toda a lógica de validação e orquestração é centralizada nas camadas de service e mapper, e a controller permanece responsável apenas pelo recebimento da requisição e devolução da resposta.

#### **Métodos/Funções principais**
- **criarEstadio**
  - Recebe DTO com nome e cep
  - Valida nome obrigatório, mínimo de 3 letras e unicidade
  - Valida formato do cep (apenas 8 dígitos numéricos)
  - Mapeia DTO para entidade Estadio
  - Persiste Estadio no repositório
  - Consulta endereço via cep usando integração dedicada, enriquecendo o DTO de resposta com logradouro, bairro, localidade, uf e estado
  - Retorna DTO contendo nome do estádio e informações completas de endereço

#### **Principais argumentos, entradas e dependências**
- DTO de entrada: nome (String), cep (String)
- Dependências da service: repositório do estádio, validador de regras de negócio, mapper para conversão de entidade para DTO, integração para consulta ao ViaCep
- DTO de resposta: nome (String), cep, logradouro, bairro, localidade, uf, estado

#### **Checklist de implementação**
- [x] Validações de nome obrigatório, mínimo de 3 letras e unicidade
- [x] Validação de cep obrigatório e formato numérico de 8 dígitos
- [x] Mapeamento correto do DTO de input para entidade e desta para DTO de saída
- [x] Consulta do endereço via cep após persistência do estádio
- [x] Retorno estruturado de DTO com nome e endereço completo após criação
- [x] Centralização de lógica de negócio e integração externa em service
- [x] Controller responsável apenas por orquestrar entrada e saída

---

## 11 - extra. EnderecoViaCepClient (integração externa)

#### **Descrição técnica**
Encarregado de realizar a integração com a API ViaCep para buscar e mapear informações de endereço a partir do cep informado na criação do estádio. Sua função é retornar um DTO de endereço preenchido para utilização no fluxo de resposta da criação do estádio.

#### **Métodos/Funções principais**
- **buscarEndereco**
  - Recebe cep (String)
  - Executa chamada HTTP para a API ViaCep usando o valor informado
  - Mapeia resposta para CepResponseDTO com campos cep, logradouro, bairro, localidade, uf e estado

#### **Principais argumentos, entradas e dependências**
- Parâmetro: cep (String)
- Uso de RestTemplate para requisição REST
- Variável de ambiente/property para configurar URL da API ViaCep
- Retorno: CepResponseDTO com dados completos de endereço

#### **Checklist de implementação**
- [x] Consulta dinâmica ao ViaCep
- [x] Mapeamento seguro da resposta para DTO interno
- [x] Isolamento da lógica de integração externa fora da controller/service principal
---

## 12. Editar um estádio (PUT)

- [x] Adicionar service para atualização de estádio
- [x] Adicionar DTO e mapper para atualização
- [x] Adicionar método PUT na controller para editar estádio
- [x] Validar nome mínimo de 3 letras (400 BAD REQUEST)
- [x] Validar nome único (409 CONFLICT)
- [x] Validar se estádio existe (404 NOT FOUND)
---

## 13. Estádio (GET por ID)

#### Requisito do projeto:
- [x] Permitir buscar um estádio pelo seu ID
- [x] Implementar método GET para consulta por ID
- [x] Retornar status 200 OK no sucesso
- [x] Retornar 404 NOT FOUND no caso de estádio não existente

#### **Descrição técnica**
Implementa a busca detalhada de estádio por ID, retornando um DTO com nome do estádio e os dados completos de endereço (cep, logradouro, bairro, localidade, uf, estado). O fluxo realiza a validação da existência do estádio, retorna 404 em caso de ausência e, em caso positivo, consulta a API ViaCep para enriquecer o DTO com o endereço obtido dinamicamente. Para estádios legados que não possuem cep cadastrado, o endpoint retorna todos os campos de endereço como string vazia, preservando o padrão de resposta.

#### **Métodos/Funções principais**
- **buscarEstadioComEnderecoPorId**
  - Recebe ID
  - Consulta o repositório pela entidade Estadio
  - Lança exceção customizada retornando 404 NOT FOUND se não encontrar o estádio
  - Se o estádio não possuir cep cadastrado, retorna DTO com campos de endereço vazios
  - Se houver cep, consulta ViaCep e preenche DTO detalhado

#### **Principais argumentos, entradas e dependências**
- PathVariable: id (Long)
- Dependências: BuscarEstadioService, EstadioRepository, EnderecoViaCepClient
- DTO de resposta: EstadioEnderecoResponseDTO (nome, CepResponseDTO: cep, logradouro, bairro, localidade, uf, estado)
- Exceção personalizada EstadioNaoEncontradoException para controle de status 404

#### **Checklist de implementação**
- [x] Consulta detalhada de estádio por ID com integração dinâmica ao ViaCep
- [x] Resposta OK com DTO enriquecido apenas para estádios existentes
- [x] Retorno com endereço vazio para estádios sem cep cadastrado
- [x] Retorno de 404 NOT FOUND para id inexistente
- [x] Isolamento da lógica de externalização de endereço na service
- [x] Controller enxuta, apenas direcionando as respostas e exceções
---

## 13 - extra. EnderecoViaCepClient (centralização da montagem do DTO detalhado na busca por estádio)

#### **Descrição técnica**
A EnderecoViaCepClient passa a ser responsável não apenas por integrar com o ViaCep, mas por centralizar toda a montagem do DTO EstadioEnderecoResponseDTO a partir dos campos nome e cep, no fluxo de busca detalhada de estádio. Isso garante que a lógica de validação de cep nulo/vazio e a produção da resposta enriquecida ou de campos vazios fique isolada nesta service, simplificando a lógica das demais camadas e mantendo consistência na resposta detalhada.

#### **Métodos/Funções principais**
- **criarEstadioEndereco**
  - Recebe nome e cep como parâmetros
  - Se o cep for nulo ou vazio, monta e retorna EstadioEnderecoResponseDTO com todos os campos de endereço em branco
  - Se o cep existe e é válido, consulta ViaCep, mapeia resposta em CepResponseDTO e monta o EstadioEnderecoResponseDTO enriquecido

#### **Principais argumentos, entradas e dependências**
- Parâmetros: nome (String), cep (String)
- Objetos processados: EstadioEnderecoResponseDTO, CepResponseDTO
- Nenhuma lógica de fallback ou tratamento posterior precisa ser implementada nas services ou controllers que utilizam EnderecoViaCepClient

#### **Checklist de implementação**
- [x] Centraliza montagem da resposta detalhada de estádio (EstadioEnderecoResponseDTO) na service EnderecoViaCepClient
- [x] Simplifica a BuscarEstadioService, que apenas delega a produção do DTO ao client
- [x] Garante tratamento uniforme de cep nulo/vazio e de integração com ViaCep
---

---

## 14. Listar estádios (GET)

- [x] Adicionar service para listagem de estádios com paginação
- [x] Adicionar método GET na controller para listar estádios
- [x] Permitir paginação e ordenação por nome
- [x] Sem resultado: retornar lista vazia com status 200 OK

---

# Busca Avançada 1: Retrospecto Geral de um Clube (GET)

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

# Busca Avançada 2: Retrospecto contra adversários (GET)

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

# 3. Busca Avançada 3: Confrontos Diretos (GET)

### **Descrição técnica**
Implementado endpoint para retornar todas as partidas entre dois clubes específicos (confronto direto).  
Além da lista de partidas, são retornados os retrospectos de cada time no confronto, incluindo:
- Nome e dados resumidos do clube
- Número total de jogos entre os dois clubes
- Vitórias, empates e derrotas de cada clube neste confronto
- Gols feitos e gols sofridos por cada clube neste confronto

O resultado entregue é um objeto JSON contendo uma lista de retrospectos (um para cada clube) e uma lista de partidas correspondentes ao confronto.

### **Funcionalidade/Endpoint**
- **Método:** GET
- **Path:** `/api/clube/{idClube}/confronto/{idAdversario}`
- **Request:**  
  Recebe os IDs de ambos os clubes via path variables na URL.
- **Exemplo de resposta:**
  ```json
  {
    "retrospectos": [
      {
        "clube": {
          "nome": "clube de time atualizado",
          "siglaEstado": "AM",
          "dataCriacao": "2025-05-13"
        },
        "jogos": 2,
        "vitorias": 1,
        "derrotas": 0,
        "empates": 1,
        "golsFeitos": 5,
        "golsSofridos": 4
      },
      {
        "clube": {
          "nome": "clube de time",
          "siglaEstado": "AP",
          "dataCriacao": "2025-05-13"
        },
        "jogos": 2,
        "vitorias": 0,
        "derrotas": 1,
        "empates": 1,
        "golsFeitos": 4,
        "golsSofridos": 5
      }
    ],
    "partidas": [
      {
        "clubeMandante": { ... },
        "clubeVisitante": { ... },
        "estadio": { ... },
        "golsMandante": 2,
        "golsVisitante": 2,
        "dataHora": "10/06/2025 21:00:00",
        "resultado": "EMPATE"
      },
      {
        "clubeMandante": { ... },
        "clubeVisitante": { ... },
        "estadio": { ... },
        "golsMandante": 3,
        "golsVisitante": 2,
        "dataHora": "05/06/2025 21:00:00",
        "resultado": "VITORIA_MANDANTE"
      }
    ]
  }
  ```

### **Cenários tratados**
- Se algum dos clubes não existir, retorna 404 NOT FOUND.
- Se não houver confrontos entre os clubes, retorna lista de partidas vazia e retrospectos zerados para ambos, status 200 OK.

### **Checklist do que foi implementado:**
- [x] Recebe os IDs de ambos os clubes via path variable na URL
- [x] Valida existência dos clubes, gerando 404 se necessário
- [x] Consulta todas as partidas entre os dois clubes, independentemente de quem foi mandante ou visitante
- [x] Calcula, para cada clube, o retrospecto no confronto — incluindo vitórias, empates, derrotas, gols feitos e sofridos
- [x] Exibe ambos retrospectos junto à lista de partidas na resposta
- [x] Retorna corretamente lista de partidas vazia e retrospectos zerados se não houver confrontos

---

# Busca Avançada 4: Ranking - Total de Pontos (GET)

### **Descrição técnica**
Implementado endpoint para retornar o **ranking dos clubes** com base no total de pontos (vitória = 3, empate = 1).  
Utiliza enum `TipoRanking` para selecionar o critério. A abordagem segue o padrão Strategy, facilitando evolução para novos tipos de ranking.

O ranking mostra:
- Nome e estado do clube
- Total de pontos (calculado: vitórias × 3 + empates)
- Lista é ordenada do maior para o menor número de pontos
- Clubes com zero pontos são excluídos do resultado final

### **Funcionalidade/Endpoint**
- **Método:** GET
- **Path:** `/api/clube/ranking?tipoRanking=TOTAL_PONTOS`
- **Request:**  
  Exemplo de chamada:
  ```
  GET http://localhost:8080/api/clube/ranking?tipoRanking=TOTAL_PONTOS
  ```
- **Exemplo de resposta:**
  ```json
  [
    {
      "nomeClube": "clube de time atualizado",
      "estadoClube": "AM",
      "total": 4
    },
    {
      "nomeClube": "aclube de time",
      "estadoClube": "AP",
      "total": 3
    },
    {
      "nomeClube": "clube de time criado",
      "estadoClube": "SC",
      "total": 3
    },
    {
      "nomeClube": "clube de time criado",
      "estadoClube": "SP",
      "total": 3
    },
    {
      "nomeClube": "clube de time oito",
      "estadoClube": "SP",
      "total": 3
    },
    {
      "nomeClube": "clube de time",
      "estadoClube": "AP",
      "total": 1
    }
  ]
  ```

### **Cenários tratados**
- Endpoint retorna 400 BAD_REQUEST se tipo de ranking informado não existir no enum
- Se não houver clubes com pontos, retorna lista vazia e status 200 OK
- Retorna lista ordenada por total de pontos, excluindo clubes zerados

### **Checklist do que foi implementado**
- [x] Criação do enum `TipoRanking` para limitar e padronizar opções de ranking recebidas pela API
- [x] Implementação do padrão Strategy para cálculo de rankings, facilitando extensão para outros tipos no futuro
- [x] Classe abstrata genérica para rankings, evitando repetição de atributos e métodos
- [x] Classe filha para ranking de pontos com o cálculo apropriado e herança da estrutura base
- [x] Service central polimórfico, que orquestra escolha dinâmica da strategy conforme o tipo solicitado
- [x] Controller GET usando `@RequestParam` para receber o tipo de ranking, em conformidade com o padrão REST
- [x] Tratamento de exceção para tipos de ranking inválidos, seguindo padronização de respostas HTTP

---

# Busca Avançada 4: Ranking - Total de Gols (GET)

### **Descrição técnica**
Implementado endpoint para retornar o **ranking dos clubes** com base no **total de gols marcados**.

- O ranking exibe nome e estado do clube e o total de gols feitos.
- Clubes com zero gols não aparecem na lista.
- A lista é ordenada do maior para o menor número de gols.

### **Como chamar**
- **Endpoint:** `GET /api/clube/ranking?tipoRanking=TOTAL_GOLS`

### **Checklist de implementação**

- [x] Implementada classe filha `RankingGols` com lógica de cálculo no atributo total (baseada em gols marcados)
- [x] Implementada `RankingGolsStrategy` para cálculo e ordenação do tipo TOTAL_GOLS
- [x] Endpoint e resposta seguem padrão já estabelecido nos rankings anteriores

---

# Busca Avançada 4: Ranking - Total de Vitórias (GET)

### **Descrição técnica**
Implementado endpoint para retornar o **ranking dos clubes** baseado no **total de vitórias**.

- O ranking mostra nome e estado do clube, e o total de vitórias.
- Clubes sem vitórias são excluídos da lista.
- Ordenação do maior para o menor total de vitórias.

### **Como chamar**
- **Endpoint:** `GET /api/clube/ranking?tipoRanking=TOTAL_VITORIAS`

### **Checklist de implementação**
- [x] Inclusão da classe filha `RankingVitorias` para calcular o total de vitórias do clube
- [x] Inclusão da strategy `RankingVitoriasStrategy` para esse tipo de ranking
- [x] Endpoint e resposta seguem padrão já estabelecido nos rankings anteriores

---

# Busca Avançada 4: Ranking - Total de Jogos (GET)

### **Descrição técnica**
Implementado endpoint para retornar o **ranking dos clubes** por **total de jogos disputados**.

- O ranking mostra nome e estado do clube, e o total de jogos.
- Clubes que não jogaram são excluídos da lista.
- Ordenação do maior para o menor número de jogos disputados.

### **Como chamar**
- **Endpoint:** `GET /api/clube/ranking?tipoRanking=TOTAL_JOGOS`

### **Checklist de implementação**
- [x] Inclusão da classe filha `RankingJogos` para calcular o total de jogos do clube
- [x] Inclusão da strategy `RankingJogosStrategy` para esse tipo de ranking
- [x] Endpoint e resposta seguem padrão já estabelecido nos rankings anteriores

---

# Filtro Avançado (extra): Goleadas (GET)

### **Descrição técnica**
Implementado filtro opcional para goleadas nos endpoints de listagem de partidas.  
Quando o parâmetro `goleada` é especificado na requisição, a API retorna apenas partidas em que a diferença de gols entre os clubes é igual ou superior a 3, conforme a definição do requisito.

### **Como chamar**
- **Endpoint:** `GET /api/partida?goleada=true`
- Também pode ser combinado com outros filtros, como clube e estádio:
  - `GET /api/partida?clubeId=1&goleada=true`
  - `GET /api/partida?estadioId=5&goleada=true`

### **Exemplo de resposta**
```json
{
  "content": [
    {
      "clubeMandante": { "nome": "...", ... },
      "clubeVisitante": { "nome": "...", ... },
      "estadio": { "nome": "..." },
      "golsMandante": 1,
      "golsVisitante": 4,
      "dataHora": "15/06/2025 21:00:00",
      "resultado": "VITORIA_VISITANTE"
    },
    ...
  ],
  "totalElements": 3,
  "totalPages": 1,
  // ... demais campos de paginação
}
```

### **Cenários tratados**
- Quando `goleada=true`, retorna apenas partidas onde a diferença de gols é >= 3
- Requisições sem o filtro retornam todas as partidas normalmente (com ou sem goleada)
- Pode ser combinado com outros filtros já existentes do endpoint
- Em caso de nenhuma partida encontrada, retorna lista vazia e status 200 OK

### **Checklist de implementação**
- [x] Adicionado parâmetro booleano `goleada` no endpoint de listagem de partidas
- [x] Implementado método/calculo para identificar partidas com diferença de gols >= 3
- [x] Aplicado filtro na service após busca das partidas
- [x] Mantido padrão de resposta paginada do projeto

---

# Filtro Avançado 2: Mandantes e Visitantes (GET)

### **Descrição técnica**
Implementado filtro opcional para partidas em que o clube jogou como mandante ou visitante nos endpoints que retornam listas de partidas ou retrospectos. O usuário pode aplicar o filtro adicionando os parâmetros `mandante` e/ou `visitante` à requisição.

### **Como chamar**
- **Endpoint exemplos:**
  - `/api/partida/buscar?clubeId=3&mandante=true`
  - `/api/clube/{id}/retrospecto?mandante=true`
  - `/api/clube/{id}/retrospectos-adversarios?visitante=true`

### **Cenários tratados**
- Filtra resultados para retornar apenas partidas em que o clube foi mandante ou visitante, conforme o parâmetro informado.
- Parâmetros `mandante` e `visitante` são opcionais; se não informados, retorna todas as partidas/retrospectos normalmente.
- Parâmetros podem ser combinados com os demais filtros já existentes.

### **Checklist de implementação**
- [x] Adicionados parâmetros opcionais `mandante` e `visitante` como Boolean nos endpoints de listagem/retrospectos
- [x] Lógica de filtragem aplicada na service para considerar apenas partidas conforme o filtro selecionado
- [x] Criação de método utilitário para evitar duplicação de lógica de filtragem
- [x] Padronização das respostas e manutenção da compatibilidade com os outros filtros e paginação

---


## Pendencias e Melhorias futuras:
- [x] Possibilidade de criar um arquivo utilitário com todos os estados brasileiros e validar a existência antes de criar um clube.
  (Enum EstadosUF)

- [] Ao retornar a exceção ClubesComPartidasEmHorarioMenorQue48HorasException, 
listar as datas conflituosas dos clubes e calcular qual o tempo correto para mostrar ao usuario e facilitar o cadastro.

- [] Tentar usar polimorfismo com interface nos dtos de clube e partida para deixar os metodos dos Validators mais genéricos

- [x] alterar o get confronto de retrospecto para usar requestparam e nao request body, pois foge do padrao rest
- Agora utiliza     @GetMapping("/{idClube}/confronto/{idAdversario}")

- [] colocar um filtro de % % nos repository por nome por exemplo, para poder filtrar todos clubes/estadios com nomes parecidos.

- [] adicionar validação na service de estadio que valida se o cep realmente existe, camada dto será apenas para validar estrutura do cep.
- 

## Estrutura
A estrutura do projeto acabou ficando no modelo chamado Domain Package Structure (DPS), 
mas só descobri esse termo ao verificar boas praticas. 
Antes, eu organizava meus projetos dessa forma simplesmente porque achava mais prático e organizado 
para visualizar tudo de cada domínio em um só lugar do que colocar pastas com os nomes do dominio em cada package, 
service(clube,partida), repository(clube,partida) que seria o modelo DDD(Domain-driven Design).

## Comentarios:
### Busca avançada 4 rankings
Minha ideia inicial era fazer um método genérico que recebesse uma String "tipo" e usasse um switch para cada tipo chamar o método privado correspondente.
Optei por evoluir para o padrão Strategy: cada novo ranking cria uma nova strategy e implementa a lógica, sem mexer no código legado.
O fluxo ficou:
Classe Ranking é pai com informações genéricas e cálculo abstrato;
Cada filha (RankingPontos, etc) implementa a lógica de cálculo de total;
Cada strategy (RankingPontosStrategy, etc) usa polimorfismo para calcular o ranking;
Service central orquestra tudo, mantendo código limpo e extensível.
Essa abordagem torna o código mais complexo inicialmente, porém altamente extensível e de fácil manutenção a longo prazo.