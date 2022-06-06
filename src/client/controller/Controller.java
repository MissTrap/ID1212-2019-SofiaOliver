//*Created by Sofia Söderström and Oliver Wilén 2019-10-29*//

package client.controller;
import client.modelLayer.Connect;
import client.view.View;

public class Controller extends Thread{
    private final Connect con = new Connect();
    private View view;

    public Controller(View view){this.view = view;}

    public void connecting(){con.Connect(this);}
    public void inputUser(){con.inputFromUser();}

    public void run(){connecting();}

    public void firstU(int x, int y){ view.initNumbers(x, y); }
    public void updateNumberOfPlayers(int x){ view.updateNr(x); }
    public void alone(){view.infoAlone(); }
    public void roundPlayed(int x, int y, int z){ view.pointsAreGiven(x, y, z); }
    public void youPlayed(){ view.moveMadeWait(); }
}
