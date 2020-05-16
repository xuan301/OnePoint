package com.example.onepoint;

public class Knowledge {
    private String title;
    private int imageId;

    public Knowledge(String title, int imageId){
        this.title = title;
        this.imageId = imageId;
    }

    public String getTitle(){
        return title;
    }
    public int getImageId(){
        return imageId;
    }

}
