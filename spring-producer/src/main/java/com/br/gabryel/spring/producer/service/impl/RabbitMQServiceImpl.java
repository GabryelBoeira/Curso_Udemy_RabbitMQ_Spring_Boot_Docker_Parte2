package com.br.gabryel.spring.producer.service.impl;

import com.br.gabryel.spring.producer.amqp.AmqpProducer;
import com.br.gabryel.spring.producer.model.Mensagem;
import com.br.gabryel.spring.producer.service.AmqpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements AmqpService {

    @Autowired
    private AmqpProducer<Mensagem> amqp;

    @Override
    public void SendToConsumer(Mensagem mensagem) {

        amqp.producer(mensagem);
    }
}
