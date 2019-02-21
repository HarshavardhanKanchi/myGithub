package com.example.rgukt.myproject;

public class President {
    String pState;
    String pDistrict;
    String pMandal;
    String pPanchayat;
    String pVillage;
    String pUsername;
    String pPassword;
    String pEmail;
    String pMobile;
    String pId;
    String pProfile;
    public President(){

    }

    public President(String pId,String pState,String pDistrict, String pMandal, String pPanchayat, String pVillage, String pUsername, String pPassword, String pEmail, String pMobile,String pProfile) {
        this.pDistrict = pDistrict;
        this.pState=pState;
        this.pId=pId;
        this.pMandal = pMandal;
        this.pPanchayat = pPanchayat;
        this.pVillage = pVillage;
        this.pUsername = pUsername;
        this.pPassword = pPassword;
        this.pEmail = pEmail;
        this.pMobile = pMobile;
        this.pProfile=pProfile;
    }

    public String getpState() {
        return pState;
    }

    public String getpDistrict() {
        return pDistrict;
    }

    public String getpMandal() {
        return pMandal;
    }

    public String getpPanchayat() {
        return pPanchayat;
    }

    public String getpVillage() {
        return pVillage;
    }

    public String getpUsername() {
        return pUsername;
    }

    public String getpPassword() {
        return pPassword;
    }

    public String getpEmail() {
        return pEmail;
    }

    public String getpMobile() {
        return pMobile;
    }

    public String getpId() {
        return pId;
    }

    public String getpProfile() {
        return pProfile;
    }
}
