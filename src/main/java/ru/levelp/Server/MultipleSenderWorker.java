package ru.levelp.Server;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class MultipleSenderWorker extends Thread {
    private PriorityQueue<Message> queue;
    private boolean alive;
    private ArrayList<ClientHandler> clients;
    private ServerExample serverExample;

    //Получаем экземпляр сервера и список клиентов
    public MultipleSenderWorker(ServerExample serverExample, ArrayList<ClientHandler> clients) {
        this.clients = clients;
        queue = new PriorityQueue<Message>();
        this.serverExample=serverExample;
    }

    @Override
    public void run() {
        alive = true;
        while (alive) {
            if (!queue.isEmpty()) {
                Message message = queue.poll();
                //Если клиент не является отправителем, то отправляем сообщение
                for (ClientHandler c : clients) {
                    if (!c.getNickName().equals(message.getSender())) {
                        //c.sendMessage(message.getSender(),message.getReceiver(),message.getBody());
                        c.sendMessage(message);
                    }
                }
                serverExample.printMessage(message.getSender()+": "+message.getBody());

            } else {
                Thread.yield();
            }
        }
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public void stopWorker() {
        alive = false;
    }
}
