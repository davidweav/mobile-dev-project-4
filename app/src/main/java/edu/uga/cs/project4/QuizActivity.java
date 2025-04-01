package edu.uga.cs.project4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity implements QuestionFragment.OnAnswerSelectedListener{

    private SQLiteDatabase db;
    private final String[] contintents = {"Asia", "Oceania", "Europe", "South America", "North America", "Africa"};
    private Quiz quiz;

    // Quiz state
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the quiz
        quiz = intializeQuiz();
        Log.i("Quiz Object", quiz.toString());

        ViewPager2 pager = findViewById(R.id.viewpager);
        QuestionAdapter questionAdapter = new QuestionAdapter(
                getSupportFragmentManager(), getLifecycle(), quiz.getQuestions());
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(questionAdapter);

    }

    /**
     * Updates the score based on the selected answer for the previous question.
     */
    private void updateScoreOnSwipe() {
        Question currentQuestion = quiz.getQuestions()[currentQuestionIndex];

        // Check if the previous answer was correct
        if (currentQuestion.isAnswerCorrect()) {
            score++;  // Increment score if the answer was correct
        }

        // Update the score in the Quiz object
        quiz.setScore(score);

        // Log and display the updated score
        Log.i("Score", "Score: " + quiz.getScore());
    }

    /**
     * Initializes a Quiz Object with data to be used to construct the quiz
     * @return the Quiz object
     */
    private Quiz intializeQuiz() {
        Cursor cursor = null;
        ArrayList<Country> countries = new ArrayList<>();
        db = DatabaseHelper.getInstance(this).getWritableDatabase();
        String[] allCountryColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_COUNTRY_NAME, DatabaseHelper.COLUMN_CONTINENT};
        cursor = db.query(DatabaseHelper.TABLE_COUNTRIES, allCountryColumns, null, null, null, null, null);

        // Create an array of all the County objects in the table
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String countryName = cursor.getString(1);
            String continent = cursor.getString(2);

            Country country = new Country(id, countryName, continent);
            countries.add(country);
        }

        db.close();

        // Randomize the Countries
        Collections.shuffle(countries);

        ArrayList<Question> questions = new ArrayList<>();
        // get 6 random countries
        ArrayList<Country> countryList = new ArrayList<>(countries.subList(0, 6));

        // Construct 6 question objects with the random countries
        for (Country country : countryList) {
            ArrayList<String> randomContinents = new ArrayList<>();

            // Find 2 non duplicate continents.
            for (String c : contintents) {
                if (!c.equals(country.getContinent())) {
                    randomContinents.add(c);
                }
            }
            Collections.shuffle(randomContinents);
            String[] incorrectContinents = {randomContinents.get(0), randomContinents.get(1)};

            Question question = new Question(country.getName(), country.getContinent(), incorrectContinents);
            questions.add(question);
        }

        // Construct a quiz with a question array
        Question[] questionsArray = new Question[questions.size()];
        return new Quiz(questions.toArray(questionsArray));
    }

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        if (isCorrect) {
            quiz.incrementScore();
        }
        Log.i("Score ", "Score: " + quiz.getScore());
    }
}
