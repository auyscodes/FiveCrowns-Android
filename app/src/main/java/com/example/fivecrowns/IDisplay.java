package com.example.fivecrowns;

/**
 * Interface class to be implemented by activity to update displays.
 */
public interface IDisplay {

    /**
     * To update human hand
     * @param hand Collection of cards in human player's hand
     */
    void displayHumanHand(CardCollection hand);

    /**
     * To update computer hand
     * @param hand Collection of cards in computer player's hand
     */
    void displayComputerHand(CardCollection hand);

    /**
     * To update discard pile
     * @param hand Collection of cards in discard pile
     */
    void displayDiscardPile(CardCollection hand);

    /**
     * To update draw pile
     * @param hand Collection of cards in draw pile
     */
    void displayDrawPile(CardCollection hand);

    /**
     * To update round
     * @param num Number representing the round
     */
    void updateRound(int num);

    /**
     * To update cumulative computer score
     * @param num Number representing computer score
     */
    void updateComputerScore(int num);

    /**
     * To update human score
     * @param num Number representing human score
     */
    void updateHumanScore(int num);
}
