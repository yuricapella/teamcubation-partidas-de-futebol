package br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQCriarClubeConfig {
    @Value("${rabbitmq.queue.criar.clube}")
    private String criarClubeQueue;

    @Value("${rabbitmq.routing-key.criar.clube}")
    private String criarClubeRoutingKey;

    @Value("${rabbitmq.dlq.queue.criar.clube}")
    private String criarClubeDlqQueue;

    @Value("${rabbitmq.dlq.routing-key.criar.clube}")
    private String criarClubeDlqRoutingKey;

    @Bean
    public Queue criarClubeQueue(TopicExchange dlqExchange) {
        return QueueBuilder.durable(criarClubeQueue)
                .withArgument("x-dead-letter-exchange", dlqExchange.getName())
                .withArgument("x-dead-letter-routing-key", criarClubeDlqRoutingKey)
                .build();
    }

    @Bean
    public Binding criarClubeBinding(Queue criarClubeQueue, TopicExchange clubeExchange) {
        return BindingBuilder
                .bind(criarClubeQueue)
                .to(clubeExchange)
                .with(criarClubeRoutingKey);
    }

    @Bean
    public Queue criarClubeDLQ() {
        return QueueBuilder.durable(criarClubeDlqQueue).build();
    }

    @Bean
    public Binding dlqBinding(Queue criarClubeDLQ, TopicExchange dlqExchange) {
        return BindingBuilder.bind(criarClubeDLQ)
                .to(dlqExchange)
                .with(criarClubeDlqRoutingKey);
    }
}
