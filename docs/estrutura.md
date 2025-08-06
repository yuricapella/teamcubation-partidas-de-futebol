### docs/estrutura.md

```
# Estrutura de pastas e arquivos
├── .gitattributes
├── .gitignore
├── .mvn
    └── wrapper
    │   └── maven-wrapper.properties
├── README.md
├── docs
    ├── anotacoes-rabbit.md
    ├── anotacoes-requisitos.md
    ├── anotacoes-teste.md
    ├── como-executar.md
    ├── estrutura.md
    ├── jobs.md
    ├── rabbitmq.md
    └── swagger.md
├── mvnw
├── mvnw.cmd
├── partidas-de-futebol-api
    ├── pom.xml
    └── src
    │   ├── main
    │       ├── java
    │       │   └── br
    │       │   │   └── com
    │       │   │       └── meli
    │       │   │           └── teamcubation_partidas_de_futebol
    │       │   │               ├── TeamcubationPartidasDeFutebolApplication.java
    │       │   │               ├── clube
    │       │   │                   ├── controller
    │       │   │                   │   ├── AtualizarClubeApiController.java
    │       │   │                   │   ├── BuscarClubeApiController.java
    │       │   │                   │   ├── CriarClubeApiController.java
    │       │   │                   │   └── InativarClubeApiController.java
    │       │   │                   ├── dto
    │       │   │                   │   ├── AtualizarClubeRequestDTO.java
    │       │   │                   │   ├── ClubeResponseDTO.java
    │       │   │                   │   ├── CriarClubeRequestDTO.java
    │       │   │                   │   └── mapper
    │       │   │                   │   │   ├── AtualizarClubeRequestMapper.java
    │       │   │                   │   │   ├── ClubeResponseMapper.java
    │       │   │                   │   │   └── CriarClubeRequestMapper.java
    │       │   │                   ├── exception
    │       │   │                   │   ├── ClubeApiExceptionHandler.java
    │       │   │                   │   ├── ClubeComNomeJaCadastradoNoEstadoException.java
    │       │   │                   │   ├── ClubeNaoEncontradoException.java
    │       │   │                   │   ├── DataCriacaoPosteriorDataPartidaException.java
    │       │   │                   │   └── EstadoInexistenteException.java
    │       │   │                   ├── model
    │       │   │                   │   └── Clube.java
    │       │   │                   ├── rabbitmq
    │       │   │                   │   ├── config
    │       │   │                   │   │   ├── RabbitMQCriarClubeConfig.java
    │       │   │                   │   │   └── RabbitMQExchangeClubeConfig.java
    │       │   │                   │   ├── consumer
    │       │   │                   │   │   ├── CriarClubeConsumer.java
    │       │   │                   │   │   └── CriarClubeDlqConsumer.java
    │       │   │                   │   ├── dto
    │       │   │                   │   │   └── ClubeEvent.java
    │       │   │                   │   └── util
    │       │   │                   │   │   ├── ClubeEventStatus.java
    │       │   │                   │   │   └── ClubeEventType.java
    │       │   │                   ├── repository
    │       │   │                   │   └── ClubeRepository.java
    │       │   │                   ├── service
    │       │   │                   │   ├── AtualizarClubeService.java
    │       │   │                   │   ├── BuscarClubeService.java
    │       │   │                   │   ├── CriarClubeService.java
    │       │   │                   │   └── InativarClubeService.java
    │       │   │                   └── util
    │       │   │                   │   ├── ClubeValidator.java
    │       │   │                   │   └── EstadosUF.java
    │       │   │               ├── config
    │       │   │                   ├── ModelMapperConfig.java
    │       │   │                   ├── RabbitMQJsonConfig.java
    │       │   │                   └── SwaggerConfig.java
    │       │   │               ├── estadio
    │       │   │                   ├── controller
    │       │   │                   │   ├── AtualizarEstadioApiController.java
    │       │   │                   │   ├── BuscarEstadioApiController.java
    │       │   │                   │   └── CriarEstadioApiController.java
    │       │   │                   ├── dto
    │       │   │                   │   ├── AtualizarEstadioRequestDTO.java
    │       │   │                   │   ├── CepResponseDTO.java
    │       │   │                   │   ├── CriarEstadioRequestDTO.java
    │       │   │                   │   ├── EstadioEnderecoResponseDTO.java
    │       │   │                   │   ├── EstadioResponseDTO.java
    │       │   │                   │   └── mapper
    │       │   │                   │   │   ├── AtualizarEstadioRequestMapper.java
    │       │   │                   │   │   ├── CriarEstadioRequestMapper.java
    │       │   │                   │   │   ├── EstadioEnderecoResponseMapper.java
    │       │   │                   │   │   └── EstadioResponseMapper.java
    │       │   │                   ├── exception
    │       │   │                   │   ├── EstadioApiExceptionHandler.java
    │       │   │                   │   ├── EstadioJaExisteException.java
    │       │   │                   │   └── EstadioNaoEncontradoException.java
    │       │   │                   ├── model
    │       │   │                   │   └── Estadio.java
    │       │   │                   ├── repository
    │       │   │                   │   └── EstadioRepository.java
    │       │   │                   ├── service
    │       │   │                   │   ├── AtualizarEstadioService.java
    │       │   │                   │   ├── BuscarEstadioService.java
    │       │   │                   │   ├── CriarEstadioService.java
    │       │   │                   │   └── EnderecoViaCepClient.java
    │       │   │                   └── util
    │       │   │                   │   └── EstadioValidator.java
    │       │   │               ├── global_exception
    │       │   │                   ├── ErroCodigo.java
    │       │   │                   ├── ErroPadrao.java
    │       │   │                   └── GlobalApiExceptionHandler.java
    │       │   │               ├── job
    │       │   │                   ├── JobCalcularRankingDiario.java
    │       │   │                   └── JobTestPrint.java
    │       │   │               ├── partida
    │       │   │                   ├── controller
    │       │   │                   │   ├── AtualizarPartidaApiController.java
    │       │   │                   │   ├── BuscarPartidaApiController.java
    │       │   │                   │   ├── CriarPartidaApiController.java
    │       │   │                   │   └── DeletarPartidaApiController.java
    │       │   │                   ├── dto
    │       │   │                   │   ├── AtualizarPartidaRequestDTO.java
    │       │   │                   │   ├── CriarPartidaRequestDTO.java
    │       │   │                   │   ├── PartidaResponseDTO.java
    │       │   │                   │   └── mapper
    │       │   │                   │   │   ├── AtualizarPartidaRequestMapper.java
    │       │   │                   │   │   ├── CriarPartidaRequestMapper.java
    │       │   │                   │   │   └── PartidaResponseMapper.java
    │       │   │                   ├── exception
    │       │   │                   │   ├── ClubeInativoException.java
    │       │   │                   │   ├── ClubesComPartidasEmHorarioMenorQue48HorasException.java
    │       │   │                   │   ├── ClubesIguaisException.java
    │       │   │                   │   ├── DataPartidaAnteriorACriacaoDoClubeException.java
    │       │   │                   │   ├── EstadioJaPossuiPartidaNoMesmoDiaException.java
    │       │   │                   │   ├── PartidaApiExceptionHandler.java
    │       │   │                   │   └── PartidaNaoEncontradaException.java
    │       │   │                   ├── model
    │       │   │                   │   └── Partida.java
    │       │   │                   ├── repository
    │       │   │                   │   └── PartidaRepository.java
    │       │   │                   ├── service
    │       │   │                   │   ├── AtualizarPartidaService.java
    │       │   │                   │   ├── BuscarPartidaService.java
    │       │   │                   │   ├── CriarPartidaService.java
    │       │   │                   │   └── DeletarPartidaService.java
    │       │   │                   └── util
    │       │   │                   │   ├── PartidaValidator.java
    │       │   │                   │   └── Resultado.java
    │       │   │               ├── ranking
    │       │   │                   ├── controller
    │       │   │                   │   └── RankingApiController.java
    │       │   │                   ├── model
    │       │   │                   │   ├── Ranking.java
    │       │   │                   │   ├── RankingGols.java
    │       │   │                   │   ├── RankingJogos.java
    │       │   │                   │   ├── RankingPontos.java
    │       │   │                   │   └── RankingVitorias.java
    │       │   │                   ├── service
    │       │   │                   │   └── RankingService.java
    │       │   │                   ├── strategy
    │       │   │                   │   ├── CalculadoraRankingStrategy.java
    │       │   │                   │   ├── RankingGolsStrategy.java
    │       │   │                   │   ├── RankingJogosStrategy.java
    │       │   │                   │   ├── RankingPontosStrategy.java
    │       │   │                   │   └── RankingVitoriasStrategy.java
    │       │   │                   └── util
    │       │   │                   │   ├── RankingPrinterUtil.java
    │       │   │                   │   └── TipoRanking.java
    │       │   │               ├── retrospecto
    │       │   │                   ├── controller
    │       │   │                   │   └── BuscarRetrospectoApiController.java
    │       │   │                   ├── dto
    │       │   │                   │   ├── RetrospectoAdversariosResponseDTO.java
    │       │   │                   │   ├── RetrospectoConfrontoRequestDTO.java
    │       │   │                   │   └── mapper
    │       │   │                   │   │   └── RetrospectosAdversariosMapper.java
    │       │   │                   ├── model
    │       │   │                   │   ├── Retrospecto.java
    │       │   │                   │   ├── RetrospectoAdversario.java
    │       │   │                   │   └── RetrospectoConfronto.java
    │       │   │                   └── service
    │       │   │                   │   └── BuscarRetrospectoService.java
    │       │   │               └── util
    │       │   │                   ├── FormatadorDeData.java
    │       │   │                   └── TimezoneUtil.java
    │       └── resources
    │       │   ├── application-dev.properties
    │       │   ├── application-test.properties
    │       │   └── application.properties
    │   └── test
    │       └── java
    │           └── br
    │               └── com
    │                   └── meli
    │                       └── teamcubation_partidas_de_futebol
    │                           ├── TeamcubationPartidasDeFutebolApplicationTests.java
    │                           ├── clube
    │                               ├── controller
    │                               │   ├── AtualizarClubeApiControllerTest.java
    │                               │   ├── BuscarClubeApiControllerTest.java
    │                               │   ├── CriarClubeApiControllerTest.java
    │                               │   └── InativarClubeApiControllerTest.java
    │                               ├── service
    │                               │   ├── AtualizarClubeServiceTest.java
    │                               │   ├── BuscarClubeServiceTest.java
    │                               │   ├── CriarClubeServiceTest.java
    │                               │   └── InativarClubeServiceTest.java
    │                               └── util
    │                               │   └── ClubeUtil.java
    │                           ├── estadio
    │                               ├── controller
    │                               │   ├── AtualizarEstadioApiControllerTest.java
    │                               │   ├── BuscarEstadioApiControllerTest.java
    │                               │   └── CriarEstadioApiControllerTest.java
    │                               ├── service
    │                               │   ├── AtualizarEstadioServiceTest.java
    │                               │   ├── BuscarEstadioServiceTest.java
    │                               │   └── CriarEstadioServiceTest.java
    │                               └── util
    │                               │   └── EstadioUtil.java
    │                           ├── partida
    │                               ├── controller
    │                               │   ├── AtualizarPartidaApiControllerTest.java
    │                               │   ├── BuscarPartidaApiControllerTest.java
    │                               │   ├── CriarPartidaApiControllerTest.java
    │                               │   └── DeletarPartidaApiControllerTest.java
    │                               ├── service
    │                               │   ├── AtualizarPartidaServiceTest.java
    │                               │   ├── BuscarPartidaServiceTest.java
    │                               │   ├── CriarPartidaServiceTest.java
    │                               │   └── DeletarPartidaServiceTest.java
    │                               └── util
    │                               │   └── PartidaUtil.java
    │                           ├── ranking
    │                               ├── controller
    │                               │   └── RankingApiControllerTest.java
    │                               ├── service
    │                               │   └── RankingServiceTest.java
    │                               └── util
    │                               │   └── RankingUtil.java
    │                           ├── retrospecto
    │                               ├── controller
    │                               │   └── BuscarRetrospectoApiControllerTest.java
    │                               ├── service
    │                               │   └── BuscarRetrospectoServiceTest.java
    │                               └── util
    │                               │   ├── RetrospectoPrintUtil.java
    │                               │   └── RetrospectoUtil.java
    │                           └── util
    │                               ├── JsonUtil.java
    │                               └── PrintUtil.java
├── partidas-de-futebol-mensageria
    ├── pom.xml
    └── src
    │   └── main
    │       ├── java
    │           └── br
    │           │   └── com
    │           │       └── meli
    │           │           └── partidas_de_futebol_mensageria
    │           │               ├── PartidasDeFutebolMensageriaApplication.java
    │           │               ├── clube
    │           │                   ├── config
    │           │                   │   ├── RabbitMQCriarClubeConfig.java
    │           │                   │   └── RabbitMQExchangeClubeConfig.java
    │           │                   ├── controller
    │           │                   │   └── CriarClubeController.java
    │           │                   ├── dto
    │           │                   │   ├── ClubeEvent.java
    │           │                   │   └── CriarClubeRequestDTO.java
    │           │                   ├── producer
    │           │                   │   └── CriarClubeProducer.java
    │           │                   └── util
    │           │                   │   ├── ClubeEventStatus.java
    │           │                   │   └── ClubeEventType.java
    │           │               ├── global_exception
    │           │                   ├── ErroCodigo.java
    │           │                   ├── ErroPadrao.java
    │           │                   └── GlobalApiExceptionHandler.java
    │           │               └── global_rabbitmq
    │           │                   └── RabbitMQJsonConfig.java
    │       └── resources
    │           └── application.properties
├── pom.xml
└── postman
    └── teamcubation-partidas-de-futebol.postman_collection.json

```
