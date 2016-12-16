package ru.levelp.Server;
/**
Многопоточный чат клиент c возможностью:
 - отправки сообщений в личку @name:text
 - получения истории сообщений @S:g из базы данных, отсортированных по времени
 */
import com.google.gson.Gson;
import ru.levelp.dao.MessageDAO;
import ru.levelp.dao.MessageServiceMongo;
import ru.levelp.dao.MessageServiceMySQL;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerExample {
    //Список клиентов
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    //Уникальные id клиентов(Временные имена клиентов)
    private int id;
    //Имя сервера
    private String name = "S";
    //Класс который отправляет сообщения всем клиентам в отдельном потоке
    public MultipleSenderWorker multiSendWorker;
    public Gson gson=new Gson();

    //Выбор бд между MongoDB или MySQL, для этого выбираем либо new MessageServiceMongo()
    //либо new MessageServiceMySQL()
    private MessageDAO messageService = new MessageServiceMongo();

    public static void main(String[] args) {
        new ServerExample().start();
    }

    private void start() {
        try {
            //Создание сокета
            ServerSocket serverSocket = new ServerSocket(7071);


            multiSendWorker = new MultipleSenderWorker(this,clients);
            multiSendWorker.start();

            String serverMessage="Сервер запущен...";
            //Вывод сообщения на консоль
            printMessage(serverMessage);


            //Принимаем клиентский сокет, отправляем его обрабатываться в отдельный поток, добавляем этот поток
            // в коллекцию. Затем возвращаемся назад и ждем нового клиента.
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, clientSocket,id++);
                clientHandler.start();
                clients.add(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Метод отправки личного сообщения для клиента
    public void sendToClient(Message message) {
        //Перебираем коллекцию и ищем сходство поля Получатель(getReceiver()) в объекте message и
        // имени клиента (getNickName())
        for (ClientHandler c : clients) {
            if (c.getNickName().equals(message.getReceiver())) {
                //Добавление сообщения в "приоритетную очередь" для конкретного клиента
                c.sendMessage(message);
            }
        }
    }
    public void sendToClientHistory(Message message,String client) {
        //Перебираем коллекцию и ищем сходство поля Получатель(getReceiver()) в объекте message и
        // имени клиента (getNickName())
        for (ClientHandler c : clients) {
            if (c.getNickName().equals(client)) {
                //Добавление сообщения в очередь для конкретного клиента
                c.sendMessage(message);
            }
        }
    }

    //Проведение необходимых процедур для отключения клиента
    public void disconnectClient(ClientHandler clientHandler) {
        //Удаления клиента из коллекции
        clients.remove(clientHandler);
    }
    //Парсинг сообщения из Json-a и отправка его нужным пользователям
    public void parseMessage(String inputMessage, ClientHandler clientHandler){
        //Получение объекта из строки Json-a
        Message message = gson.fromJson(inputMessage, Message.class);
        //Вручную задаем Отправителя (setSender)т.к. объект Client не знает своего собственного имени
        message.setSender(clientHandler.getNickName());


        //Если поле Получателя пустое - отправляем всем, иначе - конкретному клиенту
        //Если получатель не является сервером, то сохраняем сообщение в бд messageService.addMessage()
        if(message.getReceiver()==null){
            multiSendWorker.addMessage(message);
            //Сохраняем сообщение в БД
            messageService.addMessage(message);
        }
        else{
            if(message.getReceiver().equals(name)){
                //Получаем историю сообщений
                requestToServer(message);
            }
            else{
                sendToClient(message);
                //Сохраняем сообщение в БД
                messageService.addMessage(message);
            }

        }



    }

    //Запрос серверу, если получателем указан сервер
    private void requestToServer(Message message) {

        List<Message> list = new ArrayList<Message>();
        //Если запрос getHistory
        if(message.getBody().equals("g")){
            //Получаем коллекцию сообщений пользователя
            list = (ArrayList<Message>) messageService.getHistory(message.getSender());

            sendToClient(new Message("Server",message.getSender(),"Ваша история сообщений "));


            //Перебираем коллекцию и отправляем сообщение клиенту
            for(int i =0; i<list.size();i++){

                sendToClientHistory(list.get(i),message.getSender());

            }
            sendToClient(new Message("Server",message.getSender()," Конец истории сообщений"));
        }

    }

    //Проверка уникальности имени. Если имя свободно, то возвращается true
    public boolean isNameFree(String name){
        for (ClientHandler c : clients) {
            //Сравниваем имена ClientHandler-ов с данным именем
            if (c.getNickName().equals(name)) {
                return false;
            }
        }

        return true;
    }
    //Вывод сообщения на консоль
    public void printMessage(String message){
        System.out.println(message);
    }

}
