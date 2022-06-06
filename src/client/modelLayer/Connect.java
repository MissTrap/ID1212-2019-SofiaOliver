//*Created by Sofia Söderström and Oliver Wilén 2019-10-29*//

package client.modelLayer;

import client.controller.Controller;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * Model class for each unique client called 'Connect'
 * */
public class Connect extends Thread{
    //init
    Controller controller;
    private Socket socket; private Socket[] sockets;
    private InputStream input; private DataInputStream[] inputs;
    private OutputStream output; private DataOutputStream[] outputs;
    private String inp = ""; private String that = ""; private String ip; private String myIP;
    private int len; private int score = 0; private int check; private int port; private int id = 0; private int numberWins = 0;
    private boolean state = false; private boolean youMayMove = true; private boolean first = true;
    private ArrayList<ObjectConnection> allPlayers = new ArrayList<>();

    //connect and startup (runs the first time)
    public void Connect(Controller controller){
        this.controller = controller;
        getConnections();
    }

    //gets info from bootstrapNode about other players ip-addresses.
    public void getConnections(){
        try{
            this.socket = new Socket("10.37.201.247", 8888); //localhost ip-address and port 8888. Bootstrap Node.
            input = socket.getInputStream();
            output = socket.getOutputStream();
            Scanner scanner = new Scanner(input, "UTF-8");
            while(scanner.hasNextLine()){
                if(first){
                    myIP = scanner.nextLine();
                    System.out.println(myIP);
                } else{
                    String[] parts = scanner.nextLine().split(",");
                    ip = parts[0];
                    System.out.println(ip);
                    if(!ip.equals(myIP)){
                        port = 8888;//Integer.parseInt(parts[1]);
                        id++;
                        addConnection(new ObjectConnection(ip, port));
                    } } }
            if(allPlayers.size() < 1){controller.alone();}
            else{controller.firstU(allPlayers.size(), score);}
            //start();
            OtherPlayers();
        }
        catch(Exception e){e.printStackTrace();}
    }

    //add connection to a player if he/she is not already in the list of players
    private void addConnection(ObjectConnection objectConnection){
        boolean bool = true;
        for (int i = 0; i < allPlayers.size()-1; i++) { if(allPlayers.get(i).equals(objectConnection)){bool = false;} }
        if(bool){
            ip = objectConnection.getIp();
            port = objectConnection.getPort();
            controller.updateNumberOfPlayers(allPlayers.size());
            allPlayers.add(objectConnection);
            try{ sockets[id] = new Socket(ip, port); }
            catch(Exception e){e.printStackTrace();}
        } }

    //public void run(){ inputFromUser(); }

    //Always waiting for new moves from the other players, called after you have made your move.
    public void OtherPlayers(){
        try{
            check = 0;
            len = allPlayers.size();
            while(!state){
                for (int i = 0; i < len; i++) { if(inputs[i].available() == 1 ){ check++; } }
                if(check == len){ state = true; }
            }
            String[] moves = new String[len];
            for (int i = 0; i < len; i++) {
                inputs[i] = new DataInputStream(sockets[i].getInputStream());
                moves[i] = inputs[i].readUTF();
                if (moves[i].equalsIgnoreCase("q") || moves[i].equalsIgnoreCase("quit")) {
                    inputs[i].close();
                    outputs[i].close();
                    sockets[i].close();
                }
                if (moves[i].equalsIgnoreCase("rock")) { moves[i] = "r"; }
                if (moves[i].equalsIgnoreCase("paper")) { moves[i] = "p"; }
                if (moves[i].equalsIgnoreCase("scissors")) { moves[i] = "s"; }
            }
            numberWins = 0;
            if (that.equalsIgnoreCase("rock") || that.equalsIgnoreCase("r")) {
                for (int i = 0; i < len; i++) { if (moves[i].equalsIgnoreCase("s")) { score++; numberWins++; } }
            }
            if (that.equalsIgnoreCase("paper") || that.equalsIgnoreCase("p")) {
                for (int i = 0; i < len; i++) { if (moves[i].equalsIgnoreCase("r")) { score++; numberWins++; } }
            }
            if (that.equalsIgnoreCase("scissors") || that.equalsIgnoreCase("s")) {
                for (int i = 0; i < len; i++) { if (moves[i].equalsIgnoreCase("p")) { score++; numberWins++; } }
            }
            controller.roundPlayed(len, numberWins, score);
            state = false;
            youMayMove = true;
        }
        catch(Exception e){e.printStackTrace();}
    }

    //Gets input from user. Always running.
    public void inputFromUser(){
        Scanner in = new Scanner(System.in);
        while(true){
            if(in.hasNextLine()){
                inp = in.nextLine();
                if(inp.equalsIgnoreCase("quit") || inp.equalsIgnoreCase("q")){
                    try{
                        socket.close();
                        for (int i = 0; i < len; i++) {
                            inputs[i].close();
                            outputs[i].close();
                            sockets[i].close();
                        } }
                    catch(Exception e){e.printStackTrace();}
                    break;
                }
                if(control(inp) != 1){
                    System.out.println("1 invalid input, did you try to hack us?! You may try to make a valid input.");
                }
                if(youMayMove && (inp.equalsIgnoreCase("r") || inp.equalsIgnoreCase("p") || inp.equalsIgnoreCase("s") ||
                        inp.equalsIgnoreCase("rock") || inp.equalsIgnoreCase("paper") || inp.equalsIgnoreCase("scissors"))){
                    try{
                        for (int i = 0; i < len; i++) {
                            outputs[i].writeUTF(inp);
                            outputs[i].flush();
                            that = inp;
                            youMayMove = false;
                            controller.youPlayed();
                            OtherPlayers();
                        } }
                    catch(Exception e){e.printStackTrace();}
                } } } }

    private int control(String input){
        while(input.length() != 0){
            inp = input.substring(0, 1);
            input = input.substring(1);
            if(inp.equals(";") || inp.equals(".") || inp.equals("(") || inp.equals(")")){
                return 0;
            } }
        return 1;
    }
}
