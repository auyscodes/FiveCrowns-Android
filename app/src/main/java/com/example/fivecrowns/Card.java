package com.example.fivecrowns;

/**
 * Represents a card class.
 * Card can be a normal or joker card.
 */
public class Card implements Comparable<Card> {
    private boolean isWildCard = false;
    private boolean isJoker = false;
    private int jokerNumber = 0;

    private String face;
    private String suit;

    /**
     * Creates a new card with face and suit
     * @param face face of the card, e.g. {3,4,5,6,7,8,9,X,J,Q,K}
     * @param suit suit of the card, e.g. {D,H,T,S,C} (diamond, heart, trident,
     *             spade, club)
     */
    Card(String face, String suit){
        this.face = face;
        this.suit = suit;
    }

    /**
     * Creates a new joker card
     * @param jokerNumber number to identify joker, eg. {1,2,3}
     */
    Card(int jokerNumber){
        this.isJoker = true;
        this.jokerNumber = jokerNumber;
    }


    /**
     * Checks if passed card is equal  to another card
     * Checking done by comparing face and suit or joker Number
     * @param card Card that is passed
     * @return True if the passed card is equal otherwise false
     */
    boolean equals(Card card){
        if (card.isJoker) {
            return card.jokerNumber == this.jokerNumber;
        }
        return card.face.equals(this.face) && card.suit.equals(this.suit);
    }

    /**
     * To check if card is joker
     * @return True if the card is joker
     */
    boolean isJoker(){
        return this.isJoker;
    }

    /**
     * To check if card is a wildcard
     * @return True if the card is a wildcard
     */
    boolean isWildCard(){
        return this.isWildCard;
    }

    /**
     * To check if a card is a special card i.e a joker or wildcard
     * @return True if the card is a special card
     */
    boolean isSpecialCard(){
        return isJoker || isWildCard;
    }

    /**
     * Make card wildcard if the passed face equals to the face of the card
     * Note: Need to recheck why I needed to pass face
     * @param face string representing face of the card
     * @return True if the making wildcard was successful
     */
    boolean makeWildCard(String face){
        if (this.face.equals(face)){
            this.isWildCard = true;
            return true;
        }
        return false;
    }

    /**
     * Make any card normal card
     */
    void unmakeWildCard(){
        this.isWildCard = false;
    }

    /**
     * Get the string representation of the card. E.g 5D for 5 of diamond
     * @return String representation of the card
     */
    public String toString(){
        if (isJoker) return "J" + Integer.toString(jokerNumber);
        return this.face + this.suit;
    }


    /**
     * Gets the string representation of card in suit and face order e.g S3 for 3 of spades
     * @return string representation of card. First char of the string is letter
     */
    String small_suit_face(){
        if (isJoker) return "J" + Integer.toString(jokerNumber);
        return this.suit + this.face;
    }

    /**
     * Checks if the passed card has the same suit as this card
     * @param card object of type card, which represents card object
     * @return True if the passed card has same suit as this card
     */
    boolean sameSuit(Card card){
        return this.suit.equals(card.suit);
    }

    /**
     * Compares two cards by value.
     * Value is compared with the help of getValue function present in the class.
     * @param card object of type card
     * @return Returns 1 if the passed card is less than this card, 0 if the cards are equal
     *              and -1 if the passed card is greater than this card.
     */
    @Override
    public int compareTo(Card card) {
        return this.getValue() > card.getValue() ? 1: this.getValue()< card.getValue() ? -1: 0;
    }

    /**
     * Returns the integer value of cards. e.g 20 for wildcard
     * Note: This function is project dependent and the values are hardcoded here
     * Better if value is passed to the card from outside.
     * @return Integer representing the value of the card
     */
    int getValue(){
        if (isJoker) return 50;
        if (isWildCard) return 20;
        if (face.equals("K")) return 13;
        if (face.equals("Q")) return 12;
        if (face.equals("J")) return 11;
        if (face.equals("X")) return 10;
        if (face.equals("9")) return 9;
        if (face.equals("8")) return 8;
        if (face.equals("7")) return 7;
        if (face.equals("6")) return 6;
        if (face.equals("5")) return 5;
        if (face.equals("4")) return 4;
        if (face.equals("3")) return 3;
        return 0; // ide was complaining about not having return statement at end
    }

    /**
     * Checks if passed string equals to the suit of the card
     * @param suit String representing suit,
     * @return True if the passed suit is equal to the suit of the card
     */
    boolean checkSuit(String suit){
        return this.suit.equals(suit);
    }

}
