package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieve the final score and total questions from the intent extras
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int total = intent.getIntExtra("total", 0);

        // Find the TextView in the layout and set the result text
        TextView resultsTextView = findViewById(R.id.resultsTextView);
        resultsTextView.setText("Your final score is " + score + " out of " + total);

        Button returnButton = findViewById(R.id.button);
        returnButton.setOnClickListener((e) -> {
            Intent mainIntent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }
}
