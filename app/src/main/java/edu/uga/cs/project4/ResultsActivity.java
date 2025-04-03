package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultsActivity extends AppCompatActivity {

    private int score;
    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Retrieve the final score and total questions from the intent extras
        Intent intent = getIntent();
        this.score = intent.getIntExtra("score", 0);
        this.total = intent.getIntExtra("total", 0);

        // Find the TextView in the layout and set the result text
        TextView resultsTextView = findViewById(R.id.resultsTextView);
        resultsTextView.setText("Your final score is " + score + " out of " + total);

        Button returnButton = findViewById(R.id.button);
        returnButton.setOnClickListener((e) -> {
            Intent mainIntent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });

        writeScoreToDB();
    }

    private void writeScoreToDB() {
        // Get the quiz total from the intent
        int total = getIntent().getIntExtra("total", 0);
        
        // Only write completed quizzes to the database
        if (score <= 0 || total <= 0 || score > total) {
            Log.e("ResultsActivity", "Invalid score or total, not saving to database");
            return;
        }

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();

        ContentValues values = new ContentValues();


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
        String currentDateTime = sdf.format(new Date());

        values.put(DatabaseHelper.COLUMN_QUIZ_DATE, currentDateTime);
        values.put(DatabaseHelper.COLUMN_QUIZ_RESULT, score);
        
        // Insert into the database
        long newRowId = db.insert(DatabaseHelper.TABLE_RESULTS, null, values);
        
        // Log the result for debugging
        if (newRowId != -1) {
            Log.d("QuizDebug", "Saved quiz result to database. ID: " + newRowId + ", Date: " + currentDateTime + ", Score: " + score);
        } else {
            Log.e("QuizDebug", "Failed to save quiz result to database");
        }

        // after saving the score, clear the quiz preferences
        clearQuizPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Clear any saved quiz state when results activity is destroyed
        // This ensures no partially completed quiz is restored when starting a new quiz
        clearQuizPreferences();
    }

    private void clearQuizPreferences() {
        SharedPreferences prefs = getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Log.d("ResultsActivity", "Cleared all quiz preferences");
    }
}
