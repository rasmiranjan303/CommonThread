package com.thrajon;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerCooridinator {

    //private int currentplayertoplay allows the players to play in order
    volatile private int currentPlayerToPlay;

    //Variable to hold total number of players
    volatile private int totalNoOfPlayers;

    //Map to hold if a player has finished all PONs or not
    //True if he has finished or left and false if he has not
    private ConcurrentHashMap
            <Integer, Boolean> playerIdVsStatus = new ConcurrentHashMap<>();

    public PlayerCooridinator(int currentPlayerToPlay, int noOfPlayers) {

        this.currentPlayerToPlay = currentPlayerToPlay;
        this.totalNoOfPlayers = noOfPlayers;
    }

    public boolean isGameOver() {
        //Logic to check Game Over or not
        //If number of players-1 = players who reached home
        //then game is over
        return playerIdVsStatus.values().stream().filter(v -> !v).count() == totalNoOfPlayers -1 ;
    }

    public Boolean isActive(int playerId) {
        return playerIdVsStatus.computeIfAbsent(playerId, k -> true);
    }

    public int getCurrentPlayerToPlay() {
        return currentPlayerToPlay;
    }

    public void setActive(int playerId, boolean isActive) {
        playerIdVsStatus.put(playerId, isActive);
    }

    public void setNextPlayerToPlay(int playerId) {

        this.currentPlayerToPlay = playerId;
    }

    public int getTotalNumberOfPlayers() {
        return totalNoOfPlayers;
    }

    public void setTotalNoOfPlayers(int totalNoOfPlayers) {
        this.totalNoOfPlayers = totalNoOfPlayers;
    }
}
