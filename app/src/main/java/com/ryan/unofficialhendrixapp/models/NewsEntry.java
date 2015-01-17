package com.ryan.unofficialhendrixapp.models;

/**
 *
 * Data model used to hold each entry of the listViewNews
 *
 */
public class NewsEntry {

    private String title;
    private String link;
    private String description;
    private String date;

    public NewsEntry(String title, String link, String description, String date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date.substring(0, 16);
    }

    public NewsEntry(String [] attributes) {
        this.title = attributes[0];
        this.link = attributes[1];
        this.description = attributes[2];
        this.date = attributes[3].substring(0, 16);
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
