package com.example.test;

import com.google.gson.annotations.SerializedName;

public class GetDiary {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("emotion")
    private String emotion;

    @SerializedName("comment")
    private String comment;

    String getTitle(){return title;}
    String getContent(){return content;}
    String getEmotion(){return emotion;}
    String getComment(){return comment;}


}
