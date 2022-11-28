package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashAcativity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acativity);
        Handler hd=new Handler();

        hd.postDelayed(new SplashHandlerForAlreadyLogin(), 3000);
    }

    private class SplashHandlerForAlreadyLogin implements  Runnable{
        public void run(){
            startActivity(new Intent(getApplication(),kakaoLogin.class));
            SplashAcativity.this.finish();
        }
    }
}