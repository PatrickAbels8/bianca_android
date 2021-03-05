package com.example.bianca.model;

public class RecyclerItemSetting {
    private String title;
    private boolean expanded;

    public RecyclerItemSetting(String title) {
        this.title = title;
        this.expanded = false;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
