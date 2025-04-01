package edu.uga.cs.project4;

public class Quiz {

    private Question[] questions;

    private String date;

    private double score;

    private int numAnswered;

    public Question[] getQuestions() {
        return questions;
    }

    public Quiz(Question[] questions) {
        this.questions = questions;
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
