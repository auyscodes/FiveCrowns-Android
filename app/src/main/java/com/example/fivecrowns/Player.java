package com.example.fivecrowns;

import android.util.MutableInt;

import java.util.List;

public abstract class Player {
    /**
     * Represents collection of cards in player's hand
     */
    private CardCollection hand;
    /**
     * Name of the player
     */
    private String name;
    /**
     * Score received by the player
     */
    private int score = 0;

    /**
     * Create an instance of player class
     * @param name Name of the player
     * @param score Cumulative score of the player
     * @param hand Hand of the player, contains cards
     */
    Player(String name, int score, CardCollection hand){
        this.name = name;
        this.score = score;
        this.hand = hand;
    }

    /**
     * Called when player palys the game. Tells if player can go out.
     * @param cardsArrgnmnt Best possible arrangement of cards in player's hand
     * @param score Score received by the player in the arrangement
     * @return True if player wants to go out
     */
    public abstract boolean playGame(List<List<Card>> cardsArrgnmnt, MutableInt score);

    /**
     * Adds card to the player's hand
     * @param card
     */
    public void addCard(Card card){
        hand.addFront(card);
    }

    /**
     * Remove card from the player's hand
     * @param card Card to be removed
     */
    public void removeCard(Card card){

         hand.remove(card);
    }

    /**
     * Returns and removes card from the players hand
     * @param card Card to be removed
     * @return the removed card
     */
    public Card popCard(Card card){
        hand.remove(card);
        return card;
    }

    /**
     * Get the name of the player
     * @return Name of the player
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get the score received by player
     * @return Cumulative score of the player
     */
    public int getScore(){
        return this.score;
    }


    /**
     * Hand of the player
     * @return Collection of cards in player's hand
     */
    public CardCollection getHand(){
        return this.hand;
    }

    /**
     * Add to the cumulative score of the player
     * @param score Score received by the player when going out
     */

    public void addToScore(int score){
        this.score += score;
    }



}
