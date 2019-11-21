package com.example.fivecrowns;


import android.content.Context;
import android.util.Log;
import android.util.MutableInt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Service class that makes changes on the model
 */
public class RepoService {

    /**
     * Stores the number of players gone out in the current round.
     */
    private int playersGoneOut = 0;

    /**
     * Repository that contains model of the game.
     */
    private GameRepo gameRepo;

    /**
     * Stores singleton instance of the class.
     */
    private static RepoService instance;

    /**
     * Stores file object to log
     */
    private static  File file;

    /**
     * Stores the card lastPicked by human player
     */
    private Card lastPicked;

    /**
     * True when human player has picked a card from draw or discard pile
     */
    private boolean picked = false;

    /**
     * Creates the instance of RepoService class
     */
    private RepoService(){
//        lastPicked = null;
//        picked = false;
//        file = null;
    };

    /**
     * Get the singleton instance of the repository
     * @return Singleton object of the class
     */
    public static synchronized RepoService getInstance(){
        // this is called when creating a new Game
        if (instance==null) {
            instance = new RepoService();
        }
        return instance;
    }

    /**
     * Clears the state of the class
     */
    private void clearAll(){
        lastPicked = null;
        picked = false;
        file = null;
        playersGoneOut = 0;
    }

    /**
     * Helps human player to throw the card
     */
    public void throwHelp(){
        Log.d("Debug", "throwHelp called");
        CardCollection handAfterPicking = gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getHand();
        CardCollection handBeforePicking = new CardCollection(handAfterPicking);
        handBeforePicking.remove(lastPicked);
        Computer.helpThrow(handBeforePicking, lastPicked);
    }

    /**
     *  Helps human player to pick the card
     */
    public void pickHelp(){
        Computer.helpPick(gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getHand());
    }

    /**
     * To delete log file
     */
    void deleteLog(){
        deleteTempFiles(MyApplication.getAppContext().getCacheDir());
    }

    /**
     * To delete the temp files
     * @param file object representing file
     * @return True if the deleting file was successful
     */
    private boolean deleteTempFiles(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteTempFiles(f);
                    } else {
                        f.delete();
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * String that is appended to the log file
     * @param log String that is logged to the file
     */
    public  void logToFile( String log){
        File directory = MyApplication.getAppContext().getCacheDir();

        if (file == null){
            file = new File(directory, "log.txt");
        }
        try{
            FileOutputStream stream = new FileOutputStream(file, true);
            stream.write(log.getBytes());
            stream.close();
            Log.d("File Log", "Called");
            Log.d("File Log", "Log Written: " + log);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Gets the content of the log file
     * @return String with content of the file
     */
    public String getLogFile(){
        if (file==null) return "Empty file";
        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            String contents = new String(bytes);
            return contents;
            // doAfterDataLoad(contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Nothing logged\n";
    }

    /**
     * Updates the display after user has picked or thrown the card.
     * If it's computer's turn to play, then computer plays' first before updating the display
     * @param display Activity class implementing display interface
     * @return True if the game has ended
     */
    public boolean updateDisplay(IDisplay display){
        computerPlayIfTurn();
        if (gameRepo.getRound()==12) return true;
        if (display!=null){
            display.displayHumanHand(getHumanPlayerHand());
            display.displayComputerHand(getComputerPlayerHand());
            display.displayDrawPile(getDrawPile());
            display.displayDiscardPile(getDiscardPile());
            display.updateComputerScore(gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getScore());
            display.updateHumanScore(gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getScore());
            display.updateRound(gameRepo.getRound());
        }
        return false;
    }

    /**
     * Tells human player has picked the card
     * @return True if the card was picked
     */
    boolean isPicked(){
        return picked;
    }

    /**
     * Picks the card from draw pile and puts into computer player hand and updates repo state while doing so.
     */
    public void pickFromDraw(){

        Card pickedCard = gameRepo.getDrawPile().popFront();
        lastPicked = pickedCard;
        gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).addCard(pickedCard);
        picked = true;
    }

    /**
     * Picks the card discard and pile and puts into human player hand and updates repo state while doing so.
     */
    public void pickFromDiscard(){
        Card pickedCard = gameRepo.getDiscardPile().popFront();
        lastPicked = pickedCard;
        gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).addCard(pickedCard);
        picked = true;
    }

    /**
     * Get the top card in discard pile.
     * @return Card object in top of the discard pile
     */
    public Card getDisCardFront(){
        return gameRepo.getDiscardPile().getFront();
    }

    /**
     *  Get the top card in the drawpile.
     * @return Card object in top of the draw pile
     */
    public Card getDrawFront(){
        return gameRepo.getDrawPile().getFront();
    }

    /**
     * remove card from the top of the draw pile.
     * @return card object removed from top of the draw pile
     */
    public Card removeDrawFront(){
        return gameRepo.getDrawPile().popFront();
    }

    /**
     * Remove card from the top of the discard pile.
     * @return card Object representing a card
     */
    public Card removeDisCardFront(){
        return gameRepo.getDiscardPile().popFront();
    }

    /**
     *  Add card to front of the discard pile.
     * @param card Object representing a card
     */
    public void addToDiscard( Card card){
        gameRepo.getDiscardPile().addCardAt(0, card);
    }




    /**
     * To make computer play game if it's computer's turn.
     */
    private void computerPlayIfTurn(){
        if (gameRepo.getNextPlayerIndex() == gameRepo.getComputerPlayerIndex()){
            playGame(gameRepo.getNextPlayerIndex());
        }

    }

    /**
     *  Makes the player next player play the game and updates game state and tells whether round has ended or not.
     * @param nextPlayerIndex Index of the nextPlayer to play the game.
     * @return True if round has ended. Used at the time.
     */
    private boolean playGame(int nextPlayerIndex){
        Player player = gameRepo.getPlayers().get(nextPlayerIndex);
        List<List<Card>> minBranch = new ArrayList<List<Card>>();
        MutableInt minScore = new MutableInt(Integer.MAX_VALUE);
        boolean playerGoOut = player.playGame(minBranch, minScore);
        if (playerGoOut){
            logToFile("\n----Player Goes Out ---------------\n");
            logToFile(player.getName() + " went out with score of " + minScore.value + " and arrangement of " + minBranch.toString());
            logToFile("\n------------------------------------\n");
        }
        // this forces the computer to go out
        if (playersGoneOut > 0){
            // bug report
            // gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).addToScore(minScore.value);
            player.addToScore(minScore.value);
            playersGoneOut++;
        }
        // this checks if computer or human wants to go out and acts accordingly
        if (playerGoOut && playersGoneOut==0){
            // don't need to add 0 score
            playersGoneOut++;
        }
        nextPlayerIndex = (++nextPlayerIndex) % (gameRepo.getPlayers()).size();
        gameRepo.setNextPlayerIndex(nextPlayerIndex);
        return roundEndVerify();
    }

    /**
     *  Throws the card present in players hand to discard pile.
     *  Calls playGame function for nextPlayer i.e. the human Player
     * @param card
     */
    public void throwCard(Card card){
        Log.d("Computer Play:", "Throw card clicked");
        gameRepo.getDiscardPile().addFront(gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).popCard(card));
        picked = false;
        lastPicked = null;
        playGame(gameRepo.getNextPlayerIndex());// human plays the game
    }

    /**
     * Checks if round has ended and logs the human and computer score on the Log File.
     * Log File can be accessed by clicking Show Log on Android screen.
     * @return True if the round has ended.
     */
    private boolean roundEndVerify(){
        if (playersGoneOut == gameRepo.getPlayers().size() ){
            logToFile("\n-----------------Round Ended-----------------------\n");
            logToFile("\n Round: " + gameRepo.getRound() + "\n");
            logToFile("\n" + "Cumulative Human Score: " + gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getScore());
            logToFile("\n" + "Cumulative Computer Score: " + gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getScore());
            logToFile("\n-----------------------------------------------------\n");
            roundEnd();
            updateRepo(gameRepo.getRound() + 1);
            return true;
        }
        return false;
    }

    /**
     *  Logs the passed string to the android studio logcat
     * @param log
     */
    public void logger(String log){
        Log.d("Computer Play", log);
    }

    /**
     * Logs draw and discard pile on android studio logcat
     */
    public void logDrawAndDiscardPile(){
        logger("Discard Pile: "+ gameRepo.getDiscardPile().toString());
        logger("Draw Pile: " + gameRepo.getDrawPile().toString());
    }

    /**
     * To get the draw pile on ongoing round
     * @return Collection of cards in draw pile
     */
    private CardCollection getDrawPile(){
        return gameRepo.getDrawPile();
    }

    /**
     * To get the discard pile on ongoing rounds
     * @return Collection of cards in discard pile
     */
    private CardCollection getDiscardPile(){
        return gameRepo.getDiscardPile();
    }

    /**
     *  To get the human player hand on ongoing rounds
     * @return Collection of cards in the human player hand
     */
    private CardCollection getHumanPlayerHand(){
        return gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getHand();
    }

    /**
     * To get the computer player hand on ongoing rounds
     * @return Collection of cards in the computer player hand
     */
    private CardCollection getComputerPlayerHand(){
        return gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getHand();
    }

    /**
     * Function called by load game controller to load the game from file
     * @param file File where the serialized game state is saved.
     */
    public void loadGame(File file){
        Log.d("Debug ", "Load Game Called");
        byte[] bytes = new byte[(int) file.length()];
        try {
            FileInputStream stream = new FileInputStream(file);
            stream.read(bytes);
            String contents = new String(bytes);
            doAfterDataLoad(contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Instantiates a new game. Works for instantiating new game and loading a game.
     * @param nextPlayerIndex Index of the next player index passed by tossing.
     * @param is_load Tells whether we are instantiating to load a game or create a new game.
     */
    public void instantiateNewGame(int nextPlayerIndex, boolean is_load){
        deleteLog();
        clearAll(); // bug fixed by adding this
        int round = 1;
        MutableInt humanPlayerIndex = new MutableInt(-1);
        MutableInt computerPlayerIndex = new MutableInt(-1);
        List<Player> players = createPlayers(humanPlayerIndex, computerPlayerIndex);
        CardCollection drawPile = new CardCollection();
        CardCollection discardPile = new CardCollection();
        if (!is_load) fillDrawPile(drawPile);
        gameRepo = new GameRepo(players, nextPlayerIndex, round, humanPlayerIndex.value, computerPlayerIndex.value, drawPile, discardPile);
        if (!is_load) updateRepo(round);
    }

    /**
     *  Fills the passed drawPile with cards
     * @param drawPile Object of type card collection that represents draw pile
     */
    private void fillDrawPile(CardCollection drawPile){
        for (Card card: createTwoDecks()){
            drawPile.addFront(card);
        }
    }

    /**
     * Updates the repo for the round
     * Performs housekeeping tasks like setting round number, proper wildcards setting for the round, and distributes cards
     * @param round
     */
    private void updateRepo(int round){
        gameRepo.setRound(round);
        unconvertWildCards();
        convertToWildCards(round);
        distributeCards(round + 2);
    }

    /**
     *  Converts card to wildcards for the round
     * @param round Number representing the round
     */
    private void convertToWildCards(int round){
        gameRepo.getDrawPile().transformCards(convertNumCardsToDealToWildCards(round + 2));
    }

    /**
     * Makes wildcard into normal card
     */
    private void unconvertWildCards(){
        gameRepo.getDrawPile().undoCardsTmation();
    }

    /**
     *  Converts card to wild card
     * @param numCardsToDeal Number of cards dealt in each round is the wildcard for that round
     * @return String representing the face of the card
     */
    private String convertNumCardsToDealToWildCards(int numCardsToDeal) {
        if (numCardsToDeal <= 9) return Integer.toString(numCardsToDeal);
        if (numCardsToDeal == 10) return "X";
        if (numCardsToDeal == 11) return "J";
        if (numCardsToDeal == 12) return "Q";
        if (numCardsToDeal == 13) return "K";
        return "";
    }


    /**
     * Housekeeping functions that need to be performed when round ends
     *  Collects card from the each players hand, discard pile. Puts the card in draw Pile
     *  Sets the number of player gone out to 0
     */
    private void roundEnd(){

        for (Player player: gameRepo.getPlayers()){
            gameRepo.getDrawPile().collect(player.getHand());
        }
        gameRepo.getDrawPile().collect(gameRepo.getDiscardPile());
        playersGoneOut = 0;

    }

    /**
     *  Creates a list of players
     * @param humanPlayerIndex Index of human player in the list
     * @param computerPlayerIndex Index of computer player in the list
     * @return list of players
     */
    private List<Player> createPlayers(MutableInt humanPlayerIndex, MutableInt computerPlayerIndex){
        Player human = new Human("Human", 0, new CardCollection());
        humanPlayerIndex.value = 0;
        Player computer = new Computer("Computer", 0, new CardCollection());
        computerPlayerIndex.value = 1;
        List<Player> output = new ArrayList<Player>();
        output.add(human);
        output.add(computer);
        return output;
    }

    /**
     * Distributes card present in drawPile to players and puts one in discard pile
     * @param numCardsToDeal Number of cards to deal in each round
     */
    private void distributeCards(int numCardsToDeal){

        for (int i=0;i<numCardsToDeal;i++){
            for (Player player: gameRepo.getPlayers()){
                player.addCard(gameRepo.getDrawPile().popFront());
            }
        }
        gameRepo.getDiscardPile().addFront(gameRepo.getDrawPile().popFront());
        // remaining cards are in draw Pile
    }

    /**
     * Creates two decks of cards and shuffles
     * @return Two decks of cards contained in the list
     */
    private List<Card> createTwoDecks(){
        List<Card> decks = new ArrayList<Card>();
        decks.addAll(createDeck());
        decks.addAll(createDeck());
        Collections.shuffle(decks);
        return decks;
    }

    /**
     * Creates a deck of cards
     * @return Deck of cards in list
     */
    private List<Card> createDeck(){
        String[] suits = {"C", "D", "H", "S", "T"};
        String[] faces = {"3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K"};
        int numJokers = 3;
        List<Card> output = new ArrayList<Card>();
        for (String suit : suits) {
            for (String face : faces) {
                Card card = new Card(face, suit);
                output.add(card);
            }
        }
        for (int i=1;i<=numJokers;i++){
            Card card = new Card(i);
            output.add(card);
        }
        return output;
    }

    /**
     * Add list of cards to a collection
     * @param cardsCol List of cards that are added.
     * @param cardCollection Object of cardCollection to which the list of cards are added
     */
    private void addCardsToCollection(List<Card> cardsCol, CardCollection cardCollection){
        for (int i=0;i<cardsCol.size();i++){
            cardCollection.addBack(cardsCol.get(i));
        }
    }

    /**
     * Transform array of cards represented as string to list of card objects
     * @param cards Array of string representing cards
     * @return List of card objects
     */
    private List<Card> transformationCards(String[] cards){
        List<Card> output = new ArrayList<Card>();
        for (String elem: cards){
            if (!elem.equals("J1") && !elem.equals("J2") && !elem.equals("J3")){
                Card card = new Card(Character.toString(elem.charAt(0)), Character.toString(elem.charAt(1)));
                output.add( card);
                continue;
            }
            Card card = new Card(Character.getNumericValue(elem.charAt(1)));
            output.add(card);
        }
        return output;
    }

    /**
     * Deserialize data in file and load model
     * @param data Complete data in file passed as string
     */
    private void doAfterDataLoad(String data){


        // Note: Need to find a better way
        instantiateNewGame(0, true);

        StringTokenizer line = new StringTokenizer(data, "\n");

        while(line.hasMoreTokens()){
            Log.d("Debug ", "Do after load");
            String[] tokens = line.nextToken().split("(:|\\s+)");
            if (tokens.length==0) continue; // continue on empty
            Log.d("token_length : ", Integer.toString(tokens.length));
            if ("Round".equals(tokens[0])){
                int round = Integer.parseInt(tokens[2]);
                Log.d("Round : ", Integer.toString(round));
                gameRepo.setRound(round);
                continue;
            }


            if ("Computer".equals(tokens[0])){
                String[] score_line = line.nextToken().split("(:|\\s+)");
                int score = Integer.parseInt(score_line[3]); // Score is at index 3 while parsing
                Log.d("Debug ", "Computer Score: " + Integer.toString(score));
                gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).addToScore(score);

                String[] hand_line_tokens = line.nextToken().split("(:|\\s+)");
                String[] cards = Arrays.copyOfRange(hand_line_tokens, 3, hand_line_tokens.length); // the actual cards start at index 3
                List<Card> cardsList = transformationCards(cards);
                addCardsToCollection(cardsList, gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getHand());
                continue;
            }
            if (tokens[0].equals("Human")){
                String[] score_line = line.nextToken().split("(:|\\s+)");
                int score = Integer.parseInt(score_line[3]); // Score is at 3 while parsing
                Log.d("Debug ", "Human Score: " + Integer.toString(score));
                gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).addToScore(score);
                String[] hand_line_tokens = line.nextToken().split("(:|\\s+)");
                String[] cards = Arrays.copyOfRange(hand_line_tokens, 3, hand_line_tokens.length); // the actual cards start at index 3
                List<Card> cardsList = transformationCards(cards);
                addCardsToCollection(cardsList, gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getHand());
                continue;
            }
            if (tokens[0].equals("Draw")){
                GameRepo test_game_repo = gameRepo;
                String[] draw_pile= Arrays.copyOfRange(tokens, 3, tokens.length);
                List<Card> cardsList = transformationCards(draw_pile);
                addCardsToCollection(cardsList, gameRepo.getDrawPile());
                CardCollection drawPile = gameRepo.getDrawPile();
                continue;
            }
            if (tokens[0].equals("Discard")){
                String[] discard_pile= Arrays.copyOfRange(tokens, 3, tokens.length);
                List<Card> cardsList = transformationCards(discard_pile);
                addCardsToCollection(cardsList, gameRepo.getDiscardPile());
                continue;
            }
            if (tokens[0].equals("Next")){
                String nextPlayer = tokens[3];
                if (nextPlayer.equals("Human")){
                    gameRepo.setNextPlayerIndex(gameRepo.getHumanPlayerIndex());
                } else{
                    gameRepo.setNextPlayerIndex(gameRepo.getComputerPlayerIndex());
                }
            }
        }
        // converts all the loaded cards into wildcards
        gameRepo.getDiscardPile().transformCards(convertNumCardsToDealToWildCards(gameRepo.getRound() + 2));
        gameRepo.getDrawPile().transformCards(convertNumCardsToDealToWildCards(gameRepo.getRound() + 2));
        for (Player player: gameRepo.getPlayers()){
            player.getHand().transformCards(convertNumCardsToDealToWildCards(gameRepo.getRound() + 2));
        }
    }

    /**
     * Saves the game state in file
     * @param context Activity context to get access to the internal storage
     * @param file_name Name of the file in which serialized game is stored
     */
    void saveGame(Context context, String file_name){
        File directory = context.getFilesDir();
        File file = new File(directory, file_name);
        try{
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(serialize().getBytes());
            stream.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        // deleteLog();
    }

    /**
     * Serializes the game to a string
     * @return String representing the state of the game
     */
    String serialize(){
        StringBuilder output = new StringBuilder();
        output.append("Round: ").append(gameRepo.getRound()).append("\n");
        output.append("Computer: \n");
        output.append("\tScore: ").append(gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getScore()).append("\n");
        output.append("\tHand: ").append(getComputerPlayerHand().toString()).append("\n");
        output.append("\n");
        output.append("Human: \n");
        output.append("\tScore: ").append(gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getScore()).append("\n");
        output.append("\tHand: ").append(getHumanPlayerHand().toString()).append("\n");
        output.append("\n");
        output.append("Draw Pile: ").append(getDrawPile().toString()).append("\n");
        output.append("\n");
        output.append("Discard Pile: ").append(getDiscardPile().toString()).append("\n");
        output.append("\n");
        output.append("Next Player: ").append(gameRepo.getNextPlayer()).append("\n");
        String str_output = output.toString();
        Log.d("Debug", str_output);
        return str_output;
    }

    /**
     * Tells which player won the game
     * @return String representing which player won the game
     */
    public String getWinner(){
        int humanScore = gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getScore();
        int computerScore = gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getScore();
        if (humanScore<computerScore) return "Human";
        if (computerScore<humanScore) return "Computer";
        return "Draw";
    }

    /**
     * Gets the cumulative score of the human
     * @return Integer representing the score
     */
    public int getHumanScore(){
        return gameRepo.getPlayers().get(gameRepo.getHumanPlayerIndex()).getScore();

    }

    /**
     * Gets the cumulative score of computer
     * @return Integer representing the score
     */
    public int getComputerScore(){
        return gameRepo.getPlayers().get(gameRepo.getComputerPlayerIndex()).getScore();
    }


}
