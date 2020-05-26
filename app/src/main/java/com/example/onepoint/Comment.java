package com.example.onepoint;

public class Comment {
    private String title;
    private String knowledgeImagesrc;
    private int userImageId;
    private String user;
    private  String comment;

    public Comment(String title,String knowledgeImagesrc,int userImageId,String user,String comment){
        this.title = title;
        this.knowledgeImagesrc = knowledgeImagesrc;
        this.userImageId = userImageId;
        this.user = user;
        this.comment = comment;
    }
    public String getTitle(){return title;}
    public String getKnowledgeImagesrc(){return knowledgeImagesrc;}
    public int getUserImageId(){return userImageId;}
    public String getUser(){return user;}
    public String getComment(){return comment;}
}
