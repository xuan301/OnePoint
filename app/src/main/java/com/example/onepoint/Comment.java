package com.example.onepoint;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable{
    private String title;
    private String knowledgeImagesrc;
    private String userImage;
    private String user;
    private  String comment;
    private  String content;
    private String author;
    private int knowid;
    private String time;
    private int commentID;

    public Comment(String title,String knowledgeImagesrc,String userImage,String user,String comment,String content,String author,int id,String time,int commentID){
        this.title = title;
        this.knowledgeImagesrc = knowledgeImagesrc;
        this.userImage = userImage;
        this.user = user;
        this.comment = comment;
        this.content = content;
        this.author = author;
        this.knowid = id;
        this.time = time;
        this.commentID = commentID;
    }
    public String getTitle(){return title;}
    public String getKnowledgeImagesrc(){return knowledgeImagesrc;}
    public String getUserImage(){return userImage;}
    public String getUser(){return user;}
    public String getComment(){return comment;}
    public String getContent(){return content;}
    public String getAuthor(){return author;}
    public int getKnowid(){return knowid;}
    public String getTime(){return time;}
    public int getCommentID(){return commentID;}
    //Parcel序列化所需函数
    private Comment(Parcel in) {//作用是？？？
        //title = in.readString();
        //knowledgeImagesrc = in.readString();
        //content = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(title);
        //dest.writeString(knowledgeImagesrc);
        //dest.writeString(content);
    }
}
