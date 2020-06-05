package com.example.onepoint.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class FirstLevelBean implements Parcelable {
    //一级评论id
    private String id;
    //一级评论头像
    private String headImg;
    //一级评论的用户名
    private String userName;
    //一级评论的用户id
    private String userId;
    //评论内容
    private String content;
    //创建时间
    private long createTime;
    //点赞数量
    private long likeCount;
    //是否点赞了  0没有 1点赞
    private int isLike;
    //当前评论的总条数（包括这条一级评论）ps:处于未使用状态
    private long totalCount;
    //当前一级评论的位置（下标）
    private int position;
    //当前一级评论所在的位置(下标)
    private int positionCount;
    public FirstLevelBean(String id,String headImg,String userName,String userId,String content,long createTime,long likeCount,int isLike,long totalCount,int position,int positionCount){
        this.id=id;
        this.headImg =headImg;
        this.userName = userName;
        this.userId  =userId;
        this.content = content;
        this.createTime = createTime;
        this.likeCount = likeCount;
        this.isLike  =isLike;
        this.totalCount = totalCount;
        this.position = position;
        this.positionCount = positionCount;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    protected FirstLevelBean(Parcel in) {

    }

    public static final Creator<FirstLevelBean> CREATOR = new Creator<FirstLevelBean>() {
        @Override
        public FirstLevelBean createFromParcel(Parcel in) {
            return new FirstLevelBean(in);
        }

        @Override
        public FirstLevelBean[] newArray(int size) {
            return new FirstLevelBean[size];
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
