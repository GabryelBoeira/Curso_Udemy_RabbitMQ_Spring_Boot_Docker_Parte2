package com.br.gabryel.spring.consumer.amqp.impl;

import com.br.gabryel.spring.consumer.amqp.AmqpConsumer;
import com.br.gabryel.spring.consumer.model.Mensagem;
import com.br.gabryel.spring.consumer.service.ConsumerService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumerImpl implements AmqpConsumer<Mensagem> {

    @Autowired
    private ConsumerService consumerService;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.request.routing-key.producer}")
    public void consumer(Mensagem mensagem) {

        try {
            consumerService.action(mensagem);
        } catch (Exception ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
