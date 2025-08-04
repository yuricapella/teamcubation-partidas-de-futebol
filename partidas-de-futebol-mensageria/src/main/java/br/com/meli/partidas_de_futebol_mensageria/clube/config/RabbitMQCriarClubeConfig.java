package br.com.meli.partidas_de_futebol_mensageria.clube.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class RabbitMQCriarClubeConfig {
    @Value("${rabbitmq.queue.criar.clube}")
    private String criarClubeQueue;

    @Value("${rabbitmq.routing-key.criar.clube}")
    private String criarClubeRoutingKey;

    @Bean
    public Queue criarClubeQueue() {
        return new Queue(criarClubeQueue);
    }

    @Bean
    public Binding criarClubeBinding(Queue criarClubeQueue, TopicExchange clubeExchange) {
        return BindingBuilder
                .bind(criarClubeQueue)
                .to(clubeExchange)
                .with(criarClubeRoutingKey);
    }
}
