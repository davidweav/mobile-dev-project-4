package edu.uga.cs.project4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private Button startQuizBtn;
    private Button resultsBtn;
    private static final String PREFS_NAME = "QuizPrefs";
    private static final String KEY_QUIZ_STATE = "quizState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the database helper
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        // Populate the database from CSV
        DatabasePopulation dbPopulation = new DatabasePopulation(this);
        dbPopulation.populateFromCSV(this); // Populate data from CSV

        startQuizBtn = findViewById(R.id.startQuizButton);
        resultsBtn = findViewById(R.id.viewPastResultsButton);
        
        startQuizBtn.setOnClickListener((view) -> {
            // Check if there's an in-progress quiz
            if (hasInProgressQuiz()) {
                // Ask the user if they want to continue the quiz or start a new one
                new AlertDialog.Builder(this)
                    .setTitle("Continue Quiz")
                    .setMessage("You have an unfinished quiz. Would you like to continue it?")
                    .setPositiveButton("Continue", (dialog, which) -> {
                        // Continue the in-progress quiz
                        Intent intent = new Intent(this, QuizActivity.class);
                        intent.putExtra("continue_quiz", true);
                        startActivity(intent);
                    })
                    .setNegativeButton("New Quiz", (dialog, which) -> {
                        // Start a new quiz
                        clearAnyExistingQuizState();
                        Intent intent = new Intent(this, QuizActivity.class);
                        intent.putExtra("start_new_quiz", true);
                        startActivity(intent);
                    })
                    .show();
            } else {
                // No in-progress quiz, start a new one
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("start_new_quiz", true);
                startActivity(intent);
            }
        });

        resultsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResultsHistoryActivity.class);
            startActivity(intent);
        });
    }
    
    /**
     * Check if there's an in-progress quiz saved
     */
    private boolean hasInProgressQuiz() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String quizJson = prefs.getString(KEY_QUIZ_STATE, null);
        
        if (quizJson != null) {
            // If there's saved quiz data, verify it's a valid quiz
            try {
                Gson gson = new Gson();
                Quiz quiz = gson.fromJson(quizJson, Quiz.class);
                
                // Check if the quiz is valid and not completed
                if (quiz != null && !quiz.isCompleted()) {
                    Log.d("MainActivity", "Found in-progress quiz with " + 
                            quiz.getNumAnswered() + " answers and score " + quiz.getScore());
                    return true;
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error parsing saved quiz", e);
            }
        }
        return false;
    }

    /**
     * Clears any existing quiz state to ensure a fresh start
     */
    private void clearAnyExistingQuizState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        Log.d("MainActivity", "Cleared all saved quiz state before starting new quiz");
    }
}