package com.example.test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("loadDiary")
    Call<GetDiary>getDiary(
            @Query("id") String id,
            @Query("date") String date
    );

    @GET("loadCalender")
    Call<GetCalender>getCalender(
            @Query("id")String id
            );

    @POST("predictEmo")
    Call<DiaryInfo> SendDiary(@Body DiaryInfo Diary);

    @GET("recommend")
    Call<Recommend>GetContent(
            @Query("userid")String userid,
            @Query("date")String date
    );
}
