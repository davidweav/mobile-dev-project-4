package edu.uga.cs.project4;

public class Quiz {

    private Question[] questions;

    private String date;

    private int score;

    private int numAnswered;

    public Quiz(Question[] questions) {
        this.questions = questions;
        this.score = 0;
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

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (Question q : questions) {
            res.append(q.toString()).append("\n");

        }
        return res.toString();
    }
}
