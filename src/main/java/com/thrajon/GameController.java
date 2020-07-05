package com.thrajon;
import java.util.Scanner;


public class GameController {

    public void startGame() {

         Scanner scanner = new Scanner(System.in);

         //Enter number of players to play
        System.out.println("Enter number of players - 2 or 4");
        int noOfPlayers = scanner.nextInt();

        //Create Player Coordiantor object
        PlayerCooridinator cooridinator = new PlayerCooridinator(1, noOfPlayers);

        Player player1 = new Player(cooridinator, scanner, 1, "Player1");
        new Thread(player1).start();
        cooridinator.setActive(1, true);

        Player player2 = new Player(cooridinator, scanner, 2, "Player2");
        new Thread(player2).start();
        cooridinator.setActive(2, true);

        if(noOfPlayers == 4)
        {
            Player player3 = new Player(cooridinator, scanner, 3, "Player3");
            new Thread(player3).start();
            cooridinator.setActive(3, true);

            Player player4 = new Player(cooridinator, scanner, 4, "Player4");
            new Thread(player4).start();
            cooridinator.setActive(4, true);
        }
    }

    public static void main(String[] args) {

        GameController controller = new GameController();
        controller.startGame();
    }

}