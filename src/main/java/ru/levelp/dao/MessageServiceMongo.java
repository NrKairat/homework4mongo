package ru.levelp.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.CriteriaContainer;
import org.mongodb.morphia.query.Query;
import ru.levelp.Server.Message;

import java.util.List;

/**
 * Created by кайрат on 16.12.2016.
 */
public class MessageServiceMongo implements MessageDAO {
    private Datastore db;
    private Message message;

    public MessageServiceMongo() {
        Morphia morphia = new Morphia();
        db = morphia.createDatastore(
                new MongoClient("localhost"), "leveluptest");
        db.ensureIndexes();
    }

    public Datastore request() {
        return db;
    }

    public void addMessage(Message message) {
        db.save(message);
    }

    public List<Message> getAllMessage() {
        return request().createQuery(Message.class)
                .asList();
    }


    public void deleteUser(long id) {
        Message message = request().createQuery(Message.class)
                .field(FIELD_ID).equal(id)
                .get();
        request().delete(message);

    }

    public List<Message> getHistory(String name) {


        Query<Message> query = request().createQuery(Message.class);

        Criteria c1 = request().createQuery(Message.class).criteria(FIELD_SENDER).equal(name);
        Criteria c2 = request().createQuery(Message.class).criteria(FIELD_RECEIVER).equal(name);

        query.or(c1,c2);
        query.order(FIELD_TIMESTAMP);

        List<Message> list = query.asList();
        return list;
    }

    public void deleteAll() {

    }
}
