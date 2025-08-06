# Documentação Jobs & Agendamentos

## Introdução

Jobs agendados utilizando Spring @Scheduled, com cron configurável via application.properties.

## Implementações

- `@EnableScheduling` ativo na aplicação principal.
- Variáveis no .properties para customizar frequência:
    - `job.teste-print.cron`: job de teste, imprime log do horário de execução a cada 10s (ajustável)
    - `job.calcular-ranking-diario.cron`: job principal, imprime ranking todo dia à meia-noite (ajustável).

## Classes

- `JobTestePrint`: simples, imprime mensagem para validação do scheduler.
- `JobCalcularRankingDiario`: executa ranking diário via service.
- `RankingPrinterUtil`: agrupador de métodos de cálculo e impressão dos rankings.
- `TimezoneUtil`: utilitário para facilitar manipulação de timezones em jobs.

## Observações

- Consolide a frequência dos jobs através dos properties para facilitar testes/desenvolvimento.
- Observe execução/prints diretamente pelos logs (não há armazenamento/resultados via API).

---