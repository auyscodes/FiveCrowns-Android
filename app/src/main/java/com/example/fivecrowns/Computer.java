package com.example.fivecrowns;

import android.util.Log;
import android.util.MutableInt;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents computer player in the game
 */
public class Computer extends Player {

    /**
     * Constructor to create computer player
     * @param name Name of the computer player
     * @param score Score of the computer player
     * @param hand Hand of the computer player
     */
    Computer(String name, int score, CardCollection hand) {
        super(name, score, hand);

    }

    /**
     * Logs string passed to file
     * @param log String to be logged
     */
    private static void logger(String log){
        RepoService.getInstance().logToFile(log);
    }

    /**
     * Uses its strategy to play the game
     * @param cardsArrgnmnt Best possible arrangement of cards in player's hand
     * @param score Score received by the player in the arrangement
     * @return True if the computer wants to go out
     */
    @Override
    public  boolean playGame(List<List<Card>> cardsArrgnmnt, MutableInt score) {
        RepoService.getInstance().logDrawAndDiscardPile();
        logger("----------Computer Playing---------\n");
        logger("I am a " + this.getName() + "\n");
        RepoService.getInstance().logDrawAndDiscardPile(); // change later
        logger(this.getHand().toString());
        CardCollection currHand = this.getHand();
        return play(currHand, cardsArrgnmnt, score, "The computer chooses to");

    }

    /**
     * Applies strategy to play the game
     * @param currHand Computer player's hand
     * @param cardsArrgnmnt Best possible arrangement of the hand
     * @param score Score of the cardsArrgnmnt
     * @param message Message format in log file
     * @return True if the computer player wants to go out
     */
    private boolean play(CardCollection currHand, List<List<Card>> cardsArrgnmnt, MutableInt score, String message){
        MutableInt currScore = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> currArrangement = new ArrayList<List<Card>>();
        currHand.genBestHelper(new CardCollection(currHand), currArrangement, currScore);
        cardsArrgnmnt.addAll(currArrangement) ;
        score.value = currScore.value;
        MutableInt newScore = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> newArrgnmnt = new ArrayList<List<Card>>();

        int cardIndex = getIndxToRmvCardAtHlpr(RepoService.getInstance().getDisCardFront(), newScore, newArrgnmnt, currScore.value, currHand);
        cardsArrgnmnt.clear();
        cardsArrgnmnt.addAll(newArrgnmnt);
        score.value = newScore.value;
        if (cardIndex != -1){
            currHand.addCardAt(cardIndex, RepoService.getInstance().removeDisCardFront());
            RepoService.getInstance().addToDiscard(currHand.popCardAt(cardIndex + 1));
            logger(message + " pick from the discard pile because replacing a card with " + currHand.getCardAt(cardIndex).toString() + " card " + " helps make " + newArrgnmnt + "\n");
            logger(message + " discard " + RepoService.getInstance().getDisCardFront().toString() + " card because replacing it with " + currHand.getCardAt(cardIndex).toString() + " card reduces the minimum score from "
                    + currScore.value + " to " + newScore.value + "\n");
            return newScore.value == 0;
        } else{
            Log.d("Computer Play", "currHand: " + currHand.toString());
            logger(message + " pick from drawpile because the card present in discard pile does not reduce the minimum score \n" );
            Card drawCard = RepoService.getInstance().removeDrawFront();
            newArrgnmnt.clear();
            newScore.value = Integer.MAX_VALUE;
            int index = getIndxToRmvCardAtHlpr(drawCard, newScore, newArrgnmnt, currScore.value, currHand);
            cardsArrgnmnt.clear();
            cardsArrgnmnt.addAll(newArrgnmnt);
            score.value = newScore.value;

            if (index == -1){
                RepoService.getInstance().addToDiscard(drawCard);
                logger(message + " throw the card picked up from drawpile because the card does not reduce the minimum score \n");


                return false;
            }
            else{
                //
                currHand.addCardAt(index, drawCard);
                //
                RepoService.getInstance().addToDiscard(currHand.popCardAt(index + 1));
                //
                logger( message + " replace a card in hand with " + currHand.getCardAt(index).toString() + " card " + " helps make " + newArrgnmnt);
                logger("\n");
                //
                logger(message + "  discard " + RepoService.getInstance().getDisCardFront().toString() + " card because replacing it with " + currHand.getCardAt(index).toString() + " card reduces the minimum score from "
                        + currScore.value + " to " + newScore.value + "\n");
                return newScore.value == 0;
            }

        }
    }

    /**
     * Tells if picking from discard pile is a good idea
     * @param hand Hand before picking the card
     * @param hand_score Hand score before picking the card
     * @param index Index of card in hand if replacing card in hand results in lower score
     * @param arrgnmnt Best possible arrangement of the hand
     * @param score  Score of the arrgnmnt
     * @return True if picking from discard pile results in lower score
     */
    private static boolean pickCardFromDiscardPile(CardCollection hand, int hand_score, MutableInt index, List<List<Card>> arrgnmnt, MutableInt score){
        index.value = -1;
        MutableInt newScore = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> newArrgnmnt = new ArrayList<List<Card>>();
        int cardIndex = getIndxToRmvCardAtHlpr(RepoService.getInstance().getDisCardFront(), newScore, newArrgnmnt, hand_score, hand);
        if (cardIndex>=0){
            index.value = cardIndex;
            arrgnmnt.addAll(newArrgnmnt) ;
            score.value = newScore.value;
            return true;
        }
        return false;

    }

    /**
     * Help human player in picking the card
     * @param hand Collection of cards in the human player's hand
     */
    public static void helpPick(CardCollection hand){
        MutableInt currScore = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> currArrangement = new ArrayList<List<Card>>();

        hand.genBestHelper(new CardCollection(hand), currArrangement, currScore);

        MutableInt index = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> arrgnmnt = new ArrayList<List<Card>>();
        MutableInt score = new MutableInt(Integer.MAX_VALUE);
        boolean keepDisCard = pickCardFromDiscardPile(hand, currScore.value, index, arrgnmnt, score);
        if (keepDisCard){
            logger("I recommend you to pick card from discard pile\n");
            logger("I recommend you to pick up from the discard pile because replacing a card with " + RepoService.getInstance().getDisCardFront().toString() + " card helps make " + arrgnmnt.toString() + "\n");
            logger("Replacing " + hand.getCardAt(index.value).toString() + " with " + RepoService.getInstance().getDisCardFront().toString() + " card reduced the minimum score from " + currScore.value + " to " + score.value +" \n");
            return;
        }
        logger("I recommend you to pick card from drawPile \n");
        logger("Picking the card in discard pile doesn't reduce you minimum score \n");

    }

    /**
     * Help human player in throwing the picked card
     * @param hand Human player's hand before picking the card
     * @param card Card picked by human player
     */
    public static void helpThrow(CardCollection hand, Card card){
        Log.d("Debug ", "Inside helpThrow");
        MutableInt currScore = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> currArrgnmnt = new ArrayList<List<Card>>();
        hand.genBestHelper(new CardCollection(hand), currArrgnmnt, currScore);

        if (currScore.value == 0){
            logger("I recommend you to throw " + card.toString() + " card because all the cards in your hand can be arranged to " + currArrgnmnt.toString() + " to get minimum score of " + currScore.value);
            return;
        }
        MutableInt new_Score = new MutableInt(Integer.MAX_VALUE);
        List<List<Card>> newArrgnmnt = new ArrayList<List<Card>>();
        int index = getIndxToRmvCardAtHlpr(card, new_Score, newArrgnmnt, currScore.value, hand);
        // will already have returned above if index was -1
        if (index==-1){
            logger("I recommend you to throw card " + card.toString() + " because it increases the minimum score \n");
            return;
        }
        logger("I recommend you to throw " + hand.getCardAt(index).toString() + " card because replacing that card with " + card.toString() + " card reduces minimum score from " + currScore.value + " to " + new_Score.value + "\n");
        logger("Your new cards arrangement will be " + newArrgnmnt.toString() + "\n");
        logger("Your old arrangement was " + currArrgnmnt.toString() + "\n");

    }

    /**
     * Get index at which card needs to removed to lower score
     * @param card Picked card
     * @param newScore Best possible score of newArrgnmnt
     * @param newArrgnmnt Best possible arrangement of cards for the returned index
     * @param currScore Best score of currHand
     * @param currHand Hand before picking the card
     * @return Index of card which to be removed from currHand, -1 if currHand has lower score
     */
    private static int getIndxToRmvCardAtHlpr(Card card, MutableInt newScore, List<List<Card>> newArrgnmnt, int currScore, CardCollection currHand){
        newScore.value = currScore; // 21
        int cardIndex = -1;
        for (int i=0;i<currHand.getSize();i++){
            List<List<Card>> arrgnmnt = new ArrayList<List<Card>>();
            MutableInt score = new MutableInt(Integer.MAX_VALUE);
            Card handCard = currHand.popCardAt(i);
            Log.d("Computer Play", "Arrangement before generation: " + arrgnmnt.toString());
            currHand.addCardAt(i, card);
            currHand.genBestHelper(new CardCollection(currHand), arrgnmnt, score);
            Log.d("Computer Play", "hand After replacing card: " + currHand.toString() + " " + handCard.toString() + " replaced with: " + card.toString());
            Log.d("Computer Play", Integer.toString(score.value) + " " + Integer.toString(newScore.value));
            Log.d("Computer Play", arrgnmnt.toString());
            if (score.value < newScore.value){
                newScore.value = score.value;
                newArrgnmnt.clear();
                newArrgnmnt.addAll(arrgnmnt);
                cardIndex = i;
            }
            currHand.popCardAt(i);
            currHand.addCardAt(i, handCard);
        }
        return cardIndex;
    }
}
