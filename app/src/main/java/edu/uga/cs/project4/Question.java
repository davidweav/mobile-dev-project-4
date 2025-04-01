package edu.uga.cs.project4;

public class Question {

    private String country;
    private String continent;
    private String[] incorrectContinents;
    private boolean isAnswerCorrect;  // Track if the answer was correct

    public Question(String country, String continent, String[] incorrectContinents) {
        this.country = country;
        this.continent = continent;
        this.incorrectContinents = incorrectContinents;
        this.isAnswerCorrect = false;  // Default to incorrect
    }

    public String getCountry() {
        return country;
    }

    public String getContinent() {
        return continent;
    }

    public String[] getIncorrectContinents() {
        return incorrectContinents;
    }

    public boolean isAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void setAnswerCorrect(boolean isAnswerCorrect) {
        this.isAnswerCorrect = isAnswerCorrect;
    }
}
