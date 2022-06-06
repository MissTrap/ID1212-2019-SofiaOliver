package bootstrapNode.connections;

import bootstrapNode.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Connection extends Thread {
    ServerSocket socket;
    ArrayList<Socket> clientConnections = new ArrayList<>();
    private int port = 8888;
    Controller controller;

    public Connection(){
        try{
            socket = new ServerSocket(port);
            Socket sock = null;

            while(true){
                controller = new Controller();
                sock = socket.accept();
                controller.Controller(sock, this);
                clientConnections.add(sock);
                if(clientConnections.size()>0){
                    notifyObservers();
                }
            }
        } catch(IOException e){e.printStackTrace();}
    }

    public ArrayList<Socket> getListOfPlayers(){
        return clientConnections;
    }

    public void deletePlayer(Socket socketNode){
        clientConnections.remove(socketNode);
        notifyObservers();
    }

    public void notifyObservers(){
        clientConnections.forEach(sock -> controller.notifyPlayer());
    }

    public static void main(String[] args){new Connection();}}
