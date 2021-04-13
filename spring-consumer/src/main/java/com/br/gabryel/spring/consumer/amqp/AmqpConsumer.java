package com.br.gabryel.spring.consumer.amqp;

public interface AmqpConsumer<T> {

    void consumer(T t);
}
