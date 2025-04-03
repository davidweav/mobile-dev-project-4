package edu.uga.cs.project4;

public class Result {
    private int id;
    private String date;
    private float score;

    public Result(int id, String date, float score) {
        this.id = id;
        this.date = date;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Date: " + date + " | Score: " + score;
    }
}