package com.example.fivecrowns;


import android.util.Log;
import android.util.MutableInt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CardCollection {
    /**
     * Stores list of cards
     */
    private List<Card> collection;

    public CardCollection(CardCollection cardCollection){
        this.collection = new ArrayList<Card>(cardCollection.collection);
    }

    /**
     * Create a new card collection
     */
    CardCollection(){
        collection = new ArrayList<Card>();
    }

    /**
     * Add card to the front of the collection
     * @param card Card to be added
     */
    public void addFront(Card card){
        collection.add(0, card);
    }

    /**
     * Add card to the back of the collection
     * @param card Card to be added
     */
    public void addBack(Card card){
        collection.add(card);
    }

    /**
     * Get card from the front of the collection
     * @return
     */
    public Card getFront(){
        return collection.get(0);
    }


    /**
     * Pop card from the front of the collection
     * @return the popped card
     */
    public Card popFront(){
        Card elem = collection.get(0);
        collection.remove(0);
        return elem;
    }

    /**
     * Get size of the collection
     * @return Numeric size
     */
    public int getSize(){
        return collection.size();
    }

    /**
     * Get card at index
     * @param position index of the card in collection
     * @return the card at index
     */
    public Card getCardAt(int position){
        return collection.get(position);
    }

    /**
     * Pop card at index
     * @param position index of the card to be popped
     * @return the popped card
     */
    public Card popCardAt(int position){
        Card card = collection.get(position);
        collection.remove(position);
        return card;
    }

    /**
     * Add card at index
     * @param position Position of the card
     * @param card card to be added
     */
    public void addCardAt(int position, Card card){
        collection.add(position, card);
    }

    /**
     * Remove cards from the collection
     * @param card card to be removed
     * @return True if the removal was successful
     */
    public boolean remove(Card card){
        for (int i=0;i<collection.size(); i++){
            if (collection.get(i) == card){
                collection.remove(card);
                return true;
            }
        }
        return false;
    }

    /**
     * Converts all the cards having the passed face to wildcards
     * @param face Transforms all the cards that has passed face
     */
    public void transformCards(String face){
        for (Card card: collection){
            if (!card.isJoker()) card.makeWildCard(face);
        }
    }

    /**
     * Undoes all transformed wildcards
     */
    public void undoCardsTmation(){
        for (Card card: collection){
            if (card.isWildCard()) card.unmakeWildCard();
        }
    }

    /**
     * Returns string representation of the class
     * @return
     */
    public String toString(){
        StringBuilder output = new StringBuilder();
        for (Card card: collection){
            output.append(card.toString()).append(" ");
        }
        return output.toString();
    }

    /**
     * Collect all the cards from passed collection
     * @param cardCollection collection from which cards are collected
     */
    public void collect(CardCollection cardCollection){
        int size = cardCollection.getSize();
        for (int i=0;i<size;i++){
            collection.add(collection.size()-1, cardCollection.popFront());
        }
    }

    /**
     * Tells whether card in the collection belogs to same suit
     * @return True if all the cards in the collection belong to same suit
     */
    boolean sameSuit(){
        Card first_card = collection.get(0);
        for (Card card: collection){
            if (!first_card.sameSuit(card)) return false;
        }
        return true;
    }


    /**
     * Separates list of cards passed to normal and special(joker and wildcards) cards
     * @param cardsCol Collection from which the list of cards are separated
     * @return Newly created list containing list of normal cards and list of special cards
     */
    private List<List<Card>> separateNormalAndSpecialCards(List<Card> cardsCol){
        List<Card> normalCards = new ArrayList<Card>();
        List<Card> specialCards = new ArrayList<Card>();

        for (int i=0;i<cardsCol.size();i++){
            Card card = cardsCol.get(i); // Note: returns a copy of reference
            if (card.isSpecialCard()){
                specialCards.add(card);
                continue;
            }
            normalCards.add(card);
        }
        return Arrays.asList(normalCards, specialCards);
    }

    /**
     * Divides the passed list of cards. e.g [x,y,z] => [ [x] [x,y] [x,y,z] [y,z] [z] ]
     * @param cardsCol List of cards that is divided
     * @return List containing the divided list of cards
     */
    private List<List<Card>> divideCards(List<Card> cardsCol){
        Collections.sort(cardsCol);
        List<List<Card>> output = new ArrayList<List<Card>>();
        if (cardsCol.isEmpty()) return output;

        for (int i=0;i<cardsCol.size(); i++){
            for (int j=0;j<cardsCol.size();j++){
                List<Card> result = new ArrayList<Card>();
                for (int k=i;k<=j;k++){
                    result.add(cardsCol.get(k));
                }
                output.add(result);
            }
        }
        return output;
    }

    /**
     * Removes card present in pass hand if it is present in curr
     * @param hand collection from which cards need to be removed
     * @param curr collection from which cards in hand that need to be removed is determined
     * @return list of cards that were removed
     */
    private List<Card> removeCards (List<Card> hand, List<Card>curr){
        List<Card> output = new ArrayList<Card>();
        int handCount = hand.size() - 1;
        while(!hand.isEmpty() && handCount >=0 ){
            int currCount = curr.size() - 1;
            while(!curr.isEmpty() && currCount >= 0){
                if (hand.get(handCount).equals(curr.get(currCount))){
                    Card removedCard = hand.remove(handCount);
                    curr.remove(currCount);
                    output.add(removedCard);
                    break;
                }
                currCount--;
            }

            handCount--;
        }
        return output;
    }

    /**
     * Adds the cards removed above
     * @param hand collection in which cards are added
     * @param removed collection in which cards that are added to hand are present
     */
    private void addCards(List<Card>hand, List<Card>removed){
        hand.addAll(removed);
    }

    /**
     * Generates best possible arrangement of cards from passed hand
     * Note: this function should be made static
     * @param hand hand that is evaluated
     * @param minBranch best possible arrangement of cards in hand to get minimum score
     * @param minScore minimum score that we can get by arranging the cards
     */
    public  void genBestHelper(CardCollection hand, List<List<Card>>minBranch, MutableInt minScore){
        List<List<Card>> branch = new ArrayList<List<Card>>();
        genBestHelper(hand.collection, branch ,minBranch, minScore);
    }

    /**
     * Tells whether going out is possible or not
     * @param minBranch best possible arrangement of cards to get minimum score
     * @param minScore minimum score that we can get by arranging the cards
     * @return True if going out is possible ie. minScore is 0
     */
    public boolean isGoingOutPossible(  List<List<Card>> minBranch, MutableInt minScore){
        Log.d("Min Branch", "isGoingOutPossible called");
        List<List<Card>> branch = new ArrayList<List<Card>>();
        genBestHelper(this.collection, branch, minBranch, minScore);
        return minScore.value == 0;
    }

    /**
     * Generates best possible arrangement of cards from passed hand
     * @param hand hand that is evaluated
     * @param branch Used by this function to keep track of branch in recurson
     * @param minBranch best possible arrangement of cards in hand to get minimum score
     * @param minScore minimum score that we can get by arranging the cards
     */
    private void genBestHelper(List<Card>hand, List<List<Card>> branch, List<List<Card>>minBranch, MutableInt minScore){
        Log.d("Min Branch", "genBestHelperCalled");
        List<List<Card>> listOfBooksAndRuns = genBooksAndRuns(hand);
        Log.d("Min Branch", "List of Books And Runs size: " + Integer.toString(listOfBooksAndRuns.size()));
        if (listOfBooksAndRuns.isEmpty()){
            int score = countScore(hand);
            if (score <= minScore.value){
                minScore.value = score;
                // !!!!!!!!!!!!!!!!!! This is important!!!!!!!!!!!!!!
                minBranch.clear();

                minBranch.addAll(branch);
                if (!hand.isEmpty()){
                    // minBranch.add(hand); !!!!!!!!!!!!!!!!!!!!!!!!!!! Careful
                    minBranch.add(new ArrayList<>(hand));
                }
            }
        } else{
            for (int i =0; i<listOfBooksAndRuns.size(); i++){
                List<Card> curr = listOfBooksAndRuns.get(i);
                List<Card> removedCards = removeCards(hand, curr);
                branch.add(removedCards);
                genBestHelper(hand, branch, minBranch, minScore);
                branch.remove(removedCards);
                addCards(hand, removedCards);
            }
        }
    }

    /**
     * Counts the score of the passed collection of cards
     * @param cardsCol The passed collection of cards
     * @return Total score of collection
     */
    private int countScore(List<Card> cardsCol){
        int sum = 0;
        for (Card card: cardsCol){
            sum+= card.getValue();
        }
        return sum;
    }

    /**
     * Generates a list that contains list of books and list of runs
     * @param cardsCol Collection of cards
     * @return List containing list of books and list of runs
     */
    private List<List<Card>> genBooksAndRuns(List<Card> cardsCol){
        List<List<Card>> output = new ArrayList<List<Card>>();
        if (cardsCol.size() < 3) return output;
        List<List<Card>> books = genBooks(cardsCol);
        List<List<Card>> runs = genRuns(cardsCol);

        output.addAll(books);
        output.addAll(runs);
        return output;
    }

    /**
     * Generates a list of runs
     * @param cardsCol cards collection containing a list of runs
     * @return list of runs
     */
    private List<List<Card>> genRuns(List<Card> cardsCol){
        List<List<Card>> separateCards = separateNormalAndSpecialCards(cardsCol);
        List<Card> normalCards = separateCards.get(0);
        List<Card> specialCards = separateCards.get(1);

        List<List<Card>> divideSuits = separateSuits(normalCards);


        List<List<Card>> output = new ArrayList<List<Card>>();

        for (List<Card> suitOfCards: divideSuits){
            List<List<Card>> dividedSuitOfCards = divideCards(suitOfCards);
            List<List<Card>> dividedSuitWithSpCards = addSpecialCards(dividedSuitOfCards, specialCards);
            for (List<Card> elem: dividedSuitWithSpCards){
                if (checkRun(elem)){
                    output.add(elem);
                }
            }
        }
        return output;

    }

    /**
     * Generates list of books
     * @param cardsCol cards collection containing list of books
     * @return list of books
     */
    private List<List<Card>> genBooks(List<Card> cardsCol){
        List<List<Card>> separateCards = separateNormalAndSpecialCards(cardsCol);
        List<Card> normalCards = separateCards.get(0);
        List<Card> specialCards = separateCards.get(1);
        List<List<Card>> dividedCards = divideCards(normalCards);
        List<List<Card>> dividedCardsWSpCards = addSpecialCards(dividedCards, specialCards);

        List<List<Card>> output = new ArrayList<List<Card>>();
        for (List<Card> cards: dividedCardsWSpCards){
            if (checkBook(cards)) output.add(cards);
        }
        return output;
    }

    /**
     * Adds special cards ([a,b]) to the passed list containing list of cards([ [x, y] [z]] ] ) eg. ([ [x, y] [z]] ] , [a,b] ) => [[x,y,a] [x,y,a,b] [z,a] [z,a,b]]
     * @param cardsCol list containing list of cards
     * @param specialCards list containing special cards
     * @return List of list of cards with special cards as shown in the example above
     */
    private List<List<Card>> addSpecialCards(List<List<Card>> cardsCol, List<Card> specialCards){
        List<List<Card>> spCardsList = new ArrayList<List<Card>>();
        for (int i=0;i<specialCards.size();i++){
            List<Card> spCards = new ArrayList<Card>();
            for (int j=0;j<=i;j++){
                spCards.add(specialCards.get(j));
            }
            spCardsList.add(spCards);
        }
        List<List<Card>> output = new ArrayList<List<Card>>();
        for (int i=0;i<cardsCol.size();i++){
            for (int j=0;j<spCardsList.size();j++){
                List<Card> elem = new ArrayList<>(cardsCol.get(i));
                List<Card> spCards = spCardsList.get(j);
                elem.addAll(spCards);
                if (elem.size() >= 3){
                    output.add(elem);
                }
            }
            if (cardsCol.get(i).size()>=3) output.add(cardsCol.get(i));
        }
        return output;
    }

    /**
     * Check if a collection cards is a book. Doesn't check multiple books. e.g [3S, 3H, 3T] returns true [3S, 3H, 3T, 6S, 6H, 6T] returns false
     * @param hand Collection of cards passed that is checked
     * @return True if the passed collection is a book
     */
    private boolean checkBook(List<Card> hand){
        if (hand.size() <3 ) return false;
        List<List<Card>> normalAndSpecialCards = separateNormalAndSpecialCards(hand);
        List<Card> normalCards = normalAndSpecialCards.get(0);

        // Do not need special cards
        if (normalCards.isEmpty()) return true;
        int size = normalCards.size();
        int count = 1;
        Card prevCard = normalCards.get(0);
        Card currCard;

        while(count< size){
            currCard = normalCards.get(count);
            if (prevCard.getValue() != currCard.getValue()) return false;
            prevCard = currCard;
            count++;
        }
        return true;
    }

    /**
     * Check the cards in passed collection belong to same suit. e.g. [3S, 9S, JS] returns true
     * @param cards Collection of cards passed
     * @return True if cards in the passed collection belong to same suit
     */
    private boolean sameSuit(List<Card> cards ){
        if (cards.isEmpty()) return true;
        Card first_card = cards.get(0);
        for (Card card: cards){
            if (!first_card.sameSuit(card)) return false;
        }
        return true;
    }

    /**
     * Check if a collection cards is a run. Doesn't check multiple runs. E.g [3S, 4S, 5S, 6S] returns true , [3S, 4S, 5S, 7S, 8S, 9S] returns false
     * @param hand Collection of cards passed that is checked
     * @return True if the passed collection is a run
     */
    private boolean checkRun(List<Card> hand){

        if (hand.size() < 3) return false;

        List<List<Card>> normalAndSpecialCardsOfHands = separateNormalAndSpecialCards(hand);

        List<Card> normalCards = normalAndSpecialCardsOfHands.get(0);
        List<Card> specialCards = normalAndSpecialCardsOfHands.get(1);

        if (normalCards.isEmpty()) return true;

        List<Card> checkedSpecialCards = new ArrayList<Card>();

        Collections.sort(normalCards);

        if (sameSuit(normalCards)){
            int size = normalCards.size();
            Card prevCard = normalCards.get(0);
            Card currCard;
            int count = 1;

            while(count<size){
                currCard = normalCards.get(count);
                if (prevCard.getValue() != currCard.getValue() - 1){
                    if (specialCards.isEmpty()) return false;
                    if (prevCard.getValue() != currCard.getValue() - 2){
                        return false;
                    }
                    else{
                        checkedSpecialCards.add(0, specialCards.remove(0));
                    }
                }
                prevCard = currCard;
                count++;
            }
            return true;
        }
        return false;
    }

    /**
     * Separate collection of cards on the basis of suit e.g [3S, 7S, 7D, KH, QH, JH] = [ [KH, QH, JH] [7D] [] [3S, 7S] []]
     * @param cardsCol Collection that is separated
     * @return List containing separated list of cards
     */
    List<List<Card>> separateSuits(List<Card> cardsCol){
        List<Card> hearts = new ArrayList<Card>();
        List<Card> diamonds= new ArrayList<Card>();
        List<Card> clubs= new ArrayList<Card>();
        List<Card> tridents= new ArrayList<Card>();
        List<Card> spades= new ArrayList<Card>();
        List<List<Card>> output = new ArrayList<List<Card>>();
        for (Card card: cardsCol)
        {
            if (card.checkSuit("H")) hearts.add(card);
            if (card.checkSuit("T")) tridents.add(card);
            if (card.checkSuit("C")) clubs.add(card);
            if (card.checkSuit("S")) spades.add(card);
            if (card.checkSuit("D")) diamonds.add(card);

        }
        output.add(hearts);
        output.add(diamonds);
        output.add(clubs);
        output.add(spades);
        output.add(tridents);

        return output;
    }
}
