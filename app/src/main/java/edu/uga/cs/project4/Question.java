package edu.uga.cs.project4;

public class Question {

    private String country;

    private String continent;

    private String[] incorrectContinents;

    public Question(String country, String continent, String[] incorrectContinents) {
        this.country = country;
        this.continent = continent;
        this.incorrectContinents = incorrectContinents;
    }

    public String toString() {
        return "Country: " + this.country +  "\n" +
                "1.) " + this.continent + "\n" +
                "2.) " + this.incorrectContinents[0] + "\n" +
                "3.) " + this.incorrectContinents[1];
    }

}
