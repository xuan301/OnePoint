package com.example.onepoint;

public class Knowledge {
    private String title;
    private String imageSrc;
    private  String content;

    public Knowledge(String title, String imageSrc, String content){
        this.title = title;
        this.imageSrc = imageSrc;
        this.content = content;
    }

    public String getTitle(){
        return title;
    }
    public String getImageSrc(){
        return imageSrc;
    }
    public String getContent(){ return content;}

}
