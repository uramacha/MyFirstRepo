package com.example.uramacha.feednews.adapter;

import java.io.Serializable;

/**
 *
 * Bean Class for FeedItem Row
 */

public class FeedItem implements Serializable {

    private final String title;
    private final String description;
    private final String imagehref;

    public FeedItem(String title, String description, String imagehref) {
        this.title = title;
        this.description = description;
        this.imagehref = imagehref;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImagehref() {
        return imagehref;
    }

}
