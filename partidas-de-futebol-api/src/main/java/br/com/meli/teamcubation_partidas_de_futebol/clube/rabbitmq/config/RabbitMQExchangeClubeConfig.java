package br.com.meli.teamcubation_partidas_de_futebol.clube.rabbitmq.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQExchangeClubeConfig {
    @Value("${rabbitmq.exchange.clube}")
    private String clubeExchange;

    @Value("${rabbitmq.dlq.exchange.clube}")
    private String dlqExchange;

    @Bean
    public TopicExchange clubeExchange() {
        return new TopicExchange(clubeExchange);
    }

    @Bean
    public TopicExchange dlqExchange() {
        return new TopicExchange(dlqExchange);
    }
}
