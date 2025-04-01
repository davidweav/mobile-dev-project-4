package edu.uga.cs.project4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionView extends ConstraintLayout {

    private TextView questionTextView;
    private RadioGroup continentRadioGroup;
    private RadioButton option1RadioButton;
    private RadioButton option2RadioButton;
    private RadioButton option3RadioButton;
    private TextView feedbackTextView;
    
    private Question question;
    private List<String> shuffledOptions;
    private int correctOptionIndex;

    public QuestionView(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context) {
        // Inflate the layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.question, this, true);
        
        // Initialize views
        questionTextView = findViewById(R.id.questionTextView);
        continentRadioGroup = findViewById(R.id.continentRadioGroup);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        feedbackTextView = findViewById(R.id.feedbackTextView);
    }
    
    /**
     * Set the question data and display it
     */
    public void setQuestion(Question question) {
        this.question = question;
        
        // Create the question text using string resources
        Context context = getContext();
        String questionTemplate = context.getString(R.string.question_template);
        questionTextView.setText(String.format(questionTemplate, question.getCountry()));
        
        // Prepare the answer options (shuffled)
        shuffledOptions = new ArrayList<>();
        shuffledOptions.add(question.getContinent());
        shuffledOptions.add(question.getIncorrectContinents()[0]);
        shuffledOptions.add(question.getIncorrectContinents()[1]);
        
        // Shuffle the options
        Collections.shuffle(shuffledOptions);
        
        // Store the index of the correct answer
        correctOptionIndex = shuffledOptions.indexOf(question.getContinent());
        
        // Set the radio button texts using string resources
        String optionFormat = context.getString(R.string.option_format);
        option1RadioButton.setText(String.format(optionFormat, "A", shuffledOptions.get(0)));
        option2RadioButton.setText(String.format(optionFormat, "B", shuffledOptions.get(1)));
        option3RadioButton.setText(String.format(optionFormat, "C", shuffledOptions.get(2)));
        
        // Clear any previous selection
        continentRadioGroup.clearCheck();
        
        // Hide feedback
        feedbackTextView.setVisibility(View.GONE);
    }
    
    /**
     * Check if the selected answer is correct
     * @return true if correct, false otherwise
     */
    public boolean checkAnswer() {
        int selectedId = continentRadioGroup.getCheckedRadioButtonId();
        
        // If nothing is selected
        if (selectedId == -1) {
            return false;
        }
        
        // Determine which option was selected (0, 1, or 2)
        int selectedOption;
        if (selectedId == option1RadioButton.getId()) {
            selectedOption = 0;
        } else if (selectedId == option2RadioButton.getId()) {
            selectedOption = 1;
        } else {
            selectedOption = 2;
        }
        
        // Show feedback
        boolean isCorrect = (selectedOption == correctOptionIndex);
        showFeedback(isCorrect);
        
        return isCorrect;
    }
    
    /**
     * Show feedback to the user
     */
    private void showFeedback(boolean isCorrect) {
        Context context = getContext();
        
        if (isCorrect) {
            feedbackTextView.setText(R.string.correct_answer);
            feedbackTextView.setTextColor(context.getColor(android.R.color.holo_green_dark));
        } else {
            String incorrectFeedback = context.getString(R.string.incorrect_answer_format);
            feedbackTextView.setText(String.format(incorrectFeedback, question.getContinent()));
            feedbackTextView.setTextColor(context.getColor(android.R.color.holo_red_dark));
        }
        feedbackTextView.setVisibility(View.VISIBLE);
    }
}