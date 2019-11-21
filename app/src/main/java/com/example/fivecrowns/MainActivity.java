package com.example.fivecrowns;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Starting Activity. Allows user to start a new game or load the game.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when activity is first created.
     * @param savedInstanceState @see Android documentation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * Called when user clicks on the load game button.
     * @param view view object that was clicked.
     */
    public void loadGame(View view) {
        Intent myIntent = new Intent(this, LoadGame.class);
        startActivity(myIntent);
    }

    /**
     * Called when user clicks start new game button.
     * @param view view object that was clicked
     */
    public void startNew(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Toss");
        alertDialog.setMessage("Select head or tail");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tail",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (toss() == 0){
                            RepoService.getInstance().instantiateNewGame(0, false);
                        } else{
                            RepoService.getInstance().instantiateNewGame(1, false);
                        }
                        startRoundActivity();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Head", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toss() == 1){
                    RepoService.getInstance().instantiateNewGame(0, false);
                } else{
                    RepoService.getInstance().instantiateNewGame(1, false);
                }
                startRoundActivity();
            }
        });
        alertDialog.show();
    }

    /**
     * Starts round activity
     */
    private void startRoundActivity(){
        Intent myIntent = new Intent(this, Round.class);
        startActivity(myIntent);
    }

    /**
     * Toss called inorder to load the game.
     *
     * @return 0 if tails 1 if heads
     */
    int toss(){
        Random r = new Random();
        return r.nextInt(2);
    }


}
