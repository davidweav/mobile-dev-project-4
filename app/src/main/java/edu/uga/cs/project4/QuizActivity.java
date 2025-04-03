package edu.uga.cs.project4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity implements QuestionFragment.OnAnswerSelectedListener {

    private SQLiteDatabase db;
    private final String[] contintents = {"Asia", "Oceania", "Europe", "South America", "North America", "Africa"};
    private Quiz quiz;
    
    // Quiz state
    private int currentQuestionIndex = 0;
    private int score = 0;
    
    // Constants for SharedPreferences
    private static final String PREFS_NAME = "QuizPrefs";
    private static final String KEY_QUIZ_STATE = "quizState";
    private static final String KEY_CURRENT_INDEX = "currentIndex";

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

        // Check if the user explicitly requested a new quiz
        boolean forceNewQuiz = getIntent().getBooleanExtra("start_new_quiz", false);

        // Restore or initialize the quiz
        if (savedInstanceState != null && !forceNewQuiz) {
            // Only restore state from savedInstanceState if not explicitly requesting new quiz
            restoreQuizState(savedInstanceState);
        } else if (!forceNewQuiz) {
            // Try to restore from SharedPreferences only if not explicitly starting new quiz
            if (!restoreQuizStateFromPrefs()) {
                // Initialize a new quiz if no saved state
                quiz = initializeQuiz();
                Log.i("Quiz Object", "New quiz created: " + quiz.toString());
            }
        } else {
            // Force new quiz - clear any saved state first
            clearSavedQuizState();
            quiz = initializeQuiz();
            Log.i("Quiz Object", "New quiz explicitly created: " + quiz.toString());
        }

        ViewPager2 pager = findViewById(R.id.viewpager);
        QuestionAdapter questionAdapter = new QuestionAdapter(
                getSupportFragmentManager(), getLifecycle(), quiz.getQuestions());
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(questionAdapter);
        
        // Set to current question index (useful when restoring state)
        if (currentQuestionIndex > 0) {
            pager.setCurrentItem(currentQuestionIndex, false);
        }

        // Inside onCreate() after setting up the pager...
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentQuestionIndex = position;
                Log.i("Page Swiped", "Current Question Index: " + currentQuestionIndex);
                
                // If we've reached the last page and all questions are answered...
                if (position == quiz.getQuestions().length - 1 && quiz.isCompleted()) {
                    Log.i("Results Activity", "All questions answered, preparing for results");
                } else if (position == quiz.getQuestions().length) {
                    Log.i("Results Activity", "Results Activity being created");
                    Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                    intent.putExtra("score", quiz.getScore());
                    intent.putExtra("total", quiz.getQuestions().length);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current quiz state
        Gson gson = new Gson();
        String quizJson = gson.toJson(quiz);
        outState.putString(KEY_QUIZ_STATE, quizJson);
        outState.putInt(KEY_CURRENT_INDEX, currentQuestionIndex);
        Log.d("QuizActivity", "Saved state - current index: " + currentQuestionIndex);
    }

    private void restoreQuizState(Bundle savedInstanceState) {
        // Restore quiz from saved instance state
        Gson gson = new Gson();
        String quizJson = savedInstanceState.getString(KEY_QUIZ_STATE);
        if (quizJson != null) {
            quiz = gson.fromJson(quizJson, Quiz.class);
            currentQuestionIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX, 0);
            score = quiz.getScore();
            Log.d("QuizActivity", "Restored from savedInstanceState - index: " + currentQuestionIndex + ", score: " + score);
        } else {
            quiz = initializeQuiz();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Only save state if the activity is not finishing (i.e., temporarily paused)
        // This way we don't save state when the user is leaving the quiz permanently
        if (!isFinishing()) {
            // Save current quiz state to SharedPreferences when app is paused
            saveQuizStateToPrefs();
        } else if (quiz != null && !quiz.isCompleted()) {
            // Clear shared preferences if we're finishing and the quiz is incomplete
            clearSavedQuizState();
        }
    }

    private void saveQuizStateToPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        Gson gson = new Gson();
        String quizJson = gson.toJson(quiz);
        
        editor.putString(KEY_QUIZ_STATE, quizJson);
        editor.putInt(KEY_CURRENT_INDEX, currentQuestionIndex);
        editor.apply();
        
        Log.d("QuizActivity", "Saved quiz state to SharedPreferences - index: " + currentQuestionIndex);
    }

    private boolean restoreQuizStateFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String quizJson = prefs.getString(KEY_QUIZ_STATE, null);
        
        if (quizJson != null) {
            Gson gson = new Gson();
            quiz = gson.fromJson(quizJson, Quiz.class);
            currentQuestionIndex = prefs.getInt(KEY_CURRENT_INDEX, 0);
            score = quiz.getScore();
            Log.d("QuizActivity", "Restored quiz from SharedPreferences - index: " + currentQuestionIndex);
            return true;
        }
        return false;
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
        
        // Mark the question as answered
        quiz.setQuestionAnswered(currentQuestionIndex, true);
        quiz.incrementAnswered();

        // Log and display the updated score
        Log.i("Score", "Score: " + quiz.getScore());
    }

    /**
     * Initializes a Quiz Object with data to be used to construct the quiz
     * @return the Quiz object
     */
    private Quiz initializeQuiz() {
        Cursor cursor = null;
        ArrayList<Country> countries = new ArrayList<>();
        db = DatabaseHelper.getInstance(this).getWritableDatabase();
        String[] allCountryColumns = {
            DatabaseHelper.COLUMN_ID, 
            DatabaseHelper.COLUMN_COUNTRY_NAME, 
            DatabaseHelper.COLUMN_CONTINENT
        };
        
        cursor = db.query(
            DatabaseHelper.TABLE_COUNTRIES, 
            allCountryColumns, 
            null, null, null, null, null
        );

        // Create an array of all the County objects in the table
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String countryName = cursor.getString(1);
            String continent = cursor.getString(2);

            Country country = new Country(id, countryName, continent);
            Log.i("Country Object", country.toString());
            countries.add(country);
        }

        cursor.close();
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
            Log.i("Question Object", question.toString());
        }

        // Construct a quiz with a question array
        Question[] questionsArray = new Question[questions.size()];
        return new Quiz(questions.toArray(questionsArray));
    }

    @Override
    public void onAnswerSelected(boolean isCorrect, boolean isChanged) {
        int cur = quiz.getScore();
        if (isCorrect && !quiz.isQuestionAnswered(currentQuestionIndex)) {
            quiz.setScore(cur + 1);
            quiz.getQuestions()[currentQuestionIndex].setAnswerCorrect(true);
        } else if (!isCorrect && isChanged && quiz.isQuestionAnswered(currentQuestionIndex)) {
            quiz.setScore(cur - 1);
            quiz.getQuestions()[currentQuestionIndex].setAnswerCorrect(false);
        }
        
        // Mark question as answered
        quiz.setQuestionAnswered(currentQuestionIndex, true);
        quiz.incrementAnswered();
        
        Log.i("Score ", "Score: " + quiz.getScore() + ", Questions answered: " + quiz.getNumAnswered());
        
        // Check if quiz is completed to prepare for results
        if (quiz.isCompleted() && currentQuestionIndex == quiz.getQuestions().length - 1) {
            Log.i("Quiz Status", "Quiz completed, ready for results");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Clear saved quiz state from SharedPreferences when the activity is destroyed
        if (quiz != null && !quiz.isCompleted()) {
            Log.d("QuizActivity", "Discarding incomplete quiz on app termination");
            clearSavedQuizState();
        }
    }

    /**
     * Clears any saved quiz state from SharedPreferences
     */
    private void clearSavedQuizState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_QUIZ_STATE);
        editor.remove(KEY_CURRENT_INDEX);
        editor.apply();
        Log.d("QuizActivity", "Cleared saved quiz state from SharedPreferences");
    }
}