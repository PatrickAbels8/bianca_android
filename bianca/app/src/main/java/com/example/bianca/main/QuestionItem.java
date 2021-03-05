package com.example.bianca.main;

public class QuestionItem {
    private Integer id;
    private String image;
    private Integer answer;
    private Integer points;

    public QuestionItem(Integer id, String image, Integer answer, Integer points) {
        this.id = id;
        this.image = image;
        this.answer = answer;
        this.points = points;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "QuestionItem{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", answer=" + answer +
                ", points=" + points +
                '}';
    }
}
