package com.example.fivecrowns;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.StringTokenizer;

public class LogActivity extends AppCompatActivity {
    /**
     * Called when activity is created
     * @param savedInstanceState @see android documentation
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        display();
        final ScrollView scrollView = findViewById(R.id.scroll_log);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    /**
     * Displays the log file on screen
     */
    public void display(){
        LinearLayout layout = findViewById(R.id.lineView);

        String log = RepoService.getInstance().getLogFile();
        StringTokenizer line = new StringTokenizer(log, "\n");

        while(line.hasMoreTokens()){
            TextView textView = new TextView(this);
            textView.setText(line.nextToken());
            layout.addView(textView);
        }

    }
}
