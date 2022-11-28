package com.example.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class kakaoLogin extends AppCompatActivity
{
    private ISessionCallback mSessionCallback;
    private Button btn_custom_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_login);
    /*----------------------------------------------------로그아웃 버튼------------------------------------------------------------*/
        btn_custom_logout = (Button) findViewById(R.id.btn_custom_logout);
        btn_custom_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Toast.makeText(kakaoLogin.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        /*----------------------------------------------------로그인, 액티비티 이동-----------------------------------------------------------*/
        mSessionCallback = new ISessionCallback()
        {
            @Override
            public void onSessionOpened()
            {
                // 로그인 요청
                UserManagement.getInstance().me(new MeV2ResponseCallback()
                {
                    @Override
                    public void onFailure(ErrorResult errorResult)
                    {
                        // 로그인 실패
                        Toast.makeText(kakaoLogin.this, "로그인 도중에 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult)
                    {
                        // 세션이 닫힘..
                        Toast.makeText(kakaoLogin.this, "세션이 닫혔습니다.. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(MeV2Response result)
                    {
                        // 로그인 성공
                        Intent intent = new Intent(kakaoLogin.this, MainActivity.class);
                        intent.putExtra("name", result.getKakaoAccount().getProfile().getNickname());
                        Long save=result.getId();
                        String userid=save.toString();
                        intent.putExtra("userid",userid);
                        GlobalApplication glo=(GlobalApplication) getApplication();
                        glo.setUsername(result.getKakaoAccount().getProfile().getNickname());
                        glo.setUserid(userid);
                        getCalendar(intent);

//                        Toast.makeText(MainActivity.this, "환영 합니다 !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            /*----------------------------------------------------------------------------------------------------------------*/
            @Override
            public void onSessionOpenFailed(KakaoException exception)
            {
                Toast.makeText(kakaoLogin.this, "onSessionOpenFailed", Toast.LENGTH_SHORT).show();
            }
        };
        Session.getCurrentSession().addCallback(mSessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();


//        getAppKeyHash();
    }

    private void getCalendar(Intent intent){
        Call<GetCalender> call;
        GlobalApplication glo=(GlobalApplication)getApplication();
        call=StartRetrofit.getAPI().getCalender("123");

        call.enqueue(new Callback<GetCalender>() {
            @Override
            public void onResponse(Call<GetCalender> call, Response<GetCalender> response) {
                if(response.isSuccessful()) {
                    GetCalender getcal = response.body();
                    HashMap<String,String>cal=getcal.getCalendar();
                    Set<String> keyset=cal.keySet();

        for(String key:keyset){
            Log.i("결과",key+" : "+cal.get(key));
        }
                    //intent.putExtra("calendar", getcal.getCalendar());

                    startActivity(intent);
                    finish();
                }
                else{

                    Log.d("error",""+response.code());
                }

            }

            @Override
            public void onFailure(Call<GetCalender> call, Throwable t) {
                Log.i("error",t.getMessage());
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mSessionCallback);
    }
}