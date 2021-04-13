package com.br.gabryel.spring.producer.amqp.impl;

import com.br.gabryel.spring.producer.amqp.AmqpProducer;
import com.br.gabryel.spring.producer.model.Message;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProducerRabbitMQ implements AmqpProducer<Message> {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;

    @Value("${spring.rabbitmq.request.routing-key.producer}")
    private String queue;

    @Override
    public void producer(Message message) {

        try {

            //realiza o envio de mensagem
            rabbitTemplate.convertAndSend(exchange, queue, message);
        } catch (Exception ex) {

            // Se ocorrer algum erro envia para a pilha de erros "deadLetter" automaticamente
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
