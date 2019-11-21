package com.example.fivecrowns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import static android.icu.lang.UCharacter.toLowerCase;

public class Round extends AppCompatActivity implements IDisplay {
    /**
     * Stores the controller class
     */
    private RoundController roundController;

    /**
     * Called when activity is created by Android operating system
     * @param savedInstanceState @see android documentation for more details
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);

        roundController = new RoundController();
        if (roundController.updateDisplay(this)) {displayGameEnd();}
    }


    /**
     * Starts Game End activity
     */
    private void displayGameEnd(){
        Intent myIntent = new Intent(this, GameEndActivity.class);
        startActivity(myIntent);
    }

    /**
     * Used for displaying CardCollection on android screen
     * Adds imageview related to the card in cardcollection to the passed layout view
     * @param hand Collection of cards represented by card collection
     * @param layout layout to which the views are added
     */
    private void display(CardCollection hand, LinearLayout layout){
        layout.removeAllViews();
        Log.d("displayHumanHand", "called");
        Log.d("humanHandSize", Integer.toString(hand.getSize()));

        for (int i=0;i<hand.getSize();i++){
            Card card = hand.getCardAt(i);
            Log.d("humanHandCard", card.small_suit_face());
            int id = getApplicationContext().getResources().getIdentifier(toLowerCase(card.small_suit_face()), "drawable", getApplicationContext().getPackageName());
            Log.d("humanHandCardDrawName", getApplicationContext().getResources().getResourceEntryName(id));
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(id);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(250,250));
            imageView.setTag(card.toString());
            String layout_name = getApplicationContext().getResources().getResourceEntryName(layout.getId());
            if ((layout_name.equals("drawPile") || layout_name.equals("discardPile")) && i==0){
                imageView.setOnClickListener(new DrawDiscardClickListener(card));
            }
            if (layout_name.equals("humanHand")){
                imageView.setOnClickListener(new HumanHandClickListener(card));
            }
            layout.addView(imageView);
        }
    }

    /**
     * Displays the cards in human hand on screen
     * @param hand Collection of cards in human hand
     */
    @Override
    public void displayHumanHand(CardCollection hand) {
        display(hand, (LinearLayout) findViewById(R.id.humanHand));
    }

    /**
     * Displays the cards in computer hand on screen
     * @param hand Collection of cards in computer hand
     */
    @Override
    public void displayComputerHand(CardCollection hand) {
        display(hand, (LinearLayout) findViewById(R.id.computerHand));
    }

    /**
     * Displays the cards in discard pile on screen
     * @param hand Collection of cards in discard pile
     */
    @Override
    public void displayDiscardPile(CardCollection hand) {
        display(hand, (LinearLayout) findViewById(R.id.discardPile));
    }

    /**
     * Displays the cards in draw pile on screen
     * @param hand Collection of cards in draw pile
     */
    @Override
    public void displayDrawPile(CardCollection hand) {
        display(hand, (LinearLayout) findViewById(R.id.drawPile));
    }

    /**
     * Updates round number on the screen
     * @param num Number representing round
     */
    @Override
    public void updateRound(int num) {
        TextView textView = findViewById(R.id.roundNo);
        textView.setText("Round No: " + num);
    }

    /**
     * Updates computer score on screen
     * @param num Number representing computer's cumulative score
     */
    @Override
    public void updateComputerScore(int num) {
        TextView textView = findViewById(R.id.computerScore);
        textView.setText("Computer Score: " + num);
    }

    /**
     * Updates human score on screen
     * @param num Number representing human's cumulative score
     */
    @Override
    public void updateHumanScore(int num) {
        TextView textView = findViewById(R.id.humanScore);
        textView.setText("Human Score: " + num);
    }


    /**
     * Opens log activity to display help for picking and throwing
     * @param view Represents clicked view object
     */
    public void showHelp(View view) {
        Log.d("Debug ", "picked state: " + roundController.is_picked());
        if (!roundController.is_picked()){
            roundController.logPickHelp();
        } else{
            roundController.logThrowHelp();
        }
        Intent myIntent = new Intent(this, LogActivity.class);
        startActivity(myIntent);
    }

    /**
     * Allows the user to save the state of game. Asks for filename while saving the game.
     * @param view Represents clicked view object
     */
    public void saveGame(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(dialogView);

        final EditText file_name = (EditText) dialogView.findViewById(R.id.file_name);

        builder.setTitle("Save Game");
        builder.setMessage("Enter File Name");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                roundController.saveGame(getApplicationContext(), file_name.getText().toString());
                closeGame();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog save_alert_dialog = builder.create();
        save_alert_dialog.show();

    }

    /**
     * Called by after state of the game is saved in order to exit the current round activity.
     */
    void closeGame(){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    /**
     * Displays the log in LogActivity
     * @param view Represents clicked view object
     */
    public void showLog(View view) {
        Intent myIntent = new Intent(this, LogActivity.class);
        startActivity(myIntent);
    }

    /**
     * Listener called when user clicks the card in draw or discard pile to pick
     */
    class DrawDiscardClickListener implements View.OnClickListener{

        private Card card;
        DrawDiscardClickListener(Card card){
            this.card = card;
        }
        @Override
        public void onClick(View view) {

            if (!roundController.is_picked()){
                Log.d("Human hand ", card.toString() + " clicked");


                if (R.id.discardPile == ((LinearLayout) view.getParent()).getId()){
                    roundController.pickFromDiscard();
                    // picked = true;
                    updateDisplay();
                    Log.d("View Parent", "discardPile");
                    return;
                }
                if (R.id.drawPile == ((LinearLayout) view.getParent()).getId()){
                    roundController.pickFromDraw();
                    // picked = true;
                    updateDisplay();
                    Log.d("View Parent", "drawPile");
                }
            }


        }
    }

    /**
     * Updates display to see changes.
     * Displays Game End activity if the game has ended.
     */
    public void updateDisplay(){
        if (roundController.updateDisplay(this)){displayGameEnd();}
    }

    /**
     * Listener called when user wants to throw the card picked
     */
    class HumanHandClickListener implements View.OnClickListener{
        private Card card;
        HumanHandClickListener(Card card){
            this.card = card;
        }
        @Override
        public void onClick(View view) {
            if (roundController.is_picked()){
                Log.d("Human hand", card.toString() + " clicked");
                if (R.id.humanHand == ((LinearLayout) view.getParent()).getId()){
                    Log.d("View Parent", "discardPile");
                    roundController.throwCard(card);
                    // picked = false;
                    updateDisplay();
                }
            }
        }
    }



}
