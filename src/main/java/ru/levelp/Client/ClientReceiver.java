package ru.levelp.Client;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Timestamp;


public class ClientReceiver extends Thread {
    private BufferedReader serverReader;
    private Gson gson;
    private Message message;
    private boolean alive=true;
    public ClientReceiver(BufferedReader serverReader,Gson gson) {
        this.serverReader = serverReader;
        this.gson=gson;
    }

    @Override
    public void run() {
        try {
            while (alive){
                //Читаем сообщения с потока
                String inputMessage = serverReader.readLine();
                message = gson.fromJson(inputMessage, Message.class);
                //Показываем время без милисекунд
                //При упаковке в Json, Timestamp теряет значение милисекунд
                //Вместо 2016-12-15 12:05:48.123 становится 2016-12-15 12:05:48.0
                //поэтому отбрасываем два последних символа
                inputMessage = message.getTimestamp().toString();
                inputMessage = inputMessage.substring(0,inputMessage.length()-2);
                //Выводим сообщение на консоль
                inputMessage = inputMessage+" "+message.getSender()+": "+message.getBody();
                System.out.println(inputMessage);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stopReceiver(){alive=false;}

}
