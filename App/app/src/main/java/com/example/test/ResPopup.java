package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResPopup extends Activity/*AppCompatActivity를 Activity로 바꾸었더니 해결된 오류 있음*/
{
    private Button btn_contents;
    private TextView tv_comments;
    private TextView tv_getid;
    private String Femo;
    private String comment;
    private HashMap<String,Integer> Emos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_res_popup);
        int happy=0;
        int sad=0;
        int angry=0;
        int anxiety=0;
        int blunted=0;
        int annoy=0;

        Intent intent=getIntent();
        Emos=(HashMap<String, Integer>) intent.getSerializableExtra("Emotion");
        Femo=intent.getStringExtra("FirstEmo");
        comment=intent.getStringExtra("Comment");
        GlobalApplication glo=(GlobalApplication)getApplication();
        tv_comments=findViewById(R.id.tv_comments);
        tv_comments.setText(comment);
        tv_getid=findViewById(R.id.tv_getid);
        tv_getid.setText(glo.getUsername()+"님의 감정은 "+Femo+"입니다.");
        Set<String> KEY=Emos.keySet();
        for (String key :KEY){
            if(key.equals("행복"))happy=Emos.get(key);
            if(key.equals("분노"))sad=Emos.get(key);
            if(key.equals("짜증"))annoy=Emos.get(key);
            if(key.equals("분노"))angry=Emos.get(key);
            if(key.equals("중립"))blunted=Emos.get(key);
            if(key.equals("불안"))anxiety=Emos.get(key);
        }

        PieChart pieChart = findViewById(R.id.pieChart);
        //샘플데이터
        ArrayList<PieEntry> visitors = new ArrayList<>();

        // 분석결과에서 받아온 값 대입하기.
        if (happy>0) {
            visitors.add(new PieEntry(happy, "행복"));
        }
        if (sad>0) {
            visitors.add(new PieEntry(sad, "슬픔"));
        }
        if (angry>0) {
            visitors.add(new PieEntry(angry , "분노"));
        }
        if (anxiety>0) {
            visitors.add(new PieEntry(anxiety   , "불안"));
        }
        if (blunted>0) {
            visitors.add(new PieEntry(blunted, "무감정"));
        }
        if (annoy>0) {
            visitors.add(new PieEntry(annoy, "짜증"));
        }


        PieDataSet pieDataSet = new PieDataSet(visitors, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("당신의 감정은?");
        pieChart.animate();




        btn_contents=findViewById(R.id.btn_contents);
        btn_contents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRes();
                //finish();
            }
        });
    }

    private void getRes(){
        GlobalApplication glo=(GlobalApplication)getApplication();
        Call<Recommend> call;
        Intent intent3=new Intent(ResPopup.this, ShowContents.class);
        call=StartRetrofit.getAPI().GetContent(glo.getUsername(),glo.getDate());

        call.enqueue(new Callback<Recommend>() {
            @Override
            public void onResponse(Call<Recommend> call, Response<Recommend> response) {
                if(response.isSuccessful()){
                Recommend result=response.body();
                //intent3.putExtra("res",result.getRes());
                startActivity(intent3);}
                else{
                    Log.d("error",""+response.code());
                }
            }

            @Override
            public void onFailure(Call<Recommend> call, Throwable t) {

            }
        });
    }

}