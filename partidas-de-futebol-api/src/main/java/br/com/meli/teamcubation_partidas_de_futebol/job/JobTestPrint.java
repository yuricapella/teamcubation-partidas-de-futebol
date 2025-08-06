package br.com.meli.teamcubation_partidas_de_futebol.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobTestPrint {

    @Scheduled(cron = "${job.teste-print.cron}", zone = "America/Sao_Paulo")
    public void rodarJob() {
        System.out.println("Job executado às: " + java.time.LocalTime.now());
    }
}
