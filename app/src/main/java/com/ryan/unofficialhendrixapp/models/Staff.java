package com.ryan.unofficialhendrixapp.models;

public class Staff {

    public String link;
    public String full_name;
    public String last_name;
    public String title;
    public String dept;
    public String phone;
    public String email;
    public String location_line_1;
    public String location_line_2;

    public Staff() {} // Empty bean constructor

    public Staff(String link, String name, String title, String dept, String phone, String email, String line1, String line2) {
        this.link = link;
        this.full_name = name;
        this.last_name = name.split(" ")[ name.split(" ").length - 1 ];
        this.title = title;
        this.dept = dept.isEmpty() ? "Other" : dept;
        this.phone = phone;
        this.email = email;
        this.location_line_1 = line1;
        this.location_line_2 = line2;
    }

    public Staff(String[] attributes) {
        this.link = attributes[0];
        this.full_name = attributes[1];
        this.last_name = attributes[1].split(" ")[ attributes[1].split(" ").length - 1 ];
        this.title = attributes[2];
        this.dept = attributes[3].isEmpty() ? "Other" : attributes[3];
        this.phone = attributes[4];
        this.email = attributes[5];
        this.location_line_1 = attributes[6];
        this.location_line_2 = attributes[7];
    }
}
