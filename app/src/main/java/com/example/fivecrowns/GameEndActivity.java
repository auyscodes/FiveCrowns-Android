package com.example.fivecrowns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * Displays winner and losers of the game and provides option to play again or exit.
 */
public class GameEndActivity extends AppCompatActivity {

    /**
     * Called when activity is first created.
     * Displays winner and each player's score at the end of the game.
     * @param savedInstanceState @see Android documentation for more details
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        TextView textView = findViewById(R.id.winner);
        textView.setText("Winner: " + getWinner());
        TextView human_score = findViewById(R.id.human_score);
        human_score.setText(humanScore());
        TextView computer_score = findViewById(R.id.computer_score);
        computer_score.setText(computerScore());

    }

    /**
     * Get Human Score in specific format
     * @return Formatted human score
     */
    public String humanScore(){
        return "Human Score: " + Integer.toString(RepoService.getInstance().getHumanScore());
    }
    /**
     * Get Computer Score in specific format
     * @return Formatted computer score
     */
    public String computerScore(){
        return "Computer Score: " + Integer.toString(RepoService.getInstance().getComputerScore());
    }

    /**
     * Get the winner of the game
     * @return String with name of the winner
     */
    public String getWinner(){
        return RepoService.getInstance().getWinner();
    }

    /**
     * Called when play again button is clicked
     * @param view View object that was clicked
     */
    public void restartGame(View view) {
        RepoService.getInstance().deleteLog();
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    /**
     * Called when exit game button is clicked
     * @param view View object that was clicked.
     */
    public void exitGame(View view) {
        RepoService.getInstance().deleteLog();
        finish();
        moveTaskToBack(true);
    }
}
