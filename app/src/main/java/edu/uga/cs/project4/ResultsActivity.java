package edu.uga.cs.project4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

        SQLiteDatabase db = DatabaseHelper.getInstance(this).getWritableDatabase();

        ContentValues values = new ContentValues();


        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
        String currentDateTime = sdf.format(new Date());

        values.put(DatabaseHelper.COLUMN_QUIZ_RESULT, this.score);
        values.put(DatabaseHelper.COLUMN_QUIZ_DATE, currentDateTime);
        db.insert(DatabaseHelper.TABLE_RESULTS, null, values);
    }
}
