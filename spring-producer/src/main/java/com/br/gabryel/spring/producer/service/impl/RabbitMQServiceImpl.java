package com.br.gabryel.spring.producer.service.impl;

import com.br.gabryel.spring.producer.amqp.AmqpProducer;
import com.br.gabryel.spring.producer.model.Message;
import com.br.gabryel.spring.producer.service.AmqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements AmqpService {

    @Autowired
    private AmqpProducer<Message> amqp;

    @Override
    public void SendToConsumer(Message message) {

        amqp.producer(message);
    }
}
