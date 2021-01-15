package com.example.qrcode.Databases;

public class DbTicketInfo {

    String sno;
    String name;
    String delegate;
    String email;
    String diet;
    String abs;
    String schedule;

 public DbTicketInfo(){

 }

    public DbTicketInfo(String sno, String name, String delegate, String email, String diet,  String abs, String schedule) {
        this.sno = sno;
        this.name = name;
        this.delegate = delegate;
        this.email = email;
        this.diet = diet;
        this.abs = abs;
        this.schedule = schedule;

    }

    public String getSno() {
        return sno;
    }

    public String getName() {
        return name;
    }

    public String getDelegate() {
        return delegate;
    }

    public String getEmail() {
        return email;
    }

    public String getDiet(){return diet;}

    public String getAbs(){return abs;}

    public String getSchedule(){return schedule;}
}
