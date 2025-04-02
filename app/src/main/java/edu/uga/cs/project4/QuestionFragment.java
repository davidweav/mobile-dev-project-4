package edu.uga.cs.project4;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionFragment extends Fragment {

    private static final String ARG_COUNTRY = "country";
    private static final String ARG_CORRECT_CONTINENT = "continent";
    private static final String ARG_INCORRECT_CONTINENTS = "incorrectContinents";
    private static final String ARG_QUESTION_NUMBER = "questionNumber";

    private boolean isCorrect = false;
    private String mCountry;
    private String mContinent;
    private String[] mIncorrectContinents;
    private int mQuestionNumber;
    private RadioButton answerA, answerB, answerC;
    private String mSelectedAnswer = null;  // Store the selected answer

    // Interface to communicate with the parent activity
    public interface OnAnswerSelectedListener {
        void onAnswerSelected(boolean isCorrect, boolean isChanged);
    }
    private OnAnswerSelectedListener mListener;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure the activity implements the callback interface
        if (context instanceof OnAnswerSelectedListener) {
            mListener = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnAnswerSelectedListener");
        }
    }
    public static QuestionFragment newInstance(Question question, int questionNumber) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, question.getCountry());
        args.putString(ARG_CORRECT_CONTINENT, question.getContinent());
        args.putStringArray(ARG_INCORRECT_CONTINENTS, question.getIncorrectContinents());
        args.putInt(ARG_QUESTION_NUMBER, questionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        if (getArguments() != null) {
            mCountry = getArguments().getString(ARG_COUNTRY);
            mContinent = getArguments().getString(ARG_CORRECT_CONTINENT);
            mIncorrectContinents = getArguments().getStringArray(ARG_INCORRECT_CONTINENTS);
            mQuestionNumber = getArguments().getInt(ARG_QUESTION_NUMBER);
        }

        answerA = view.findViewById(R.id.answerA);
        answerB = view.findViewById(R.id.answerB);
        answerC = view.findViewById(R.id.answerC);

        TextView questionTextView = view.findViewById(R.id.question);
        TextView questionNumber = view.findViewById(R.id.questionNumber);

        questionNumber.setText("Question " + mQuestionNumber);
        questionTextView.setText("Where is " + mCountry + " located?");
        answerA.setText(mContinent);
        answerB.setText(mIncorrectContinents[0]);
        answerC.setText(mIncorrectContinents[1]);

        // Handle answer selection
        answerA.setOnClickListener(v -> mSelectedAnswer = answerA.getText().toString());
        answerB.setOnClickListener(v -> mSelectedAnswer = answerB.getText().toString());
        answerC.setOnClickListener(v -> mSelectedAnswer = answerC.getText().toString());

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Info", "onPause");
        if (mSelectedAnswer == null) {
            return;
        }
        // Emit the event and notify the activity whether the answer is correct
        if (mListener != null && mSelectedAnswer != null && !isCorrect) {
            Log.i("Info", "Selected: " + mSelectedAnswer);
            isCorrect = mSelectedAnswer.equals(mContinent);
            mListener.onAnswerSelected(isCorrect, false);
        }
        else if (isCorrect != mSelectedAnswer.equals(mContinent)) {
            isCorrect = mSelectedAnswer.equals(mContinent);
            mListener.onAnswerSelected(isCorrect, true);
        }
    }

}
