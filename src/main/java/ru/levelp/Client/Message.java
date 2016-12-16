package ru.levelp.Client;

import javax.persistence.*;
import java.sql.Timestamp;

public class Message {
    private String sender;
    private String receiver;
    private String body;
    private long id;
    private Timestamp timestamp;




    public Message() {
    }

    public Message(String sender, String receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;

        timestamp = new Timestamp(System.currentTimeMillis());

    }


    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
