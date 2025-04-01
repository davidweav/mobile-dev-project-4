package edu.uga.cs.project4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private final String[] contintents = {"Asia", "Oceania", "Europe", "South America", "North America", "Africa"};
    private Quiz quiz;
    
    // UI components
    private FrameLayout questionContainer;
    private TextView questionNumberTextView;
    private TextView scoreTextView;
    private Button nextButton;
    private Button prevButton;
    private Button submitButton;
    
    // Quiz state
    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean[] answeredQuestions;
    private QuestionView currentQuestionView;

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
        
        // Initialize answeredQuestions array
        answeredQuestions = new boolean[quiz.getQuestions().length];
        
        // Initialize UI components
        initializeUI();
        
        // Display the first question
        displayQuestion(currentQuestionIndex);
        
        // Set up button click listeners
        setupButtonListeners();
    }
    
    private void initializeUI() {
        // Find views from layout
        questionContainer = findViewById(R.id.questionContainer);
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.previousButton);
        submitButton = findViewById(R.id.submitButton);
        
        // Initialize the score display
        updateScoreDisplay();
    }
    
    private void setupButtonListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex < quiz.getQuestions().length - 1) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                } else {
                    Toast.makeText(QuizActivity.this, "This is the last question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--;
                    displayQuestion(currentQuestionIndex);
                } else {
                    Toast.makeText(QuizActivity.this, "This is the first question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuestionView != null) {
                    boolean isCorrect = currentQuestionView.checkAnswer();
                    
                    // Only update score if this question hasn't been answered correctly before
                    if (isCorrect && !answeredQuestions[currentQuestionIndex]) {
                        score++;
                        answeredQuestions[currentQuestionIndex] = true;
                        updateScoreDisplay();
                    }
                    
                    // Check if quiz is complete
                    checkQuizCompletion();
                }
            }
        });
    }
    
    private void displayQuestion(int index) {
        // Update question number display
        questionNumberTextView.setText("Question " + (index + 1) + " of " + quiz.getQuestions().length);
        
        // Clear previous question view
        questionContainer.removeAllViews();
        
        // Create new question view for current question
        currentQuestionView = new QuestionView(this);
        currentQuestionView.setQuestion(quiz.getQuestions()[index]);
        
        // Add to container
        questionContainer.addView(currentQuestionView);
    }
    
    private void updateScoreDisplay() {
        scoreTextView.setText("Score: " + score + "/" + quiz.getQuestions().length);
    }
    
    private void checkQuizCompletion() {
        boolean allAnswered = true;
        for (boolean answered : answeredQuestions) {
            if (!answered) {
                allAnswered = false;
                break;
            }
        }
        
        if (allAnswered) {
            // All questions have been answered correctly
            Toast.makeText(this, "Quiz complete! Final score: " + score + "/" + 
                    quiz.getQuestions().length, Toast.LENGTH_LONG).show();
            
            // You could also navigate to a results screen here
            // Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
            // intent.putExtra("SCORE", score);
            // intent.putExtra("TOTAL", quiz.getQuestions().length);
            // startActivity(intent);
            // finish();
        }
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
            Log.i("Country Object", country.toString());
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
            Log.i("Question Object", question.toString());
        }

        // Construct a quiz with a question array
        Question[] questionsArray = new Question[questions.size()];
        return new Quiz(questions.toArray(questionsArray));
    }
}