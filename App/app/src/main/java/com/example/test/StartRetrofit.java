package com.example.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartRetrofit {
    public  static RetrofitAPI getAPI(){return getInstance().create(RetrofitAPI.class);}
    private static final String BASE_URL = "http://13.209.67.45:8080/";
    public static Retrofit getInstance(){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}