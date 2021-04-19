package com.br.gabryel.spring.consumer.amqp.impl;

import com.br.gabryel.spring.consumer.amqp.AmqpRePublish;
import com.br.gabryel.spring.consumer.model.Mensagem;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RePublishRabbitMQ implements AmqpRePublish {

    @Value("${spring.rabbitmq.request.exchange.producer}")
    private String exchange;

    @Value("${spring.rabbitmq.request.routing-key.producer}")
    private String queue;

    @Value("${spring.rabbitmq.request.dead-letter.producer}")
    private String deadLetter;

    @Value("${spring.rabbitmq.request.parking-lot.producer}")
    private String parkingLot;

    @Value("${spring.qtdeProcessamentoRabbitMQ}")
    private Integer qtdeProcessRabbitMQ;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String X_RETRIES_HEADER = "x-retries";

    @Override
    @Scheduled(cron = "${spring.rabbitmq.listener.time-retry}")
    public void rePublish() {
        List<Message> messageList = getMensagemDeadLetter();

        messageList.forEach(message -> {

            Map<String, Object> headerList = message.getMessageProperties().getHeaders();
            Integer retriesHeader = (Integer) headerList.get(X_RETRIES_HEADER);

            if (retriesHeader == null) {
                retriesHeader = 0;
            }

            if (qtdeProcessRabbitMQ < 3) {

                headerList.put(X_RETRIES_HEADER, retriesHeader + 1);
                rabbitTemplate.send(exchange, queue, message);
            } else {

                rabbitTemplate.send(parkingLot, message);
            }
        });

    }

    private List<Message> getMensagemDeadLetter() {

        List<Message> messageList = new ArrayList<>();
        boolean isNull;
        Message message;

        do {
            message = rabbitTemplate.receive(deadLetter);
            isNull = message != null;

            if (isNull) {
                messageList.add(message);
            }

        } while (isNull == true);

        return messageList;
    }
}
