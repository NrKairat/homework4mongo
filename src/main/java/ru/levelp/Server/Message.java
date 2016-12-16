package ru.levelp.Server;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Message {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "sender")
    private String sender;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "body")
    private String body;
    @Column(name = "timestamp")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Timestamp timestamp;

//    private long precisionTime;

    public Message() {
    }

    public Message(String sender, String receiver, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.body = body;

//        precisionTime = System.currentTimeMillis();
//        timestamp = new Timestamp(precisionTime);
        timestamp = new Timestamp(System.currentTimeMillis());

    }

//    public void setPrecisionTime() {
//        timestamp = new Timestamp(precisionTime);
//    }

    public long getId() {
        return id;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
