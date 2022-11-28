package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private Button btn_MoveWrite;
    private DatePicker dp;
    private TextView tv;
    private TextView tv_getid;
    String date;
    String title;
    String content;
    String comment;
    HashMap<String,String>calendar;

         /*----------------------------------------------------날짜 설정------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=getIntent();

        dp=findViewById(R.id.date_picker);
        int year=2022;
        int month=7-1;
        int day=15;
        dp.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = year + "/" + (monthOfYear+1) + "/" + dayOfMonth;
                Toast.makeText(MainActivity.this, date, Toast.LENGTH_SHORT).show();
            }
        });
        /*---------------------------------------------------(이전 액티비티)카카오톡에서 넘겨받은 정보-------------------------------------------------------------*/

        tv=findViewById(R.id.test);
        tv_getid=findViewById(R.id.getid);
        String name=intent.getStringExtra("name");
        String userid=intent.getStringExtra("userid");
        tv.setText(name);
        tv_getid.setText(userid);
        /*calendar=(HashMap<String, String>) intent.getSerializableExtra("calendar");
        Set<String> keyset=calendar.keySet();

        for(String key:keyset){
            Log.i("결과",key+" : "+calendar.get(key));
        }*/

        /*------------------------------------------------날짜 넘겨주면서 액티비티 전환----------------------------------------------------------------*/
        btn_MoveWrite=findViewById(R.id.btn_MoveWrite);                 /*일기 작성하러가기 버튼을 눌렀을 때 */
        btn_MoveWrite.setOnClickListener(new View.OnClickListener() {   /*작동할 코드들*/
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(MainActivity.this,WriteDiary.class);
                Log.i("KAKAO_API", " 됐어용" +date );
                /*-------------------------------------------------------Retrofit 통신---------------------------------------------------------*/

                Call<GetDiary> call;
                call=StartRetrofit.getAPI().getDiary("1","1");
                call.enqueue(new Callback<GetDiary>() {
                    @Override
                    public void onResponse(Call<GetDiary> call, Response<GetDiary> response) {
                        if(response.isSuccessful()){
                        GetDiary result=response.body();
                        title=result.getTitle();
                        comment=result.getComment();
                        content=result.getContent();
                            intent2.putExtra("date",date);
                            intent2.putExtra("title",title);
                            intent2.putExtra("comment",comment);
                            intent2.putExtra("content",content);
                            Log.i("KAKAO_API", comment  );
                            startActivity(intent2);//액티비티 이동
                        }
                        else{
                            Log.i("KAKAO_API", " 안됐어용"  );
                        }
                    }

                    @Override
                    public void onFailure(Call<GetDiary> call, Throwable t) {
                        Log.i("KAKAO_API", " 안됐어용"+t.getMessage()  );
                    }
                });
            }
        });
    }
}