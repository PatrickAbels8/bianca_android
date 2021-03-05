package com.example.bianca.restapi.models;

public class Lesson {
    private int id;
    private String title;
    private String image;
    private String video;

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage(){
        return image;
    }

    public String getVideo() {
        return video;
    }
}
