package com.example.onepoint.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SecondLevelBean implements Parcelable {
    //二级评论id
    //private String id;
    //二级评论头像
    private String headImg;
    //二级评论的用户名
    private String userName;
    //评论内容（回复内容）
    private String content;
    //点赞数量
    private long likeCount;
    //是否点赞了  0没有 1点赞
    private int isLike;

    public SecondLevelBean(/*String id,*/String headImg,String userName,/*String userId,*/String content,long likeCount,int isLike/*,long totalCount,int position,int positionCount*/){
        //this.id=id;
        this.headImg =headImg;
        this.userName = userName;
        //this.userId  =userId;
        this.content = content;
        this.likeCount = likeCount;
        this.isLike  =isLike;
        //this.totalCount = totalCount;
        //this.position = position;
        //this.positionCount = positionCount;
    }

    /*public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }*/
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    protected SecondLevelBean(Parcel in) {

    }

    public static final Creator<SecondLevelBean> CREATOR = new Creator<SecondLevelBean>() {
        @Override
        public SecondLevelBean createFromParcel(Parcel in) {
            return new SecondLevelBean(in);
        }

        @Override
        public SecondLevelBean[] newArray(int size) {
            return new SecondLevelBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

}
