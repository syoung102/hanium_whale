package com.example.test;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class GetCalender {

    @SerializedName("calender")
    private HashMap<String,String>calender;

    HashMap<String,String> getCalendar(){
        return calender;
    }
}
