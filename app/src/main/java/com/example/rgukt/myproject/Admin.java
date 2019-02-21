package com.example.rgukt.myproject;

public class Admin {
    String aId;
    String aUsername;
    String aPassword;
    String aEmail;
    String aMobile;
    public Admin(){

    }

    public Admin(String aId,String aUsername, String aPassword, String aEmail, String aMobile) {
        this.aId=aId;
        this.aUsername = aUsername;
        this.aPassword = aPassword;
        this.aEmail = aEmail;
        this.aMobile = aMobile;
    }

    public String getaId() {
        return aId;
    }

    public String getaUsername() {
        return aUsername;
    }

    public String getaPassword() {
        return aPassword;
    }

    public String getaEmail() {
        return aEmail;
    }

    public String getaMobile() {
        return aMobile;
    }
}
