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