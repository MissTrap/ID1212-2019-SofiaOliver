package client.modelLayer;

/**
 * The class ObjectConnection is an DTO for the connection-addresses that are active in the game.
 */
public class ObjectConnection extends Thread {
    private String ip; private int port;

    ObjectConnection(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public String getIp(){ return ip; }

    public int getPort(){ return port; }
}
