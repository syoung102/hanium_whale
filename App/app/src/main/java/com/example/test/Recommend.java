package com.example.test;

import android.renderscript.ScriptIntrinsic;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Recommend {
    @SerializedName("movie")
    private HashMap<Integer,String> movie;
    public  HashMap<Integer, String> getMovie(){
        return  movie;
    }


    private HashMap<Integer,String >song;
    public HashMap<Integer, String > getSong(){
        return song;
    }
}
