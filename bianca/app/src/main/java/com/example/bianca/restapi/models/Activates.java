package com.example.bianca.restapi.models;

public class Activates {
    private int u_id;
    private int l_id;
    private int yes;

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getL_id() {
        return l_id;
    }

    public void setL_id(int l_id) {
        this.l_id = l_id;
    }

    public int getYes() {
        return yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public Activates(int u_id, int l_id, int yes) {
        this.u_id = u_id;
        this.l_id = l_id;
        this.yes = yes;
    }
}
