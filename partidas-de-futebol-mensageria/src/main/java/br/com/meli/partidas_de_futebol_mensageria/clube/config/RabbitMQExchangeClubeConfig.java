package br.com.meli.partidas_de_futebol_mensageria.clube.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQExchangeClubeConfig {
    @Value("${rabbitmq.exchange.clube}")
    private String clubeExchange;

    @Bean
    public TopicExchange clubeExchange() {
        return new TopicExchange(clubeExchange);
    }
}
