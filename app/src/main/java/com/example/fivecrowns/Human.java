package com.example.fivecrowns;

import android.util.MutableInt;

import java.util.List;

/**
 * Represents human player of the game
 */
public class Human extends Player {
    /**
     * Create the human player
     * @param name Name of the human player
     * @param score Score received by the human player
     * @param hand Hand of the human player
     */
    Human(String name, int score, CardCollection hand) {
        super(name, score, hand);
    }

    /**
     * Play the Game
     * @param cardsArrgnmnt Best possible arrangement of cards in player's hand
     * @param score Score received by the player in the arrangement
     * @return True if the human player goes out.
     */
    @Override
    public boolean playGame(List<List<Card>> cardsArrgnmnt, MutableInt score) {
        return getHand().isGoingOutPossible(cardsArrgnmnt, score);
    }
}
