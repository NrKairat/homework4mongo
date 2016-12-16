package ru.levelp.Server;

import ru.levelp.dao.MessageDAO;
import ru.levelp.dao.MessageServiceMongo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by кайрат on 13.12.2016.
 */
public class Main {
//    HibernateManager hibernateManager = HibernateManager.getInstance();
//    Session session = hibernateManager.getSession();



    public static void main(String[] args) {
//        MessageService messageService = new MessageService();
//
//        Message message = new Message("Sever","Aill","Hello");
//        messageService.addMessage(message);
//        List<Message> list = messageService.getAllMessage();
//
//        for(Message mes:list){System.out.println(mes.getSender()+mes.getReceiver()+mes.getBody());}

        MessageDAO messageService = new MessageServiceMongo();
        Message message = new Message("Peter","Masha","GO5");
        Message message1 = new Message("Masha","Peter","GO4");
        Message message2 = new Message("Sasha","Masha","GO3");
        Message message3 = new Message("Masha","Sasha","GO2");
        Message message4 = new Message("Peter","Sasha","GO1");
        Message message5 = new Message("Sasha","Peter","GO0");

//        messageService.addMessage(message);
//        messageService.addMessage(message1);
//        messageService.addMessage(message2);
//        messageService.addMessage(message3);
//        messageService.addMessage(message4);
//        messageService.addMessage(message5);

//        ArrayList<Message> list = (ArrayList<Message>)messageService.getAllMessage();

//        for(Message m:list){
//            System.out.println(m.getTimestamp()+m.getSender()+m.getReceiver()+m.getBody());
//        }

        ArrayList<Message> list = (ArrayList<Message>)messageService.getHistory("Masha");
        for(Message m:list){
            System.out.println(m.getTimestamp()+m.getSender()+m.getReceiver()+m.getBody());
        }

    }
}
