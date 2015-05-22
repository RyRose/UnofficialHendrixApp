package com.ryan.unofficialhendrixapp.models;

import com.ryan.unofficialhendrixapp.helpers.DateUtils;

import java.util.Date;

public class NewsEntry {
    public final String LOG_TAG = getClass().getSimpleName();

    public String title;
    public String link;
    public String description;
    public Date date;

    public NewsEntry(String title, String link, String description, String date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = DateUtils.convertToDate(date);
    }

    public NewsEntry(String title, String link, String description, long date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = DateUtils.convertToDate(date);
    }


    public NewsEntry(String [] attributes) {
        this.title = attributes[0];
        this.link = attributes[1];
        this.description = attributes[2];
        this.date = DateUtils.convertToDate(attributes[3]);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof NewsEntry &&
                title.equals(((NewsEntry) o).title) &&
                link.equals(((NewsEntry) o).link) &&
                description.equals(((NewsEntry) o).description) &&
                date.getTime() == ((NewsEntry) o).date.getTime();
    }

    @Override
    public int hashCode() {
        return title.hashCode() * link.hashCode() * description.hashCode() * date.hashCode();
    }
}
