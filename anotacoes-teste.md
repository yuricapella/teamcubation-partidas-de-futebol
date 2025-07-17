Com seu exemplo de código e modelo de documentação, aqui está como documentar **de forma sucinta, objetiva e focada só no código, argumentos e lógica** (e não em como rodar/comandos):

---

# Anotações

## Clube

### 1. **Buscar clube controller (GET - todos com Page, por id, e exceção)**

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável por buscar clubes de futebol.  
A classe `BuscarClubeApiControllerTest` cobre os cenários principais de busca paginada (com filtros opcionais na query), busca por id e cenário de exceção (clube não encontrado).

#### **Métodos de teste**
- `deveBuscarTodosClubesComSucesso(String nome, String estado, Boolean ativo)`
    - Testa o endpoint de busca paginada de clubes (`/api/clube/buscar`)
    - Utiliza argumentos opcionais (`nome`, `estado`, `ativo`) via `@CsvSource`, incluindo variações com filtros nulos ou preenchidos.
    - Espera resposta em formato paginado (Page), verifica tamanho da lista retornada e campos específicos do DTO via `jsonPath`.

- `deveBuscarClubePorIdComSucesso()`
    - Testa o endpoint de busca por id (`/api/clube/buscar/{id}`).
    - Argumento: `id` do clube a ser buscado.
    - Simula retorno do clube via mock do service, verifica os campos do DTO no JSON de resposta (incluindo serialização da data em array).

- `deveRetornarNotFoundQuandoClubeNaoExistir()`
    - Testa o cenário de exceção ao buscar clube por id inexistente.
    - Argumento: `id` inexistente.
    - Simula exceção no service (`ClubeNaoEncontradoException`).
    - Valida o status HTTP 404 retornado.

#### **Principais argumentos e dependências**
- **Controllers/Endpoints testados:**
    - `/api/clube/buscar [GET]` (paginado, filtros opcionais)
    - `/api/clube/buscar/{id} [GET]`
- **Parâmetros opcionais:** `nome`, `estado`, `ativo`, além de parâmetros de paginação (`page`, `size`)
- **Resolução de Pageable** via `PageableHandlerMethodArgumentResolver` no setup do MockMvc
- **Uso de `any()` nos argumentos do Mockito** para abranger todos os cenários de filtros
- **DTO de resposta**: `ClubeResponseDTO` com campos `nome`, `siglaEstado`, `dataCriacao`
- **Verificação detalhada do JSON** usando `jsonPath`, especialmente para datas
- **Mock dos services** com Mockito para simular cenários de sucesso e exceção
- **ControllerAdvice** incluído no setup para tratamento global e específico de exceções de clube

#### **Checklist de implementação**
- [x] Testes para busca paginada com filtros opcionais e parâmetros de página.
- [x] Teste para busca por id (sucesso).
- [x] Teste para retorno de exceção 404 (clube não encontrado).
- [x] Todos os campos relevantes do DTO verificados no JSON (`nome`, `siglaEstado`, `dataCriacao` como array).
- [x] Uso de lista mutável (`new ArrayList<>`) no PageImpl, garantindo serialização correta com Jackson.
- [x] Setup do MockMvc inclui ControllerAdvices e ArgumentResolver para Pageable.
- [x] Impressão do início de cada teste via `PrintUtil`.

---

## 2. Inativar clube controller (DELETE - sucesso e exceção)

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável por inativar (desativar) um clube de futebol.  
A classe `InativarClubeApiControllerTest` cobre o cenário de sucesso e o cenário de exceção (clube inexistente), garantindo que a camada controller responde corretamente de acordo com o contrato HTTP especificado.

#### **Métodos de teste**
- `deveInativarClubePorIdComSucessoERetornarNoContent()`
  - Testa o endpoint de inativação (`/api/clube/inativar/{id}`) com um id válido.
  - Mocka o service para realizar a operação sem erro (`doNothing()`).
  - Verifica que o status retornado é 204 (NO_CONTENT) e que o método do service foi chamado.

- `deveRetornarNotFoundAoInativarClubeInexistente()`
  - Testa o cenário onde se tenta inativar um clube com id inexistente.
  - Mocka o service para lançar a exceção `ClubeNaoEncontradoException`.
  - Espera o status HTTP 404 (NOT_FOUND) e verifica a chamada correta do método do service.

#### **Principais argumentos e dependências**
- **Controller/Endpoint testado:**
  - `/api/clube/inativar/{id}` [DELETE]
- **Mocks do service:** Uso de `doNothing().when(...)` para simular sucesso e `doThrow(...).when(...)` para simular exceção.
- **ControllerAdvice** incluído no MockMvc para tratamento global e específico das exceções.
- **Verificação do status HTTP**: 204 para sucesso, 404 para caso de clube inexistente.
- **Mockito.verify** para confirmar a chamada ao método do service com o id correto.
- **Impressão do início de cada teste** via `PrintUtil` para facilitar rastreabilidade.

#### **Checklist de implementação**
- [x] Teste para DELETE de sucesso com status 204 NO_CONTENT.
- [x] Teste para exceção de clube não encontrado retornando status 404 NOT_FOUND.
- [x] Controle preciso de mocks para todos os retornos (sucesso e exceção).
- [x] Setup do MockMvc inclui ControllerAdvices para tratamento correto das respostas de erro.
- [x] Verificação se o método do service responsável foi chamado exatamente uma vez.
- [x] Impressão informativa do início de cada teste com `PrintUtil`.


---

## 3. Atualizar clube controller (PUT - sucesso, validação, erros de negócio)

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável pela atualização dos dados do clube de futebol.  
A classe `AtualizarClubeApiControllerTest` cobre os principais cenários de atualização: fluxo feliz, erros de validação de DTO, exceção de não encontrado e exceções de regras de negócio customizadas do domínio.

#### **Métodos de teste**
- `deveAtualizarClubePorIdComSucesso()`
  - Testa o endpoint de atualização (`/api/clube/atualizar/{id}`) com dados válidos.
  - Simula fluxo normal, mockando o service para retornar o clube atualizado.
  - Verifica status 200 OK e garante via `Mockito.verify` que service foi chamado com os dados corretos.

- `deveRetornarNotFoundQuandoAtualizarClubeComIdInexistente()`
  - Testa o cenário em que o id do clube não existe.
  - Mocka o service para lançar `ClubeNaoEncontradoException`.
  - Verifica status 404 e conteúdo do JSON de erro (`codigoErro`, `mensagem`).

- `deveRetornarBadRequestQuandoAtualizarClubeComDtoInvalido(String nome, String siglaEstado, String dataCriacaoStr, String campoErro, String mensagemEsperada)`
  - Teste parametrizado via `@CsvSource` cobrindo todos os casos relevantes de violação de validação do DTO.
  - Envia dados inválidos nos campos (`nome`, `siglaEstado`, `dataCriacao`), incluindo nulos, tamanhos e formatos errados.
  - Valida retorno 400 e a mensagem de erro específica esperada no campo correto do JSON (`errors.<campo>` e `codigoErro` igual a `CAMPO_INVALIDO`).

- `deveRetornarConflictQuandoNomeJaCadastradoNoEstado()`
  - Testa cenário de negócio onde já existe clube com mesmo nome no estado.
  - Mocka o service para lançar `ClubeComNomeJaCadastradoNoEstadoException`.
  - Espera status 409 e conteúdo do erro padronizado (`codigoErro`, `mensagem`).

- `deveRetornarConflictQuandoDataCriacaoPosteriorAPartida()`
  - Testa cenário de negócio onde a data de criação do clube conflita com partidas já cadastradas.
  - Mocka o service para lançar `DataCriacaoPosteriorDataPartidaException`.
  - Espera status 409 e JSON de erro (`codigoErro`, `mensagem`).

- `deveRetornarBadRequestQuandoEstadoInexistente()`
  - Testa cenário de negócio onde o estado informado não existe.
  - Mocka o service para lançar `EstadoInexistenteException`.
  - Espera status 400 (`BAD_REQUEST`) e JSON de erro padronizado (`codigoErro`, `mensagem`).

#### **Principais argumentos e dependências**
- **Controller/Endpoint testado:**
  - `/api/clube/atualizar/{id}` [PUT]
- **DTO de request:** `AtualizarClubeRequestDTO` (validações: tamanho, formato, data)
- **Campos testados:** `nome`, `siglaEstado`, `dataCriacao`
- **Mocks do service:** `Mockito.when(...).thenReturn(...)` e `thenThrow(...)` para simulação de cenários diversos
- **Uso de `Mockito.verify(... argThat(...))`** para garantir os valores passados ao service
- **Validações automáticas propagam `MethodArgumentNotValidException`**, capturada por `ControllerAdvice` e customizada no retorno JSON
- **ControllerAdvice** incluído no setup do MockMvc para tratamento global e específico das exceções

#### **Checklist de implementação**
- [x] Teste para PUT de sucesso validando chamada exata ao service.
- [x] Teste de exceção para id não encontrado (404).
- [x] Teste parametrizado cobrindo todas as validações do DTO, campo a campo (400).
- [x] Teste para exceção de negócio de nome duplicado (409).
- [x] Teste para exceção de negócio de data inválida com partidas (409).
- [x] Teste para exceção de estado inexistente (400).
- [x] Conferência dos campos e mensagens nas respostas de erro.
- [x] Setup do MockMvc inclui dois ControllerAdvices.
- [x] Impressão do início de cada teste via `PrintUtil`.

---

## 4. Criar clube controller (POST - sucesso, validação, erros de negócio)

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável pelo cadastro de clubes de futebol.  
A classe `CriarClubeApiControllerTest` cobre os principais cenários de criação: fluxo feliz de cadastro, erros de validação do DTO, e exceções de regras de negócio customizadas do domínio.

#### **Métodos de teste**
- `deveCriarClubeComSucessoERetornar201Created()`
  - Testa o endpoint de criação (`/api/clube/criar`) com dados válidos.
  - Mocka o service para retornar o clube criado.
  - Verifica status 201 (Created) e garante via Mockito.verify que o service foi chamado com os dados corretos.

- `deveRetornarBadRequestQuandoCriarClubeComDtoInvalido(String nome, String siglaEstado, String dataCriacaoStr, String campoErro, String mensagemEsperada)`
  - Teste parametrizado via @CsvSource, cobrindo todos os cenários relevantes de violação de validação do DTO.
  - Envia dados inválidos nos campos (`nome`, `siglaEstado`, `dataCriacao`), incluindo nulos, tamanhos e formatos errados.
  - Valida retorno 400 (Bad Request) e a mensagem de erro específica esperada no campo correto do JSON (`errors.<campo>`) e código de erro `CAMPO_INVALIDO`.

- `deveRetornarBadRequestQuandoEstadoInexistente()`
  - Testa cenário de negócio onde o estado informado não existe.
  - Mocka o service para lançar a exceção `EstadoInexistenteException`.
  - Espera status 400 e JSON de erro padronizado com código `ESTADO_INEXISTENTE` e mensagem específica.

- `deveRetornarConflictQuandoNomeJaCadastradoNoEstado()`
  - Testa cenário de negócio onde já existe clube com mesmo nome no estado.
  - Mocka o service para lançar `ClubeComNomeJaCadastradoNoEstadoException`.
  - Espera status 409 (Conflict) e JSON de erro padronizado com código `CLUBE_DUPLICADO` e mensagem específica.

#### **Principais argumentos e dependências**
- **Controller/Endpoint testado:**
  - `/api/clube/criar` [POST]
- **DTO de request:** `CriarClubeRequestDTO` (validações: tamanho, formato, data)
- **Campos testados:** `nome`, `siglaEstado`, `dataCriacao`
- **Mocks do service:** Uso de `when(...).thenReturn(...)` e `thenThrow(...)` para simulação de cenários de sucesso e exceção
- **Utilização de Mockito.verify e argThat** para garantir os valores corretos enviados ao service
- **Validações automáticas disparam `MethodArgumentNotValidException`**, tratada por `ControllerAdvice` com retorno JSON padronizado
- **ControllerAdvice** incluído no setup do MockMvc para tratamento global e customizado das exceções de negócio
- **Impressão do início de cada teste** via PrintUtil para rastreabilidade

#### **Checklist de implementação**
- [x] Teste para POST de sucesso validando a chamada ao service e status 201.
- [x] Teste parametrizado cobrindo todas as validações do DTO, campo a campo (400).
- [x] Teste para exceção de negócio de nome duplicado (409).
- [x] Teste para exceção de estado inexistente (400).
- [x] Verificação dos campos e mensagens nas respostas de erro.
- [x] Setup do MockMvc inclui ControllerAdvices para tratamento de erros.
- [x] Verificação rigorosa com Mockito.verify dos argumentos.
- [x] Impressão do início dos testes com PrintUtil.

## Estadio

## 1. Buscar estádio controller (GET - paginado, por id, exceção)

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável pela busca de estádios de futebol.  
A classe `BuscarEstadioApiControllerTest` cobre cenários principais de busca paginada (com e sem filtro de nome), busca por id e cenário de exceção quando o estádio não é encontrado.

#### **Métodos de teste**
- `deveBuscarTodosEstadiosComSucesso(String nome)`
  - Teste parametrizado via @CsvSource, cobrindo busca sem filtro (retorna todos) e com filtro de nome.
  - Simula página de resultados com dois estádios distintos e valida JSON de resposta.
  - Verifica se o service foi chamado com os parâmetros apropriados.
- `deveBuscarEstadioPorIdComSucesso()`
  - Testa busca por id e valida retorno dos campos do EstadioResponseDTO.
  - Garante chamada ao método buscarEstadioPorId no service.
- `deveRetornarNotFoundQuandoEstadioNaoExistir()`
  - Simula exceção de EstadioNaoEncontradoException ao buscar id inexistente.
  - Espera status 404 e verifica a chamada ao service.

#### **Principais argumentos e dependências**
- **Controllers/Endpoints testados:**
  - `/api/estadio/buscar [GET]` (paginado, opcionalmente filtrado por `nome`)
  - `/api/estadio/buscar/{id} [GET]`
- **Mocks do service:** Uso de Mockito para simular page de DTOs e lançamentos de exceção
- **ArgumentResolver** para Pageable na configuração do MockMvc
- **ControllerAdvices** para tratamento de exceções
- **Impressão do início dos testes** via PrintUtil para rastreabilidade

#### **Checklist de implementação**
- [x] Teste parametrizado de busca paginada com variações de filtro de nome.
- [x] Teste para busca por id com validação detalhada do JSON.
- [x] Teste de exceção para id inexistente retornando 404.
- [x] Setup completo do MockMvc com ControllerAdvices e ArgumentResolver.
- [x] Uso do PrintUtil para logar início dos testes.

---

## 2. Criar estádio controller (POST - sucesso, validação, conflito de nome)

#### **Descrição técnica**
Foram implementados testes unitários para o controller responsável pelo cadastro de estádios de futebol.
A classe `CriarEstadioApiControllerTest` cobre os principais cenários de criação, incluindo fluxo de cadastro bem-sucedido, erros de validação do DTO e conflito de nome já existente.

#### **Métodos de teste**
- `deveCriarEstadioComSucessoERetornar201Created()`
  - Testa o endpoint de criação (`/api/estadio/criar`) com dados válidos.
  - Mocka o service para retornar o estádio criado.
  - Verifica status 201 (Created) e garante via Mockito.verify que o service foi chamado com o nome correto.
- `deveRetornarBadRequestQuandoCriarEstadioComDtoInvalido(String nome, String campoErro, String mensagemEsperada)`
  - Teste parametrizado via @CsvSource para cenários de erro do DTO (`@Size`, `@Pattern`), validando status 400, campo de erro e mensagem específica.
- `deveRetornarConflictQuandoNomeJaCadastrado()`
  - Testa o cenário de nome duplicado, simulando EstadioJaExisteException.
  - Verifica status 409, código e mensagem customizada no JSON de erro.

#### **Principais argumentos e dependências**
- **Controller/Endpoint testado:**
  - `/api/estadio/criar` [POST]
- **DTO de request:** `CriarEstadioRequestDTO` (validações de tamanho e formato no campo `nome`)
- **Mocks do service:** Uso de Mockito para simular retorno do estádio criado e lançamento de exceção de nome duplicado
- **Configuração do MockMvc:** Inclui ControllerAdvices para tratamento global e de exceção de estádio
- **Verificação dos argumentos com Mockito.verify** e validadores específicos no DTO
- **Impressão do início dos testes** via PrintUtil

#### **Checklist de implementação**
- [x] Teste para POST de sucesso com status 201 Created.
- [x] Teste parametrizado cobrindo validação de campo do DTO e retorno 400.
- [x] Teste para exceção de nome de estádio duplicado com retorno 409 Conflict.
- [x] Setup do MockMvc com ControllerAdvices.
- [x] Verificação rigorosa da chamada ao service e mensagens de erro em JSON.
- [x] Utilização de PrintUtil para rastreabilidade nos testes.

---

## 3. AtualizarEstadioApiControllerTest (testes - atualização, validação, conflitos e inexistência de estádio)

#### **Descrição técnica**
A classe `AtualizarEstadioApiControllerTest` valida, de forma isolada, os principais fluxos do endpoint de atualização de estádios (`AtualizarEstadioApiController`). Os testes cobrem cenários de sucesso, validações de entrada (nome inválido), conflitos (nome já existente) e não encontrados (ID inexistente). São utilizados Mockito para mockar dependências e MockMvc configurado com handlers globais e específicos.

#### **Métodos/Funções principais**
- **setUp**
  - Inicializa MockMvc para testar a controller isoladamente, aplicando o `GlobalApiExceptionHandler` e `EstadioApiExceptionHandler` para tratamento de erros.
- **deveAtualizarEstadioComSucessoERetornar200OK**
  - Testa fluxo principal de sucesso para atualização de estádio. Simula retorno correto da service e valida interação e resposta 200.
- **deveRetornarBadRequestQuandoAtualizarEstadioComDtoInvalido**
  - Utiliza dados parametrizados para testar diferentes erros de validação do DTO (`nome` muito curto ou com caracteres inválidos). Espera erro 400 e presença do campo de erro e mensagem específica.
- **deveRetornarConflictQuandoNomeJaCadastrado**
  - Simula tentativa de atualização com nome já existente, força exceção customizada (`EstadioJaExisteException`) e valida retorno 409 com mensagem apropriada.
- **deveRetornarNotFoundQuandoAtualizarEstadioComIdInexistente**
  - Simula cenário em que o ID não existe, disparando `EstadioNaoEncontradoException` e garantindo status 404 Not Found, código e mensagem específicos.

#### **Principais argumentos, entradas e dependências**
- **Endpoint testado:** `PUT /api/estadio/atualizar/{id}`
- **DTO:** `AtualizarEstadioRequestDTO` — recebe campo `nome`, validado com regras mínimas de tamanho e padrão de caracteres
- **Dependências Mockadas:**
  - `AtualizarEstadioService` para orquestração dos fluxos de atualização
  - Exceções customizadas: `EstadioJaExisteException`, `EstadioNaoEncontradoException`
  - Utilização de `JsonUtil` para serialização dos objetos
  - Handlers globais/local para tratamento e mapeamento de erros na resposta
- **Validações:**
  - Tamanho e padrão do nome
  - Conflito de nomes já cadastrados
  - Existência do estádio pelo ID informado

#### **Checklist de implementação**
- [x] Fluxo principal de atualização de estádio funcionando e validado (status 200 OK)
- [x] Validação de entradas obrigatórias e regras de negócio (nome)
- [x] Testes para tratamento de conflito (nome duplicado) com status 409
- [x] Testes para ID inexistente retornando status 404 e mensagens customizadas
- [x] Configuração dos handlers de exceção específicos e globais na MockMvc
- [x] Uso de mocks para isolar camada de controller dos serviços reais
- [x] Cobertura para mensagens, códigos de erro e estrutura da resposta JSON