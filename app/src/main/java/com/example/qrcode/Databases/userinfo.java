package com.example.qrcode.Databases;

public class userinfo {

    String pid;
    String email;
    String firstname;
    String lastname;
    String mnumber;
    String occupation;
    String workplace;


    public userinfo(){

    }

    public userinfo(String pid, String email, String firstname, String lastname, String mnumber, String occupation, String workplace) {
        this.pid = pid;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mnumber = mnumber;
        this.occupation = occupation;
        this.workplace = workplace;
    }

    public String getPid() {
        return pid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMnumber() {
        return mnumber;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getWorkplace() {
        return workplace;
    }
}


