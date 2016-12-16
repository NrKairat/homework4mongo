package ru.levelp.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler extends Thread {
    private Socket socket;
    private SenderWorker senderWorker;
    private ServerExample server;
    //Имя клиента
    private String nickName;
    private boolean isRegistered = false;



    public ClientHandler(ServerExample server, Socket socket,int id) {
        this.server = server;
        this.socket = socket;
        //Временное имя клиента
        nickName = ""+id;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public void run() {
        try {
            //Создание читателей, писателей из потока
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            //Создание отправителя в отдельном потоке
            senderWorker = new SenderWorker(writer, server.gson);
            senderWorker.start();

            String inputMessage;

            //Регистрация имени польователя
            while (!isRegistered){

                inputMessage = reader.readLine();
                Message message = server.gson.fromJson(inputMessage, Message.class);
                inputMessage = message.getBody();

                //Проверяем имя на уникальность
                if(server.isNameFree(inputMessage)){
                    nickName = inputMessage;
                    inputMessage = "Поздравляем. Ваше имя в чате: "+inputMessage;
                    message = new Message("Сервер",nickName,inputMessage);
                    sendMessage(message);
                    isRegistered=true;
                }
                else{
                    inputMessage = "Извините, но имя уже занято. Введите новое ";
                    message = new Message("Сервер",nickName,inputMessage);
                    sendMessage(message);
                }
            }
            inputMessage=nickName+" присоединился к нашему чату";
            //Отправка сообщения всем
            server.multiSendWorker.addMessage(new Message("Сервер",null,inputMessage));

            ////////////////////////////////////////////////////////////////////////////////////////////////
            boolean socketNotNull = true;
            //Чтение сокета клиента и получение от него сообщений
            while (socketNotNull){

                try{
                    inputMessage = reader.readLine();
                    if(!(inputMessage==null)){
                        server.parseMessage(inputMessage,this);
                    }

                }
                catch (SocketException e){

                    inputMessage="Пользователь "+nickName+" отключился от чата";
                    server.multiSendWorker.addMessage(new Message("Сервер",null,inputMessage));
                    socketNotNull = false;
                }


            }
            //Удаление пользователя из коллекции. Закрытие потока отправителя(senderWorker),
            //(writer), (reader) и сокета
            server.disconnectClient(this);
            senderWorker.stopWorker();
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message) {

        senderWorker.addMessage(message);

    }


}
