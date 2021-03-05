package com.example.bianca.restapi.models;

public class Answers {
    private int id;
    private int q_id;
    private int u_id;
    private int correct;

    public Answers(int q_id, int u_id, int correct) {
        this.q_id = q_id;
        this.u_id = u_id;
        this.correct = correct;
    }

    @Override
    public String toString() {
        return "Answers{" +
                "id=" + id +
                "q_id=" + q_id +
                ", u_id=" + u_id +
                ", correct=" + correct +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getQ_id() {
        return q_id;
    }

    public int getU_id() {
        return u_id;
    }

    public int getCorrect() {
        return correct;
    }
}
