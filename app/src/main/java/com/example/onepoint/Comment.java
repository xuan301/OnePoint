package com.example.onepoint;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable{
    private String title;
    private String knowledgeImagesrc;
    private int userImageId;
    private String user;
    private  String comment;
    private  String content;

    public Comment(String title,String knowledgeImagesrc,int userImageId,String user,String comment,String content){
        this.title = title;
        this.knowledgeImagesrc = knowledgeImagesrc;
        this.userImageId = userImageId;
        this.user = user;
        this.comment = comment;
        this.content = content;
    }
    public String getTitle(){return title;}
    public String getKnowledgeImagesrc(){return knowledgeImagesrc;}
    public int getUserImageId(){return userImageId;}
    public String getUser(){return user;}
    public String getComment(){return comment;}
    public String getContent(){return content;}
    //Parcel序列化所需函数
    private Comment(Parcel in) {
        title = in.readString();
        knowledgeImagesrc = in.readString();
        content = in.readString();
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
        dest.writeString(title);
        dest.writeString(knowledgeImagesrc);
        dest.writeString(content);
    }
}
