package com.example.bianca.restapi.models;

public class Examines {
    private int q_id;
    private int l_id;

    @Override
    public String toString() {
        return "Examines{" +
                "q_id=" + q_id +
                ", l_id=" + l_id +
                '}';
    }

    public int getQ_id() {
        return q_id;
    }

    public int getL_id() {
        return l_id;
    }
}
