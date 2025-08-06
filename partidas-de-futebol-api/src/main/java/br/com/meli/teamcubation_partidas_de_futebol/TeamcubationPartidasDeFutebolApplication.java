package br.com.meli.teamcubation_partidas_de_futebol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TeamcubationPartidasDeFutebolApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamcubationPartidasDeFutebolApplication.class, args);
    }

}
