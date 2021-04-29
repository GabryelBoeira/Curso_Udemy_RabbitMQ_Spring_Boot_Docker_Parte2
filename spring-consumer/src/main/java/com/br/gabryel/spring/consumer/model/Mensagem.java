package com.br.gabryel.spring.consumer.model;

public class Mensagem {

    private String text;

    public Mensagem() {
    }

    public Mensagem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
