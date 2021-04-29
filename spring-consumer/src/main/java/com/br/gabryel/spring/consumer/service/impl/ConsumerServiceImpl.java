package com.br.gabryel.spring.consumer.service.impl;

import com.br.gabryel.spring.consumer.model.Mensagem;
import com.br.gabryel.spring.consumer.service.ConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Override
    public void action(Mensagem mensagem) {

        if ("teste".contains(mensagem.getText()) ) {
            throw new AmqpRejectAndDontRequeueException("erro");
        }

        log.info("ConsumerServiceImpl From action: Received <{}>", mensagem);
    }
}
