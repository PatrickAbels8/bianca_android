package com.example.bianca.restapi.models;

public class User {
    private int id;
    private String name;
    private String pass;
    private int elo;

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public User(int id, int elo){
        this.id = id;
        this.elo = elo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public int getElo(){
        return elo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                ", elo=" + elo +
                '}';
    }
}
