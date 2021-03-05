package com.example.bianca.model;

import java.util.List;

public class RecyclerItemSchool {
    String title;
    List<RecyclerItemClass> recyclerItemClassList;

    public RecyclerItemSchool(String title, List<RecyclerItemClass> recyclerItemClassList) {
        this.title = title;
        this.recyclerItemClassList = recyclerItemClassList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<RecyclerItemClass> getRecyclerItemClassList() {
        return recyclerItemClassList;
    }

    public void setRecyclerItemClassList(List<RecyclerItemClass> recyclerItemClassList) {
        this.recyclerItemClassList = recyclerItemClassList;
    }
}
