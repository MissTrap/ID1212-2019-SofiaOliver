

package bootstrapNode.controller;
import bootstrapNode.connections.Connection;
import bootstrapNode.model.BootstrapNode;

import java.net.*;

public class Controller extends Thread {

    private BootstrapNode boot;

    public void Controller(Socket socket, Connection connection){
        boot = new BootstrapNode(socket, connection);
        boot.newUpdate();
        boot.start();
    }

    public void notifyPlayer(){
        boot.newUpdate();
    }
}
