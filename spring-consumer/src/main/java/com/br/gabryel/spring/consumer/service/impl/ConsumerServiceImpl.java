package com.br.gabryel.spring.consumer.service.impl;

import com.br.gabryel.spring.consumer.model.Mensagem;
import com.br.gabryel.spring.consumer.service.ConsumerService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void action(Mensagem mensagem) {

        if ("teste".contains(mensagem.getText()) ) {
            throw new AmqpRejectAndDontRequeueException("erro");
        }

        System.out.println(mensagem.getText());
    }
}
