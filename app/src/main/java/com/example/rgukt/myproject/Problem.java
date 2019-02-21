package com.example.rgukt.myproject;

public class Problem {
    String problemId,title,description,image,username,village,status,date;



    public Problem(){

    }

    public Problem(String problemId,String title, String description, String image,String username,String village,String  status,String date) {
        this.title = title;
        this.date=date;
        this.description = description;
        this.image = image;
        this.username=username;
        this.village=village;
        this.status=status;
        this.problemId=problemId;
    }

    public String getDate() {
        return date;
    }

    public String getUsername() { return username; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getVillage() {
        return village;
    }

    public String getStatus() {
        return status;
    }

    public String getProblemId() {
        return problemId;
    }
}
