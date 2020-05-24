package com.example.onepoint;

public class Knowledge {
    private String title;
    private String imageSrc;

    public Knowledge(String title, String imageSrc){
        this.title = title;
        this.imageSrc = imageSrc;
    }

    public String getTitle(){
        return title;
    }
    public String getImageSrc(){
        return imageSrc;
    }

}
