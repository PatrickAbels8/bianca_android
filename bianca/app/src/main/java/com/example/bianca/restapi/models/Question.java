package com.example.bianca.restapi.models;

public class Question {
    private int id;
    private String image;
    private int points;
    private int answer;

    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public int getAnswer() {
        return answer;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", answer=" + answer +
                ", points=" + points +
                '}';
    }
}
