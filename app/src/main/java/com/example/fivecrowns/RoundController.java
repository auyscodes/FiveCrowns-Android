package com.example.fivecrowns;

import android.content.Context;

class RoundController {
    /**
     * Stores instance of the repoService class
     */
    private RepoService repoService;

    /**
     * Creates instance of round controller class
     */
    RoundController(){
        repoService = RepoService.getInstance();
    }

    /**
     * To update display of the round activity class.
     * @param display Activity class that implements the IDisplay interface.
     * @return True if the Game has completed and Round class needs to start GameEndActivity
     */
    boolean updateDisplay(IDisplay display){
        return repoService.updateDisplay(display);
    }

    /**
     * To tell model that user wants to pick from discard pile
     */
    void pickFromDiscard(){
        repoService.pickFromDiscard();

    }
    /**
     * To tell model that user wants to pick from draw pile
     */
    void pickFromDraw(){
        repoService.pickFromDraw();
    }
    /**
     * Logs the help provided by computer when the user asks for help throwing a card
     */
    void logThrowHelp(){
        repoService.throwHelp();
    }

    /**
     * Logs the help provided by computer when the user asks for help picking a card
     */
    void logPickHelp(){
        repoService.pickHelp();
    }

    /**
     * To check if a card is picked by the user
     * @return True if the card is picked
     */
    boolean is_picked(){
        return repoService.isPicked();
    }

    /**
     * To throw the card picked by user
     * @param card Actual card picked by user
     */
    void throwCard(Card card){
        repoService.throwCard( card);
    }

    /**
     * To save the current game state in a file
     * @param context Activity context needed to get access to the storage
     * @param file_name Name of the save file entered by the user
     */
    void saveGame(Context context, String file_name){
        repoService.saveGame(context, file_name);
    }

}
