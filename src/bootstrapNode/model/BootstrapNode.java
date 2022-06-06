//*Created by Sofia Söderström and Oliver Wilén 2019-10-29*//
//*Bootstrap node. Will act as a lookup service, registering and distributing ip addresses of player nodes.*//

package bootstrapNode.model;

import bootstrapNode.connections.Connection;

import java.io.*;
import java.net.*;
import java.util.*;



public class BootstrapNode extends Thread{
    //init
    private Socket socket; private Connection con;
    private InputStream input;
    private PrintWriter out;
    private int len;

    public BootstrapNode(Socket socket, Connection connection){
        System.out.println("Bootstrap node has init. ");
        this.socket = socket;
        this.con = connection;
        try{
            input = socket.getInputStream();
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException e){ e.printStackTrace(); }
        out.println(ipInfo());

    }

    public void run(){update();}

    //only run once
    public InetAddress ipInfo(){ return socket.getInetAddress(); }

    //Runs directly after client joins and every time any client joins or leaves.
    public void newUpdate(){
        try{
            ArrayList<Socket> listPlayers = con.getListOfPlayers();
            len = listPlayers.size();
            for (int i = 0; i < len; i++) {
                out.println(listPlayers.get(i).getInetAddress() );
                System.out.println(listPlayers.get(i).getInetAddress());
                out.flush();
            }
        }
        catch(Exception e){e.printStackTrace();}
    }

    //Always runs, checks if client quits.
    private void update(){
        try{
            while (true) {
                if(input.read() == -1){
                    con.deletePlayer(socket);
                    con.notifyObservers();
                    break;
                }
            }
        }
        catch (Exception e) { e.printStackTrace(); }
    }


}
