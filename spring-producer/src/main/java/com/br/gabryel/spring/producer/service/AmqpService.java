package com.br.gabryel.spring.producer.service;

import com.br.gabryel.spring.producer.model.Message;

public interface AmqpService {

    void SendToConsumer(Message message);
}
