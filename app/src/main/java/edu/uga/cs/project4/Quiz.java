package edu.uga.cs.project4;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Quiz implements Serializable {

    private Question[] questions;
    private String date;
    private int score;
    private int numAnswered;
    private boolean[] answeredQuestions;

    public Quiz(Question[] questions) {
        this.questions = questions;
        this.score = 0;
        this.numAnswered = 0;
        this.answeredQuestions = new boolean[questions.length];
        
        // Set current date when quiz is created
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm a", Locale.getDefault());
        this.date = sdf.format(new Date());
    }

    public void incrementScore() {
        this.score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public Question[] getQuestions() {
        return questions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumAnswered() {
        return numAnswered;
    }

    public void incrementAnswered() {
        this.numAnswered++;
    }

    public boolean isQuestionAnswered(int index) {
        if (index >= 0 && index < answeredQuestions.length) {
            return answeredQuestions[index];
        }
        return false;
    }

    public void setQuestionAnswered(int index, boolean answered) {
        if (index >= 0 && index < answeredQuestions.length) {
            answeredQuestions[index] = answered;
        }
    }

    public boolean isCompleted() {
        return numAnswered >= questions.length;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Question q : questions) {
            res.append(q.toString()).append("\n");
        }
        return res.toString();
    }
}