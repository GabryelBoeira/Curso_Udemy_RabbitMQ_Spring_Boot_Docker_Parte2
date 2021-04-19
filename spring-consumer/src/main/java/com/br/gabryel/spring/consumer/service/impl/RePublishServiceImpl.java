package com.br.gabryel.spring.consumer.service.impl;

import com.br.gabryel.spring.consumer.amqp.AmqpRePublish;
import com.br.gabryel.spring.consumer.service.RePublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RePublishServiceImpl implements RePublishService {

    @Autowired
    private AmqpRePublish amqpRePublish;

    @Override
    public void repost() {

        amqpRePublish.rePublish();
    }
}
