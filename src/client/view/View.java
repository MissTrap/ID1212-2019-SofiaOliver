//*Created by Sofia Söderström and Oliver Wilén 2019-10-29*//
//*the view might need to have more of the view?*//

package client.view;
import client.controller.Controller;

public class View extends Thread{
    Controller c;

    public View(){views();}

    public void views(){
        c = new Controller(this);
        firstMessages();
        c.start();
        c.inputUser();
    }

    public void firstMessages(){
        System.out.println("Welcome to the Rock-Paper-Scissors Game " +
                "\n You may at any time type 'quit' to end the game, You may also quit by typing 'q'. " +
                "\n Type 'rock', 'paper' or 'scissors' to chose your move, or make your move by typing 'r', 'p' or 's'. " +
                "\n Complete the action by pressing enter. " +
                "\n Points will be given to winners when every player online has made their moves. ");
    }

    public void initNumbers(int x, int y){ System.out.println("There is " + x + " players online and your score is " + y + ". "); }

    public void updateNr(int x){ System.out.println("Number of players online is: " + x + ". "); }

    public void infoAlone(){ System.out.println("You are the only one online at the moment, awaiting other players... "); }

    public void pointsAreGiven(int x, int y, int z){ System.out.println("All (" + x + ") players have made their moves. " + "\n You won against " + y + " players and now have " + z + " points. "); }

    public void moveMadeWait(){ System.out.println("You have made your turn and now we await the other players moves. "); }
}
