package com.example.rgukt.myproject;

import java.util.Arrays;

public class User {
    String uId;
    String uName;
    String uEmail;
    String uPassword;
    String uMobile;
    String uState;
    String uDistrict;
    String uMandal;
    String uPanchayt;
    String uVillage;
    String uProfile;

    public User(){

    }

    public User(String uId,String uName, String uEmail, String uPassword, String uMobile, String uState, String uDistrict, String uMandal, String uPanchayt, String uVillage,String uProfile) {
        this.uId=uId;
        this.uProfile=uProfile;
        this.uName = uName;
        this.uEmail = uEmail;
        this.uPassword = uPassword;
        this.uMobile = uMobile;
        this.uState = uState;
        this.uDistrict = uDistrict;
        this.uMandal = uMandal;
        this.uPanchayt = uPanchayt;
        this.uVillage = uVillage;
    }

    public String getuProfile() {
        return uProfile;
    }

    public String getuId() {
        return uId;
    }

    public String getuName() {
        return uName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuPassword() {
        return uPassword;
    }

    public String getuMobile() {
        return uMobile;
    }

    public String getuState() {
        return uState;
    }

    public String getuDistrict() {
        return uDistrict;
    }

    public String getuMandal() {
        return uMandal;
    }

    public String getuPanchayt() {
        return uPanchayt;
    }

    public String getuVillage() {
        return uVillage;
    }
}
