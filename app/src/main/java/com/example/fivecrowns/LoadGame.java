package com.example.fivecrowns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import java.io.File;
import java.io.FileOutputStream;


public class LoadGame extends AppCompatActivity {
    /**
     * Instance of Controller for the class
     */
    private LoadGameController loadGameController;


    String amruth_case_1 = "Round: 2\n" +
            "  \n" +
            "Computer:\n" +
            "   Score: 6\n" +
            "   Hand: 9C 5C 9D 9H\n" +
            " \n" +
            "Human:\n" +
            "   Score: 0\n" +
            "   Hand: 5T 8D 7T 8T\n" +
            "\n" +
            "Draw Pile:  4H 4C 3S 5S 5H 6H 8H 7H XC J2 XD QD KD KS XS 9T QT JT 5D QS 7C 6C 8C KH QH XH JC KC QC JH 4T 3T 3D 4D 4S 6T 6S 5S 4S 3S 7S J1 3C 5C 6C 7C 9H JH QH KH 4T 6T J3 QS XS 9S 8C 4C 9C QC KC JC XC 8S JS KS J2 6H 3H 4H 5H XH 8H 7H XD QD KD 6D 5D 3D 4D 7D JD 9D 8D 8T 5T 3T 9T XT QT KT JT 7T  \n" +
            "\n" +
            "Discard Pile: JD J1 J3 KT XT 3C 6S 7S 3H JS 7D 6D 8S 9S\n" +
            "\n" +
            "Next Player: Computer";

    String amruth_case_2 = "Round: 3\n" +
            "\n" +
            "Computer:\n" +
            "   Score: 6\n" +
            "   Hand: 4H 7H 8D 9H QH\n" +
            " \n" +
            "Human:\n" +
            "   Score: 10\n" +
            "   Hand: 9C 8H 9D 9H JH\n" +
            "\n" +
            "Draw Pile:  5H 5C 5D XT QT JT KT KH 3H J3 6H XH 9S XS JS QS KS 6D 3D 7D 4D XD XC QC KC J1 5T 3T 8T 4T 6T 6S 5S 4S 3S 7S J1 3C 5C 6C 7C JH QH KH 4T 6T J3 QS XS 9S 8C 4C 9C QC KC JC XC 8S JS KS J2 6H 3H 4H 5H XH 8H 7H XD QD KD 6D 5D 3D 4D 7D JD 9D 8D 8T 5T 3T 9T XT QT KT JT 7T\n" +
            "\n" +
            "Discard Pile: 9T 5S 4S 8S 3S 6S 7S JD KD QD J2 4C 3C 8C 6C 7C JC 7T \n" +
            "\n" +
            "Next Player: Human";

    String amruth_case_3 = "Round: 11\n" +
            "\n" +
            "Computer:\n" +
            "   Score: 167\n" +
            "   Hand: 3H 3C 4C 5D 6C 7T 7C 7D 8D 9D XS QS QT\n" +
            " \n" +
            "Human:\n" +
            "   Score: 173\n" +
            "   Hand: 3C 3D 4H 5C 6H 8T 9T XT XH XD XC JS QC \n" +
            "\n" +
            "Draw Pile: J3 KC KD J2 KT KH J1 6S 5S 4S 3S 7S J1 5C 6C 7C 9H JH QH KH 4T 6T J3 QS XS 9S 8C 4C 9C QC KC JC XC 8S JS KS J2 6H 3H 4H 5H XH 8H 7H XD QD KD 6D 5D 3D 4D 7D JD 9D 8D 8T 5T 3T 9T XT QT KT JT 7T \n" +
            "\n" +
            "Discard Pile: 5H 3S 4S 8H 9H 7S 8S 9S KS 3T JH QH 8C 6D JD QD 5H 7H 6T JT 9C JC 5S 6S 4T 5T 4D \n" +
            "\n" +
            "Next Player: Human";



    /**
     * Called  when new activity is created.
     * Saves the test cases in files and loads them on screen.
     * @param savedInstanceState @see android documentation for more details
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        loadGameController = new LoadGameController();
        saveGame();
        File directory = this.getFilesDir();

        File[] files = directory.listFiles();
        LinearLayout linearLayout = findViewById(R.id.files_linear);
        Log.d("files.length", Integer.toString(files.length));
        for (int i=0;i<files.length;i++){
            Button textView = new Button(LoadGame.this);
            textView.setText(files[i].getName());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
            textView.setOnClickListener(new BtnClickListener(files[i]));
            linearLayout.addView(textView);
            Log.d("files.length loop", Integer.toString(i));
        }
    }

    /**
     * Starts Round activity
     */
    private void startRoundActivity(){
        Intent myIntent = new Intent(this, Round.class);
        startActivity(myIntent);
    }

    /**
     * Listener called when user clicks on file load button
     */
    public class BtnClickListener implements View.OnClickListener{
        private File file;
        BtnClickListener(File file){
            this.file = file;
        }
        @Override
        public void onClick(View view) {
            loadGameController.loadRepo(file);
            startRoundActivity();

        }
    }

    /**
     * Saves game to the file.
     * Note: Remove later
     * @param file_name
     * @param contents
     */
    public void saveGame(String file_name, String contents){
        File directory = this.getFilesDir();
        File file = new File(directory, file_name);
        try{
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(contents.getBytes());
            stream.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Saves the test cases
     * Note: Remove later
     */
    public void saveGame(){
        saveGame("case1.txt", amruth_case_1);
        saveGame("case2.txt", amruth_case_2);
        saveGame("case3.txt", amruth_case_3);
    }
}
