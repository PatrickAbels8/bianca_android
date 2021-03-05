package com.example.bianca.restapi.models;

public class Covers {
    private int t_id;
    private int l_id;

    @Override
    public String toString() {
        return "Covers{" +
                "t_id=" + t_id +
                ", l_id=" + l_id +
                '}';
    }

    public int getT_id() {
        return t_id;
    }

    public int getL_id() {
        return l_id;
    }
}
