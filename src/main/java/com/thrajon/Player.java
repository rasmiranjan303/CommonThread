package com.thrajon;

import org.omg.PortableInterceptor.NON_EXISTENT;

import java.util.Scanner;


public class Player implements Runnable {


    private int playerId;
    private String playerName;
    private PlayerCooridinator playerCooridinator;
    private Scanner scanner;

    public Player(PlayerCooridinator monitor, Scanner sc, int playerId, String playerName) {

        this.playerId = playerId;
        this.playerCooridinator = monitor;
        this.scanner = sc;
        this.playerName = playerName;
    }


    @Override
    public void run() {
        //if game is not over or player has not gone to home need to play
        while (!playerCooridinator.isGameOver() && playerCooridinator.isActive(playerId)) {
            play();
        }
    }

    private void play() {


        synchronized (playerCooridinator) {

            while (playerCooridinator.getCurrentPlayerToPlay() != playerId) {
                try {
                    playerCooridinator.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //DOUBLE CHECK FOR MAKING SURE, NO GAME IF GAME IS OVER
                if (playerCooridinator.isGameOver() || playerCooridinator.getCurrentPlayerToPlay() != playerId) {
                    return;
            }
            if (!allHome()) {
                rollDice();
            }
            setNextPlayerAndNotufy();
        }

    }

    private void setNextPlayerAndNotufy() {
        //Mark Next player who will play
        //Before setting next player need to check if he is inactive
        //Next player should not be an inactive player
        //If next player is inactive need to check next to next player until we find an active player

        int maxRetries = playerCooridinator.getTotalNumberOfPlayers();
        int retry = 1;
        int nextPlayer = -1;
        while (retry <= maxRetries) {
            int tempNextPlayer = (playerCooridinator.getCurrentPlayerToPlay() + retry) >
                    playerCooridinator.getTotalNumberOfPlayers() ?
                    (playerCooridinator.getCurrentPlayerToPlay() + retry) % playerCooridinator.getTotalNumberOfPlayers() :
                    playerCooridinator.getCurrentPlayerToPlay() + retry;
            if (playerCooridinator.isActive(tempNextPlayer)) {
                nextPlayer = tempNextPlayer;
                break;
            }
            retry++;
        }

        if (nextPlayer != -1) {
            playerCooridinator.setNextPlayerToPlay(nextPlayer);
        } else {
            System.out.println("Game is over, If you are seeing this message something is wrong. Please check");
        }
        playerCooridinator.notifyAll();
    }


    private void rollDice() {

        System.out.println("Roll Dice For Player " + playerName);
        int value = scanner.nextInt();
        if (value == 6) {
            rollDice();
        }
    }

    private boolean allHome() {
        System.out.println("Player " + playerName + " all home ? Enter Y for yes and N for no");
        String ans = scanner.next();
        if ("Y".equalsIgnoreCase(ans)) {
            //Set player as inactive
            playerCooridinator.setActive(playerId, false);
            return true;
        } else {
            return false;
        }
    }
}
