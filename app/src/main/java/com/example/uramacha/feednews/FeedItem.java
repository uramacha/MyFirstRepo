package com.example.uramacha.feednews;

import java.io.Serializable;

/**
 * Created by URAMACHA on 2/8/2018.
 * Bean Class for FeedItem Row
 */

class FeedItem implements Serializable {

    private String title;
    private String description;
    private String imagehref;

    public FeedItem() {

    }

    public FeedItem(String title, String description, String imagehref) {
        this.title = title;
        this.description = description;
        this.imagehref = imagehref;
    }

    public FeedItem(String ti) {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagehref() {
        return imagehref;
    }

    public void setImagehref(String imagehref) {
        this.imagehref = imagehref;
    }
}
