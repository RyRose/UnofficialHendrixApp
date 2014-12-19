package com.ryan.unofficialhendrixapp;

import java.util.ArrayList;

/**
 * Created by ryan on 12/18/14.
 */
public class Entry extends ArrayList {

    private String title;
    private String link;
    private String description;
    private String date;

    public Entry(String title, String link, String description, String date) {
        setTitle(title);
        setLink(link);
        setDescription(description);
        setDate(date);
    }

    public Entry() {
        setTitle(null);
        setLink(null);
        setDescription(null);
        setDate(null);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
