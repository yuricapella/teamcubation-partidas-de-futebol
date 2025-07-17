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