package com.example.qrcode.Databases;

public class DbScannedTickets {

    String ssno;
    String semail;
    String sdelegate;
    String sname;
    String sdiet;
    String sabs;
    String sschedule;

public DbScannedTickets(){

}
    public DbScannedTickets(String ssno, String semail, String sdelegate, String sname, String sdiet,  String sabs, String sschedule) {
        this.ssno = ssno;
        this.semail = semail;
        this.sdelegate = sdelegate;
        this.sname = sname;
        this.sdiet = sdiet;
        this.sabs = sabs;
        this.sschedule = sschedule;
    }

    public String getSsno() {
        return ssno;
    }

    public String getSemail() {
        return semail;
    }

    public String getSdelegate() {
        return sdelegate;
    }

    public String getSname() {
        return sname;
    }

    public String getSdiet() {
        return sdiet;
    }

    public String getSabs() {
        return sabs;
    }

    public String getSschedule(){return sschedule;}
}
