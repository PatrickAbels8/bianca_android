package com.example.bianca.model;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.example.bianca.R;

public class RecyclerItemClass {
    private Integer id;
    private Integer imageUrl;
    private String title;
    private String subtitle;

    @Override
    public String toString() {
        return "RecyclerItemClass{" +
                "id=" + id +
                ", imageUrl=" + imageUrl +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                '}';
    }

    public RecyclerItemClass(Integer id, Integer imageUrl, String title, String subtitle) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getKey(Context context){
        return String.join(context.getString(R.string.delimiter_key), subtitle, title);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
