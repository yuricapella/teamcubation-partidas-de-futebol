## 1. Clube (criação, atualização, busca, inativação – fluxos de sucesso e erro)

#### **Descrição técnica**
APIs RESTful para gerenciamento de clubes de futebol. Implementa cadastro, busca, atualização e inativação, aplicando validação automática (Bean Validation) e regras de negócio via services e validators. Controle centralizado de exceções. Erros retornam sempre com payload padronizado para tratamento no front ou integração.

#### **Erros padrões de Clube**

##### 1.1. Schema global de erro
```json
{
  "codigoErro": "CLUBE_NAO_ENCONTRADO",
  "dataHora": "2025-08-04T12:00:00",
  "mensagem": "Mensagem legível ao consumidor.",
  "errors": {
    "campo": "Descrição do erro para o campo."
  }
}
```

##### 1.2. Mapas de códigos e exemplos de payloads

- **CAMPO_INVALIDO** – violação de bean validation
    - `400 Bad Request`
    - Exemplo:
      ```json
      {
        "codigoErro": "CAMPO_INVALIDO",
        "dataHora": "2025-08-04T11:02:56",
        "mensagem": "Invalid request content.",
        "errors": {
          "siglaEstado": "A sigla do estado só pode ter 2 letras.",
          "nome": "O nome tem que ter no minimo duas letras;",
          "dataCriacao": "A data de criação não pode ser futura"
        }
      }
      ```
- **ESTADO_INEXISTENTE** – sigla de estado inválida
    - `400 Bad Request`
    - Exemplo:
      ```json
      {
        "codigoErro": "ESTADO_INEXISTENTE",
        "dataHora": "2025-08-04T11:03:40",
        "mensagem": "Não é possivel criar o clube pois o estado AX não existe.",
        "errors": null
      }
      ```
- **CLUBE_DUPLICADO** – tentava duplicar nome/estado já existente
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBE_DUPLICADO",
        "dataHora": "2025-08-04T11:04:33",
        "mensagem": "Já existe clube com este nome no mesmo estado.",
        "errors": null
      }
      ```
- **CLUBE_NAO_ENCONTRADO** – id informado não existe
    - `404 Not Found`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBE_NAO_ENCONTRADO",
        "dataHora": "2025-08-04T11:52:23",
        "mensagem": "Clube com id 999 não encontrado.",
        "errors": null
      }
      ```
- **DATA_CRIACAO_POSTERIOR_A_DATA_PARTIDA** – ao atualizar data para depois de partida existente
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "DATA_CRIACAO_POSTERIOR_A_DATA_PARTIDA",
        "dataHora": "2025-08-04T11:52:08",
        "mensagem": "A data de criação do clube com id 1 está posterior a data de uma partida cadastrada.",
        "errors": null
      }
      ```

#### **Principais cenários cobertos**
- Cadastro (`POST /api/clube/criar`): Erros de campo, estado, duplicidade
- Atualização (`PUT /api/clube/atualizar/{id}`): Mesmos acima + data inválida, clube não encontrado
- Busca (`GET /api/clube/buscar/{id}`): Clube não encontrado
- Inativação (`DELETE /api/clube/inativar/{id}`): Clube não encontrado

---

## 2. Estádio (criação, atualização, busca – fluxos de sucesso e erro)

#### **Descrição técnica**
APIs RESTful para gerenciamento de estádios, contemplando cadastro, atualização (com busca automática do endereço via ViaCep), busca por critérios e inativação. Validação de nome e cep, tratamento dos principais erros de negócio e estrutura padronizada de erro.

#### **Erros padrões de Estádio**

- **CAMPO_INVALIDO** – qualquer falha de bean validation: nome ou cep inválido/nulo
    - `400 Bad Request`
    - Exemplo:
      ```json
      {
        "codigoErro": "CAMPO_INVALIDO",
        "dataHora": "2025-08-04T16:18:54",
        "mensagem": "Invalid request content.",
        "errors": {
          "nome": "não deve ser nulo",
          "cep": "O cep deve conter exatamente 8 dígitos numéricos"
        }
      }
      ```
- **ESTADIO_JA_EXISTE** – nome duplicado
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "ESTADIO_JA_EXISTE",
        "dataHora": "2025-08-04T16:15:10",
        "mensagem": "Já existe um estadio com este nome.",
        "errors": null
      }
      ```
- **ESTADIO_NAO_ENCONTRADO** – busca, atualização, deleção não encontra id
    - `404 Not Found`
    - Exemplo:
      ```json
      {
        "codigoErro": "ESTADIO_NAO_ENCONTRADO",
        "dataHora": "2025-08-04T16:14:43",
        "mensagem": "Estadio com id 999 não encontrado.",
        "errors": null
      }
      ```

#### **Principais cenários cobertos**
- Cadastro (`POST /api/estadio/criar`)
- Atualização (`PUT /api/estadio/atualizar/{id}`)
- Busca detalhada (`GET /api/estadio/buscar/{id}`)
- Busca paginada (`GET /api/estadio/buscar`): Apenas casos felizes/documentação externa para erros.
- Todas as situações acima utilizam sempre o schema padrão de erro.

---

## 3. Partida (criação, atualização, busca, deleção – fluxos de sucesso e erro)

#### **Descrição técnica**
APIs RESTful para partidas de futebol, abrangendo cadastro, edição, busca (por filtros ou id) e deleção. Aplica todas as validações de negócio necessárias: clubes distintos, clubes/estádio existentes, datas válidas, conflitos de agendamento, etc. Controle unificado de exceções para respostas consistentes.

#### **Erros padrões de Partida**

- **CAMPO_INVALIDO** – campos obrigatórios ausentes, negativos, data futura
    - `400 Bad Request`
    - Exemplo:
      ```json
      {
        "codigoErro": "CAMPO_INVALIDO",
        "dataHora": "2025-08-04T22:31:14",
        "mensagem": "Invalid request content.",
        "errors": {
          "golsMandante": "O número de gols do mandante não pode ser negativo",
          "dataHora": "A data da partida não pode ser futura"
        }
      }
      ```
- **CLUBES_IGUAIS** – mandante/visitante iguais
    - `400 Bad Request`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBES_IGUAIS",
        "dataHora": "2025-08-04T22:32:29",
        "mensagem": "Não é possível criar partida pois os clubes são iguais.",
        "errors": null
      }
      ```
- **DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE** – data da partida antes da criação do clube
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "DATA_PARTIDA_ANTERIOR_A_CRIACAO_DO_CLUBE",
        "dataHora": "2025-08-04T22:37:22",
        "mensagem": "Não pode cadastrar uma partida para uma data anterior à data de criação do clube.",
        "errors": null
      }
      ```
- **CLUBE_INATIVO** – algum clube envolvido está inativo
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBE_INATIVO",
        "dataHora": "2025-08-04T22:38:08",
        "mensagem": "Não é possível criar a partida pois há um clube inativo.",
        "errors": null
      }
      ```
- **CLUBE_TEM_PARTIDAS_COM_DATA_MENOR_QUE_48_HORAS_DA_NOVA_PARTIDA** – conflito por partidas anteriores próximas
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBE_TEM_PARTIDAS_COM_DATA_MENOR_QUE_48_HORAS_DA_NOVA_PARTIDA",
        "dataHora": "2025-08-04T22:39:10",
        "mensagem": "Não é possível criar a partida pois um dos clubes já possui uma partida cadastrada em menos de 48 horas desta data.",
        "errors": null
      }
      ```
- **ESTADIO_JA_POSSUI_PARTIDA_MARCADA_NO_MESMO_DIA** – estádio já ocupado
    - `409 Conflict`
    - Exemplo:
      ```json
      {
        "codigoErro": "ESTADIO_JA_POSSUI_PARTIDA_MARCADA_NO_MESMO_DIA",
        "dataHora": "2025-08-04T22:40:24",
        "mensagem": "Não é possível criar a partida pois no estádio já tem uma partida marcada para o mesmo dia.",
        "errors": null
      }
      ```
- **CLUBE_NAO_ENCONTRADO** – id de clube não existe
    - `404 Not Found`
    - Exemplo:
      ```json
      {
        "codigoErro": "CLUBE_NAO_ENCONTRADO",
        "dataHora": "2025-08-04T19:27:34",
        "mensagem": "Clube com id 999 não encontrado.",
        "errors": null
      }
      ```
- **ESTADIO_NAO_ENCONTRADO** – estádio não existe
    - `404 Not Found`
    - Exemplo:
      ```json
      {
        "codigoErro": "ESTADIO_NAO_ENCONTRADO",
        "dataHora": "2025-08-04T19:28:03",
        "mensagem": "Estádio com id 999 não encontrado.",
        "errors": null
      }
      ```
- **PARTIDA_NAO_ENCONTRADA** – partida não existe (edição, deleção, busca)
    - `404 Not Found`
    - Exemplo:
      ```json
      {
        "codigoErro": "PARTIDA_NAO_ENCONTRADA",
        "dataHora": "2025-08-04T23:05:00",
        "mensagem": "Partida com id 999 não encontrada.",
        "errors": null
      }
      ```

#### **Principais cenários cobertos**
- Cadastro (`POST /api/partida/criar`): validador cobre campos, clubes, estádio, datas, regras de negócio
- Atualização (`PUT /api/partida/atualizar/{id}`): mesmos erros possíveis do cadastro, mais partida não encontrada
- Busca (`GET /api/partida/buscar/{id}`): partida não encontrada
- Listagem paginada: Sem erro; casos de ausência retornam lista vazia e 200 OK
- Deleção (`DELETE /api/partida/deletar/{id}`): partida não encontrada

---

## 4. Observações gerais e orientação para documentação futura

- Sempre retorne os campos "codigoErro", "mensagem", "dataHora" e "errors" (quando aplicável).
- Use erro apenas “representativo” nas anotações das controllers. Casos específicos de campos inválidos, erros de negócio etc, documente neste arquivo ou em Wiki.
- Novos fluxos ou recursos devem seguir o mesmo padrão.
- Em endpoints paginados, resposta vazia é considerada 200 OK com array vazio.
- Mantenha os exemplos de erros padronizados para facilitar testes automatizados e integração de clientes.

---