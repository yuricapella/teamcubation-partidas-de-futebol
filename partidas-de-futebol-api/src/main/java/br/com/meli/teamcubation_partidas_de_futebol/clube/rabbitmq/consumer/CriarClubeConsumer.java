package br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.consumer;

import br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.dto.ClubeEvent;
import br.com.meli.teamcubation_partidas_de_futebol.clube.service.CriarClubeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeConsumer {

    private final CriarClubeService criarClubeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CriarClubeConsumer.class);

    public CriarClubeConsumer(CriarClubeService criarClubeService) {
        this.criarClubeService = criarClubeService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.criar.clube}")
    public void consume(ClubeEvent clubeEvent){
        LOGGER.info("Clube event received from RabbitMQ: {}", clubeEvent.toString());
        criarClubeService.criarClube(clubeEvent.getClube());
    }
}

