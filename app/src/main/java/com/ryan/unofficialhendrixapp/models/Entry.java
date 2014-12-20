package com.ryan.unofficialhendrixapp.models;

/**
 * Created by ryan on 12/18/14.
 */
public class Entry {

    private String title;
    private String link;
    private String description;
    private String date;

    public Entry(String title, String link, String description, String date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
