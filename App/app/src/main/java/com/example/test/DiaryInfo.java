package com.example.test;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class DiaryInfo {
    @SerializedName("firstEmo")
    private String firstEmo;
    @SerializedName("emotions")
    private HashMap<String, Integer> emotions;
    @SerializedName("comment")
    private String comment;

    public String getfirstEmo(){
        return firstEmo;
    }
    public String getComment(){
        return comment;
    }
    public  HashMap<String, Integer>getEmotions(){
        return  emotions;
    }

    private  String id;
    private int date;
    private String title;
    private String content;


    public DiaryInfo(String title, String content, String id, int date){
        this.title=title;
        this.content=content;
        this.id=id;
        this.date=date;

    }

}
