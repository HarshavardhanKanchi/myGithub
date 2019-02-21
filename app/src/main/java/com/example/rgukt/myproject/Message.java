package com.example.rgukt.myproject;

public class Message {
    String msgId;
    String msgSender;
    String msgSenderVillage;
    String msgText;

    public Message(){

    }

    public Message(String msgId,String msgSender, String msgSenderVillage, String msgText) {
        this.msgSender = msgSender;
        this.msgSenderVillage = msgSenderVillage;
        this.msgText = msgText;
        this.msgId=msgId;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public String getMsgSenderVillage() {
        return msgSenderVillage;
    }

    public String getMsgText() {
        return msgText;
    }

    public String getMsgId() {
        return msgId;
    }
}
