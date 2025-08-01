package br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.consumer;

import br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.dto.ClubeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CriarClubeDlqConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CriarClubeDlqConsumer.class);

    @RabbitListener(queues = "${rabbitmq.dlq.queue.criar.clube}")
    public void consume(ClubeEvent clubeEvent){
        LOGGER.error("Mensagem foi para a DLQ criar_clube_queue.dlq: {}", clubeEvent.toString());
        // ações adicionais: notificar, trigger para correção manual, etc
    }
}
