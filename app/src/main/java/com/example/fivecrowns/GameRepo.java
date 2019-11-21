package com.example.fivecrowns;

import java.util.ArrayList;
import java.util.List;

public class GameRepo {
    /**
     * List of players
     */
    private List<Player> players;
    /**
     * Next Player to play the game
     */
    private int nextPlayerIndex;
    /**
     * Ongoing Round
     */
    private int round;
    /**
     * Index of human player on the list
     */
    private int humanPlayerIndex;
    /**
     * Index of computer player on the list
     */
    private int computerPlayerIndex;
    /**
     * Collection of cards in the draw pile
     */
    private CardCollection drawPile;
    /**
     * Collection of cards in the discard pile
     */
    private CardCollection discardPile;

    /**
     * Create a new game repo
     * @param players List of players playing the game
     * @param nextPlayerIndex Next player to play the game
     * @param round Number representing the round
     * @param humanPlayerIndex Index of human player on the list
     * @param computerPlayerIndex Index of computer player on the list
     * @param drawPile Collection of cards in the draw pile
     * @param discardPile Collection of cards in the discard pile.
     */
    GameRepo(List<Player> players, int nextPlayerIndex, int round, int humanPlayerIndex, int computerPlayerIndex, CardCollection drawPile, CardCollection discardPile){
        this.players = players;
        this.nextPlayerIndex = nextPlayerIndex;
        this.round = round;
        this.humanPlayerIndex = humanPlayerIndex;
        this.computerPlayerIndex = computerPlayerIndex;
        this.discardPile = discardPile;
        this.drawPile = drawPile;
    }

    /**
     * Get the draw pile
     * @return draw pile collection
     */
    CardCollection getDrawPile(){
        return this.drawPile;
    }

    /**
     * Get the discard pile
     * @return discard pile collection
     */
    CardCollection getDiscardPile(){
        return this.discardPile;
    }

    /**
     * Get the list of players
     * @return list of player playing the game
     */
    List<Player> getPlayers(){
        return this.players;
    }

    /**
     * Getter method for next player index
     * @return Index of the next player on the players list
     */
    int getNextPlayerIndex(){
        return this.nextPlayerIndex;
    }

    /**
     * Round number of ongoing round
     * @return Number representing round
     */
    int getRound(){
        return this.round;
    }

    /**
     * Getter method for human player index
     * @return index of human player on the player's list
     */
    int getHumanPlayerIndex(){
        return this.humanPlayerIndex;
    }

    /**
     * Getter method for computer player index
     * @return index of computer player on the player's list
     */
    int getComputerPlayerIndex(){
        return this.computerPlayerIndex;
    }

    /**
     * Setter for round
     * @param round Number representing ongoing round
     */
    void setRound(int round){
        this.round = round;
    }

    /**
     * Get string representation of next player
     * @return String representing the next player
     */
    String getNextPlayer(){

        if (nextPlayerIndex==humanPlayerIndex) return "Human";
        if (nextPlayerIndex == computerPlayerIndex) return "Computer";

        return "";
    }

    /**
     * Setter method for setting next player index
     * @param index index of the next player
     */
    void setNextPlayerIndex(int index){
        this.nextPlayerIndex = index;
    }


}
