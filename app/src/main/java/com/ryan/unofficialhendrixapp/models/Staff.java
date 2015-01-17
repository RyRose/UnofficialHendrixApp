package com.ryan.unofficialhendrixapp.models;

public class Staff {
    private String link;
    private String name;
    private String title;
    private String dept;
    private String phone;
    private String email;
    private String loc_line_1;
    private String loc_line_2;

    public Staff(String link, String name, String title, String dept, String phone, String email, String line1, String line2) {
        this.link = link;
        this.name = name;
        this.title = title;
        this.dept = dept.isEmpty() ? "Other" : dept;
        this.phone = phone;
        this.email = email;
        this.loc_line_1 = line1;
        this.loc_line_2 = line2;
    }

    public Staff(String[] attributes) {
        this.link = attributes[0];
        this.name = attributes[1];
        this.title = attributes[2];
        this.dept = attributes[3].isEmpty() ? "Other" : attributes[3];
        this.phone = attributes[4];
        this.email = attributes[5];
        this.loc_line_1 = attributes[6];
        this.loc_line_2 = attributes[7];
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDept() {
        return dept;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getline1() {
        return loc_line_1;
    }

    public String getline2() {
        return loc_line_2;
    }
}
