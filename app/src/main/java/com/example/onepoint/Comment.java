package com.example.onepoint;

public class Comment {
    private String title;
    private int knowledgeImageId;
    private int userImageId;
    private String user;
    private  String comment;

    public Comment(String title,int knowledgeImageId,int userImageId,String user,String comment){
        this.title = title;
        this.knowledgeImageId = knowledgeImageId;
        this.userImageId = userImageId;
        this.user = user;
        this.comment = comment;
    }
    public String getTitle(){return title;}
    public int getKnowledgeImageId(){return knowledgeImageId;}
    public int getUserImageId(){return userImageId;}
    public String getUser(){return user;}
    public String getComment(){return comment;}
}
