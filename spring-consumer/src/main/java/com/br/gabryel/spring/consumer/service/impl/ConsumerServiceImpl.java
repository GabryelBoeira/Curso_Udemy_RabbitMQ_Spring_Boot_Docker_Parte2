package com.br.gabryel.spring.consumer.service.impl;

import com.br.gabryel.spring.consumer.model.Message;
import com.br.gabryel.spring.consumer.service.ConsumerService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void action(Message message) {
        if ("teste".equalsIgnoreCase(message.getText())) {
            throw new AmqpRejectAndDontRequeueException("erro");
        }

        System.out.println(message.getText());
    }
}
