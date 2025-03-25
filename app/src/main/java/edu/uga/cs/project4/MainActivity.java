package edu.uga.cs.project4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database helper
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        // Populate the database from CSV
        DatabasePopulation dbPopulation = new DatabasePopulation(this);
        dbPopulation.populateFromCSV(this); // Populate data from CSV

    }
}
