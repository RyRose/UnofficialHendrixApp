package com.ryan.unofficialhendrixapp.models;

public class Staff {
    public String link;
    public String name;
    public String title;
    public String dept;
    public String phone;
    public String email;
    public String location_line_1;
    public String location_line_2;

    public Staff(String link, String name, String title, String dept, String phone, String email, String line1, String line2) {
        this.link = link;
        this.name = name;
        this.title = title;
        this.dept = dept.isEmpty() ? "Other" : dept;
        this.phone = phone;
        this.email = email;
        this.location_line_1 = line1;
        this.location_line_2 = line2;
    }

    public Staff(String[] attributes) {
        this.link = attributes[0];
        this.name = attributes[1];
        this.title = attributes[2];
        this.dept = attributes[3].isEmpty() ? "Other" : attributes[3];
        this.phone = attributes[4];
        this.email = attributes[5];
        this.location_line_1 = attributes[6];
        this.location_line_2 = attributes[7];
    }
}
