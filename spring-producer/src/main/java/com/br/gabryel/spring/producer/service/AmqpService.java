package com.br.gabryel.spring.producer.service;

import com.br.gabryel.spring.producer.model.Mensagem;

public interface AmqpService {

    void SendToConsumer(Mensagem mensagem);
}
