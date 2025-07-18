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
---

## 1. Criar Clube Service (criação e cenários de exceção)

#### **Descrição técnica**
Implementa todos os testes unitários necessários para a camada de serviço de criação de clube (`CriarClubeService`), abrangendo:
- Operação de sucesso, atribuição de atributos obrigatórios (`nome`, `siglaEstado`, `dataCriacao`, `ativo`, `id`)
- Fluxo de exceção para estados inexistentes e nomes já cadastrados no mesmo estado, propagando as exceções corretas e prevenindo persistência em caso de erro
- Validação da ordem correta: validação ocorre antes do acesso ao repositório

#### **Métodos/Funções principais**
- **deveCriarUmClubeComSucesso**
  - Testa a criação plena de um clube, incluindo mapeamento do DTO, atribuição correta de campos, ativação automática e ausência de data de atualização
  - Garante execução de `clubeValidator.validarClubeNaCriacao` antes de `clubeRepository.save`
- **deveLancarEstadoInexistenteException_quandoEstadoNaoExistir**
  - Simula o lançamento de `EstadoInexistenteException` pelo validator para estados inválidos
  - Garante que o save não é invocado e mensagem da exceção está correta
- **deveLancarClubeComNomeJaCadastradoNoEstadoException_quandoNomeDoClubeJaExisteNoMesmoEstado**
  - Simula cenário de nome duplicado para estado, lançando `ClubeComNomeJaCadastradoNoEstadoException`
  - Valida mensagem e não persiste o registro

#### **Principais argumentos, entradas e dependências**
- **Entradas:** `CriarClubeRequestDTO` com nome, siglaEstado e dataCriacao
- **Dependências:**
  - `ClubeRepository` mockado para persistência
  - `ClubeValidator` mockado para simular validações de negócio e lançamento de exceções relevantes
- **Verificações relevantes:**
  - Atributos essenciais preenchidos conforme esperado
  - Ordem de chamada das dependências
  - Exceções propagadas com mensagem correta
  - Save não executado em caso de erros

#### **Checklist de implementação**
- [x] Fluxo principal de criação com persistência
- [x] Validação de atributos obrigatórios e estado inicial (ativo true, dataAtualizacao nula)
- [x] Ordem dos métodos de validação e persistência
- [x] Cenários de exceção para estado inválido e nome duplicado
- [x] Asserts para mensagens das exceções
- [x] Teste para bloqueio do save quando há erro de validação
---

## 2. Buscar Clube Service (busca filtrada, busca por ID e cenário de exceção)

#### **Descrição técnica**
Implementa a bateria de testes unitários para a camada de serviço de busca de clubes (`BuscarClubeService`), cobrindo:
- Busca paginada e filtrada utilizando os parâmetros nome, sigla do estado e booleano "ativo" via método listarClubesFiltrados
- Conversão correta de entidades Clube para DTO de resposta nas respostas da busca filtrada
- Fluxo de sucesso na busca por ID, garantindo a preservação de todos os atributos relevantes do clube retornado
- Cenário negativo onde um clube não é encontrado, assegurando o lançamento e mensagem da exception ClubeNaoEncontradoException

#### **Métodos/Funções principais**
- **deveListarClubesFiltradosComSucesso**
  - Testa integração dos filtros isoladamente (nome, estado, ativo) usando @ParameterizedTest e verifica a correspondência dos dados no DTO
  - Garante chamada do método findByFiltros do ClubeRepository com todos os parâmetros e pageable
- **deveBuscarClubePorIdComSucesso**
  - Verifica busca correta e completa por id existente
  - Assegura manutenção de todos os campos do objeto Clube recuperado
- **deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir**
  - Simula ausência de resultado para o id buscado e valida o lançamento da exceção customizada, com assert na mensagem
  - Utiliza utilitário PrintUtil para exibir mensagem de erro nos logs do teste

#### **Principais argumentos, entradas e dependências**
- Métodos testados: listarClubesFiltrados(nome, estado, ativo, pageable) e buscarClubePorId(id)
- Parâmetros: `String nome`, `String estado`, `Boolean ativo`, `Long id`, `Pageable pageable`
- Mocks: `ClubeRepository`, com configuração de resposta mediante argumentos via Mockito
- Uso de DTO: `ClubeResponseDTO` para conversão e verificação dos dados apresentados ao consumidor
- Suporte para validação de mensagem de exceção em caso de erro esperado na busca

#### **Checklist de implementação**
- [x] Fluxo principal de busca paginada e filtrada (cenário feliz)
- [x] Conversão correta de entidade para DTO em buscas filtradas
- [x] Busca por id com objeto encontrado e cobertura total de atributos
- [x] Tratamento de cenário negativo (clube não encontrado), com assert da exception
- [x] Validação de mocks e verificação das chamadas ao repository em todos os testes
- [x] Uso de utilitário PrintUtil para marcação dos testes e mensagens de erro
---

## 3. Inativar Clube Service (inativação por id e cenário de exceção)

#### **Descrição técnica**
Garante os principais fluxos da camada de serviço responsável por inativar clubes, especificamente:
- Alterar o atributo `ativo` para `false` e definir o `dataAtualizacao` ao inativar um clube pelo identificador
- Persistir a alteração no repository
- Lançar exceção customizada caso o clube buscado não exista

#### **Métodos/Funções principais**
- **deveInativarClubePorIdComSucesso**
  - Mocka BuscarClubeService para retornar entidade existente
  - Garante update correto nos campos (`ativo=false`, dataAtualizacao preenchida)
  - Valida persistência com save no ClubeRepository
  - Verifica ordem das chamadas no fluxo de service
- **deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir**
  - Simula ausência do clube ao buscar por id com throw de ClubeNaoEncontradoException
  - Valida mensagem da exception e chamada única do método de busca
  - Utiliza PrintUtil para log do erro no teste

#### **Principais argumentos, entradas e dependências**
- Service testada: `InativarClubeService`
- Parâmetros principais: `Long id` do clube
- Dependências mockadas: `BuscarClubeService`, `ClubeRepository`
- Integração: verifica chamada a buscarClubeService e persistência no repository

#### **Checklist de implementação**
- [x] Inativação do clube (ativo=false, dataAtualizacao)
- [x] Persistência da alteração no banco (save)
- [x] Verificação de ordem entre busca e persistência
- [x] Lançamento e validação de exceção de não encontrado
- [x] Assert no estado final dos campos alterados
- [x] Cobertura de logs utilitários para rastreabilidade
---

## 4. Atualizar Clube Service (atualização, ordem de dependências e exceções)

#### **Descrição técnica**
Implementa cobertura completa para a lógica de atualização de clubes na camada serviço, abrangendo:
- Cenário de sucesso com atualização dos campos, persistência e sequenciamento correto das dependências
- Todas as principais exceções de negócio previstas: clube não encontrado, estado inexistente, nome duplicado e data de criação inválida relacionada a partidas
- Utilização de mocks para BuscarClubeService, ClubeRepository e ClubeValidator, com controle preciso de fluxo e validação de ordem

#### **Métodos/Funções principais**
- **deveAtualizarUmClubePorIdComSucesso**
  - Simula fluxo normal de atualização de clube existente
  - Verifica persistência, modificação correta dos atributos e sequência dos métodos dependentes
- **deveLancarClubeNaoEncontradoException_quandoClubeNaoExistir**
  - Garante tratamento de ausência do clube na busca prévia e bloqueio de validação/persistência
- **deveLancarEstadoInexistenteException_quandoEstadoNaoExistir**
  - Testa regra de validação de estado inválido impedindo continuidade da operação
- **deveLancarClubeComNomeJaCadastradoNoEstadoException_quandoNomeDoClubeJaExisteNoMesmoEstado**
  - Cobre bloqueio de atualização quando há clube duplicado no mesmo estado
- **deveLancarDataCriacaoPosteriorDataPartidaException_quandoAlterarDataCriacaoPosteriorADataPartida**
  - Assegura validação de integridade da data de criação frente a partidas cadastradas

#### **Principais argumentos, entradas e dependências**
- Service: AtualizarClubeService
- Parâmetros: AtualizarClubeRequestDTO, id (Long)
- Mocks: BuscarClubeService, ClubeRepository, ClubeValidator, ClubeUtil para instância de domínio
- Validações: correlaciona mensagens e tipos de exceção previstos

#### **Checklist de implementação**
- [x] Atualização de clube para fluxo nominal com todos os atributos essenciais
- [x] Cobertura dos cenários previstos de erro de domínio/regra de negócio
- [x] Testes garantem fluxo correto entre busca, validação e persistência via InOrder
- [x] Logs principais para rastreabilidade de exceções
---

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
---

## 1. Criar Estadio Service (criação e cenários de exceção)

#### **Descrição técnica**
Implementa testes unitários para o fluxo de criação de estádios na camada de serviço (`CriarEstadioService`), cobrindo tanto o caso de sucesso quanto os principais cenários de erro por nome duplicado. Os testes garantem validação adequada do nome via `EstadioValidator` antes de persistir no `EstadioRepository`, além de confirmar a ordem das operações.

#### **Métodos/Funções principais**
- **deveCriarUmEstadioComSucesso**
  - Garante que um novo estádio é criado corretamente e que o save ocorre apenas após a validação bem-sucedida
  - Valida atributos obrigatórios do estádio e ausência de data de atualização inicial
  - Usa InOrder para checar ordem entre validação e persistência
- **deveLancarEstadioJaExisteException_quandoTentarCriarEstadioComMesmoNome**
  - Simula fluxo de tentativa de criação com nome já existente, forçando EstadioJaExisteException
  - Assegura que, ao ocorrer a exceção, o método save do repository não é chamado
  - Valida a mensagem da exception customizada

#### **Principais argumentos, entradas e dependências**
- Service testada: `CriarEstadioService`
- DTO: `CriarEstadioRequestDTO` (nome)
- Dependências mockadas: `EstadioValidator`, `EstadioRepository`
- Principais asserts: atributos do estádio criado, ordem dos métodos, exceção e mensagem customizada

#### **Checklist de implementação**
- [x] Criação de estádio com sucesso e atributos essenciais
- [x] Validação do nome via EstadioValidator
- [x] Bloqueio com exceção de nome duplicado, sem chamada ao save
- [x] Conferência da ordem entre validação e persistência
- [x] Logs utilitários para rastreabilidade
---

## 2. Atualizar Estadio Service (atualização, ordem de dependências e exceções)

#### **Descrição técnica**
Abrange o fluxo completo de atualização de estádios na camada de serviço (`AtualizarEstadioService`), incluindo:
- Atualização de entidade existente, com propagação correta das alterações e controle da ordem das operações entre busca, validação e persistência
- Tratamento de exceções de domínio: estádio não encontrado e duplicidade de nome ao atualizar
- Utilização de mocks para EstadioRepository, EstadioValidator e BuscarEstadioService, garantindo path correto de execução em todos os cenários

#### **Métodos/Funções principais**
- **deveAtualizarUmEstadioComSucesso**
  - Simula atualização normal de estádio, garantindo alteração de nome e atualização do campo dataAtualizacao
  - Garante persistência e sequência exata do fluxo de dependências
- **deveLancarEstadioNaoEncontradoException_quandoAtualizarEstadioInexistente**
  - Bloqueia validação/persistência na ausência do estádio, lançando exceção customizada
  - Valida mensagem de exceção e não execução dos métodos seguintes
- **deveLancarEstadioJaExisteException_quandoTentarAtualizarEstadioComMesmoNome**
  - Testa cenário de validação de nome duplicado, garantindo lançamento de exceção e ausência de save

#### **Principais argumentos, entradas e dependências**
- Service: AtualizarEstadioService
- DTO: AtualizarEstadioRequestDTO (nome)
- Dependências mockadas: EstadioRepository, EstadioValidator, BuscarEstadioService
- Validação via EstadioValidator (validarDadosDoEstadioAoAtualizar)
- Checagem de ordem entre busca, validação e save com InOrder

#### **Checklist de implementação**
- [x] Atualização e persistência de estádio com sucesso
- [x] Tratamento de exceções de domínio (não encontrado e nome duplicado)
- [x] Garantia de bloqueio em caminhos de exceção
- [x] Validação da ordem correta das dependências
- [x] Logs de rastreabilidade para erros e início dos testes
---

## Partida

## 1. Criação de partida (controller - fluxos de sucesso, validações e exceções específicas)

#### **Descrição técnica**
A classe `CriarPartidaApiControllerTest` testa os principais fluxos do endpoint de criação de partidas na API. 
Valida tanto o caminho de sucesso (resposta 201) quanto diferentes cenários de erro, 
incluindo validação do DTO e exceções de negócio, simulando responses padronizadas para cada caso. 
Utiliza MockMvc com Mockito para orquestração dos mocks e autenticação do fluxo HTTP, 
além de handlers globais e específicos para mapeamento consistente das exceções.

#### **Métodos/Funções principais**
- **deveCriarPartidaComSucessoERetornar201Created:**  
  Testa o fluxo padrão de criação de partida, garantindo status 201 e chamada correta ao CriarPartidaService.

- **deveRetornarBadRequestParaCamposInvalidos:**  
  Teste parametrizado cobrindo campos obrigatórios nulos, valores negativos e data futura, validando erro 400 e mensagens do DTO.

- **deveRetornarBadRequestQuandoClubesSaoIguais:**  
  Simula lançamento da ClubesIguaisException, garante status 400 e response com código/mensagem apropriados.

- **deveRetornarConflictQuandoDataPartidaAnteriorACriacaoDoClube:**  
  Testa exceção de tentativa de criar partida para data anterior à criação dos clubes, retornando 409 e conteúdo de erro.

- **deveRetornarConflictQuandoClubeInativo:**  
  Valida comportamento quando um clube do DTO está inativo, gerando 409 e response com detalhes do erro.

- **deveRetornarConflictQuandoEstadioJaPossuiJogoNoDia:**  
  Simula conflito de múltiplas partidas no mesmo estádio e dia, garantindo exceção e resposta de conflito 409.

- **deveRetornarConflictQuandoClubeJaTiverPartidasDentroDe48HorasDaNovaPartida:**  
  Cobre situação de conflito de horários por partidas anteriores dos clubes em intervalo inferior a 48h, validando o JSON de erro.

#### **Principais argumentos, entradas e dependências**
- **Endpoint testado:** `POST /api/partida/criar`
- **DTO:** `CriarPartidaRequestDTO` — recebe clubeMandanteId, clubeVisitanteId, estadioId, golsMandante, golsVisitante, dataHora
- **Dependências mockadas:**
  - `CriarPartidaService`
  - Exceções específicas de partida: ClubesIguaisException, DataPartidaAnteriorACriacaoDoClubeException, ClubeInativoException, EstadioJaPossuiPartidaNoMesmoDiaException, ClubesComPartidasEmHorarioMenorQue48HorasException
- **Response esperado:**
  - Status HTTP 201, 400 ou 409, dependendo do cenário
  - Corpo JSON validando códigos de erro e mensagens de exceção via `jsonPath`
  - Consistência garantida por uso de PartidaApiExceptionHandler e GlobalApiExceptionHandler

#### **Checklist de implementação**
- [x] Fluxo principal de criação de partida com retorno 201 Created
- [x] Validação de campos obrigatórios, tipos e regras do DTO via testes parametrizados
- [x] Tratamento e teste das principais exceções de negócio relacionadas ao domínio de partida
- [x] Verificação dos status HTTP, códigos de erro e mensagens nas respostas das exceções
- [x] Integração nos testes dos handlers de exceção globais e locais
- [x] Cobertura de casos de conflito de agendamento, clubes inativos e clubes iguais

---

## 2. Atualização de partida (controller - cenários de sucesso, validação e exceções)

#### **Descrição técnica**
A classe `AtualizarPartidaApiControllerTest` cobre, de maneira isolada, todos os principais fluxos do endpoint de atualização de partidas pela AtualizarPartidaApiController. Os testes abrangem desde o fluxo de sucesso até todas as validações de entrada, regras de negócio e exceções previstas no domínio. Utiliza MockMvc para simular requisições HTTP e Mockito para orquestrar retornos e lançamentos de exceções do serviço mockado, além de aplicar handlers globais e do domínio de partida.

#### **Métodos/Funções principais**
- **deveAtualizarPartidaComSucessoERetornar200OK**  
  Valida atualização bem-sucedida da partida, verificando resposta 200 e parâmetros corretos ao serviço.
- **deveRetornarBadRequestParaCamposInvalidos**  
  Parametriza casos de erro por obrigatoriedade de campo, valor negativo e data futura, validando a estrutura da resposta de erro.
- **deveRetornarBadRequestQuandoClubesSaoIguais**  
  Simula exceção de clubes iguais, verifica retorno 400 e mensagem.
- **deveRetornarConflictQuandoDataPartidaAnteriorAAtualizacaoDoClube**  
  Testa exceção para data anterior à criação do clube, conferindo status 409.
- **deveRetornarConflictQuandoClubeInativo**  
  Cobre atualização envolvendo clube inativo, garantindo returno e mensagem certas.
- **deveRetornarConflictQuandoEstadioJaPossuiJogoNoDia**  
  Garante cobertura para caso de estádio ocupado na mesma data.
- **deveRetornarConflictQuandoClubeJaTiverPartidasDentroDe48HorasDaNovaPartida**  
  Valida cenário de conflito de horários para clubes com partidas em menos de 48h.
- **deveRetornarNotFoundQuandoPartidaNaoExistir**  
  Testa situação de atualização de partida inexistente, retornando 404 e mensagem específica.

#### **Principais argumentos, entradas e dependências**
- **Endpoint testado:** `PUT /api/partida/atualizar/{id}`
- **DTO:** `AtualizarPartidaRequestDTO`
- **Dependências mockadas:**
  - `AtualizarPartidaService`
  - Exceções customizadas para regras de negócio: ClubesIguaisException, DataPartidaAnteriorACriacaoDoClubeException, ClubeInativoException, EstadioJaPossuiPartidaNoMesmoDiaException, ClubesComPartidasEmHorarioMenorQue48HorasException, PartidaNaoEncontradaException
- **Response esperado:**
  - Status HTTP 200, 400, 409 ou 404, conforme o cenário
  - Corpo JSON validando código de erro e mensagem da exception, quando aplicável
- **Handlers utilizados:** GlobalApiExceptionHandler e PartidaApiExceptionHandler

#### **Checklist de implementação**
- [x] Cobertura total do fluxo de sucesso (200 OK)
- [x] Teste para validação de entrada (obrigatórios, negativo, data futura)
- [x] Clubes iguais (400 Bad Request)
- [x] Data anterior à criação do clube (409 Conflict)
- [x] Clube inativo (409 Conflict)
- [x] Estádio já ocupado no mesmo dia (409 Conflict)
- [x] Partidas em menos de 48 horas (409 Conflict)
- [x] Partida não encontrada (404 Not Found)
- [x] Handlers globais e de domínio configurados no MockMvc

---

## 3. Deletar partida (controller - deleção e tratamento de exceção de não encontrada)

#### **Descrição técnica**
Testa o endpoint de deleção de partidas implementado pela DeletarPartidaApiController. Garante a correta resposta do controller tanto para operações de deleção bem-sucedidas quanto para o cenário de exceção quando a partida solicitada não existe. Utiliza MockMvc para orquestração dos fluxos HTTP e Mockito para simulação dos cenários de negócio e lançamento de exceções específicas, integrando handlers globais e de domínio de partida para padronizar o tratamento de erro da API.

#### **Métodos/Funções principais**
- `deveDeletarPartidaPorIdComSucessoERetornarNoContent`
  - Testa remoção de partida existente por id, confere status 204 No Content e chamada ao service mockado
- `deveRetornarNotFoundAoDeletarPartidaInexistente`
  - Simula solicitação de deleção para id inválido, força lançamento de PartidaNaoEncontradaException, valida resposta 404 Not Found

#### **Principais argumentos, entradas e dependências**
- Endpoint: `DELETE /api/partida/deletar/{id}`
- Service mockado: `DeletarPartidaService`
- Exceção customizada: `PartidaNaoEncontradaException`
- Utilização de MockMvc para asserções de status HTTP, e de handlers GlobalApiExceptionHandler e PartidaApiExceptionHandler para tratamento e mapeamento dos erros

#### **Checklist de implementação**
- [x] Fluxo principal de deleção de partida com retorno 204 No Content
- [x] Tratamento de tentativa de deleção de partida inexistente com retorno 404 Not Found
- [x] Integração dos exception handlers globais e específicos de partida
- [x] Verificação de chamada única ao service para ambos cenários

---

## 4. Buscar partida (controller - busca paginada, filtros e busca por id)

#### **Descrição técnica**
Foi implementada a bateria de testes para o endpoint de buscar partida na BuscarPartidaApiController. 
Cobre todos os fluxos relevantes, incluindo buscas paginadas com 
filtros opcionais (clubeId, estadioId, goleada, mandante, visitante), 
busca unitária pelo id, e tratamento de exceção quando a partida não existe. 
A criação e paginação de objetos Partida utiliza PartidaUtil, 
garantindo consistência nos mocks e validações de retorno.

#### **Métodos/Funções principais**
- `deveBuscarPartidasComFiltros`
  - Valida busca paginada com diversos filtros, confere conteúdo do JSON, size da lista e mapeamento dos sub-objetos (clube, estádio, resultado)
  - Usa Mockito para mockar a busca paginada partindo de Page<Partida> criado pelo utilitário PartidaUtil
- `deveBuscarPartidaPorIdComSucesso`
  - Testa busca de partida única por ID, verificando todos os campos relevantes retornados e integração com o mapper de DTOs
- `deveRetornarNotFoundQuandoClubeNaoExistir`
  - Simula cenário de partida inexistente via exceção específica, valida status 404 e rastreabilidade do fluxo

#### **Principais argumentos, entradas e dependências**
- Endpoints:
  - GET `/api/partida/buscar` com filtros opcionais via query params
  - GET `/api/partida/buscar/{id}`
- Argumentos e filtros principais: clubeId, estadioId, goleada, mandante, visitante, pageable
- Dependências: BuscarPartidaService mockado, PartidaUtil para criação de Partida e Page<Partida>, 
- PartidaResponseMapper para conversão ao DTO de resposta, handlers globais e do domínio de partida configurados no MockMvc

#### **Checklist de implementação**
- [x] Busca paginada de partidas com múltiplos filtros opcionais
- [x] Busca de partida por id retornando DTO completo
- [x] Cobertura para cenário de partida não encontrada (404 Not Found)
- [x] Mock de Page e entidades Partida com utilitário dedicado para padronização dos dados de teste
- [x] Integração e validação dos handlers globais e específicos de partida no controller