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

public class MainActivity extends AppCompatActivity {

    private Button startQuizBtn;
    private Button resultsBtn;

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
            // Clear any existing quiz state before starting a new quiz
            clearAnyExistingQuizState();

            Intent intent = new Intent(this, QuizActivity.class);
            startActivity(intent);
        });

        resultsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, ResultsHistoryActivity.class);
            startActivity(intent);
        });
    }

    private void clearAnyExistingQuizState() {
        SharedPreferences prefs = getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        // Also clear from our main quiz preferences
        prefs = getSharedPreferences("QuizPrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.apply();
        
        Log.d("MainActivity", "Cleared all saved quiz state before starting new quiz");
    }
}
