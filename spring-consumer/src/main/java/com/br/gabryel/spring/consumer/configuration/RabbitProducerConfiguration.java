package com.br.gabryel.spring.consumer.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitProducerConfiguration {

    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;

    @Value("${spring.rabbitmq.request.routing-key.producer}")
    private String queue;

    @Value("${spring.rabbitmq.request.dead-letter.producer}")
    private String deadLetter;

    @Value("${spring.rabbitmq.request.parking-lot.producer}")
    private String parkingLot;

    @Bean
    Queue queue() {

        return QueueBuilder.durable(queue)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(deadLetter)
                .build();
    }

    @Bean
    Queue deadLetter() {

        return QueueBuilder.durable(deadLetter)
                .deadLetterExchange(exchange)
                .deadLetterRoutingKey(queue)
                .build();
    }

    @Bean
    Queue parkingLot() {
        return new Queue(parkingLot);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding bindingQueue() {
        return BindingBuilder.bind(queue())
                .to(directExchange())
                .with(queue);
    }

    @Bean
    Binding bindingDeadLetter() {
        return BindingBuilder.bind(deadLetter())
                .to(directExchange())
                .with(deadLetter);
    }

    @Bean
    Binding bindingParkingLot() {
        return BindingBuilder.bind(parkingLot())
                .to(directExchange())
                .with(parkingLot);
    }
}
