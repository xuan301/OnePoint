package com.example.onepoint;

import android.os.Parcel;
import android.os.Parcelable;

public class Knowledge implements Parcelable {
    private String title;
    private String imageSrc;
    private String content;
    private  String author;
    private int id;
    private int like_num;

    Knowledge(String title, String imageSrc, String content, String author, int id, int like_num){
        this.title = title;
        this.imageSrc = imageSrc;
        this.content = content;
        this.author = author;
        this.id = id;
        this.like_num = like_num;
    }
    String getTitle(){
        return title;
    }
    String getImageSrc(){
        return imageSrc;
    }
    public String getContent(){ return content;}
    public String getAuthor(){ return author;}
    public int getId(){return id;}
    public int getLike_num(){return like_num;}



    //Parcel序列化所需函数
    private Knowledge(Parcel in) {
        title = in.readString();
        imageSrc = in.readString();
        content = in.readString();
        author = in.readString();
        id = in.readInt();
        like_num = in.readInt();
    }

    public static final Creator<Knowledge> CREATOR = new Creator<Knowledge>() {
        @Override
        public Knowledge createFromParcel(Parcel in) {
            return new Knowledge(in);
        }

        @Override
        public Knowledge[] newArray(int size) {
            return new Knowledge[size];
        }
    };

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageSrc);
        dest.writeString(content);
        dest.writeString(author);
        dest.writeInt(id);
        dest.writeInt(like_num);
    }


}
