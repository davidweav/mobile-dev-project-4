package edu.uga.cs.project4;

public class Country {

    private int id;
    private String name;
    private String continent;

    public Country(int id, String name, String continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Name: " + this.getName() + "\n" +
                "Continent: " + this.getContinent() + "\n";
    }


}
