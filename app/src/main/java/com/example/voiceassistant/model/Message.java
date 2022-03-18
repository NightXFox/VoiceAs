package com.example.voiceassistant.model;

import java.util.Date;

public class Message {
    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Boolean isSend) {
        this.text = text;
        this.isSend = isSend;
        date = new Date();
    }
}
