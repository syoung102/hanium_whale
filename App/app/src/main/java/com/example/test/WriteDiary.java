package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WriteDiary extends AppCompatActivity {

    private TextView tv_date;
    private Button btn_complete;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_comments;
    private String date;
    String title;
    String content;
    String comment;
    String Femo;
    HashMap<String,Integer> Emo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);

        tv_date=findViewById(R.id.tv_date);
        tv_title=findViewById(R.id.tv_title);
        tv_content=findViewById(R.id.tv_content);
        btn_complete=findViewById(R.id.btn_complete);
        tv_comments=findViewById(R.id.tv_comments);

        Intent intent=getIntent();

        date=intent.getStringExtra("date");
        GlobalApplication glo=(GlobalApplication)getApplication();
        glo.setDate(date);

        title=intent.getStringExtra("title");
        content=intent.getStringExtra("content");
        comment=intent.getStringExtra("comment");

       tv_title.setText(title);
       tv_content.setText(content);
       tv_date.setText(date);
       tv_comments.setText(comment);


        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createData();
            }
        });


    }

   private void createData(){
       Intent intent3=new Intent(WriteDiary.this,ResPopup.class);


        DiaryInfo diary=new DiaryInfo("하이하이","바이바이","12345678",20220812);
        Call<DiaryInfo> call;
        call=StartRetrofit.getAPI().SendDiary(diary);
        call.enqueue(new Callback<DiaryInfo>() {
            @Override
            public void onResponse(Call<DiaryInfo> call, Response<DiaryInfo> response) {
                if(!response.isSuccessful()){
                    Log.i("KAKAO_API", "오류발생"+response.code());

                }
                DiaryInfo diary=response.body();

                Femo=diary.getfirstEmo();
                Emo=diary.getEmotions();
                comment=diary.getComment();

                intent3.putExtra("FirstEmo",Femo);
                intent3.putExtra("Comment",comment);
                intent3.putExtra("Emotion",Emo);

                startActivity(intent3);//액티비티 이동
            }


            @Override
            public void onFailure(Call<DiaryInfo> call, Throwable t) {
                Log.i("KAKAO_API", "오류발생" +t.getMessage());
            }
        });

    }
}