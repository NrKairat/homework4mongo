package ru.levelp.dao;

import ru.levelp.Server.Message;

import java.util.List;

/**
 * Created by кайрат on 16.12.2016.
 */
public interface MessageDAO {
    String FIELD_ID = "id";
    String FIELD_SENDER = "sender";
    String FIELD_RECEIVER = "receiver";
    String FIELD_TIMESTAMP = "timestamp";

    void addMessage(Message message);

    List<Message> getAllMessage();

    void deleteUser(long id);

    List<Message> getHistory(String name);

    void deleteAll();
}
