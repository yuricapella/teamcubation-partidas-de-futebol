package br.com.meli.partidas_de_futebol_mensageria.clube.producer;

import br.com.meli.partidas_de_futebol_mensageria.clube.dto.ClubeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CriarClubeProducer.class);

    @Value("${rabbitmq.exchange.clube}")
    private String exchangeClube;

    @Value("${rabbitmq.routing-key.criar.clube}")
    private String criarClubeRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public CriarClubeProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCriarClube(ClubeEvent event) {
        LOGGER.info("Clube event enviado para o RabbitMQ: {}", event);
        rabbitTemplate.convertAndSend(exchangeClube, criarClubeRoutingKey, event);
    }
}
