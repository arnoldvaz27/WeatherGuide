package com.arnold.weatherguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.splash_screen);

        SharedPreferences settingLayoutType = getSharedPreferences("Details", MODE_PRIVATE);
        String mobileNumber = settingLayoutType.getString("mobile number", null);
        String userRegistered = settingLayoutType.getString("user registered", null);
        String userLogout = settingLayoutType.getString("user logout", null);

        if(mobileNumber == null){
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        MobileRegister.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }else if(userRegistered == null || userRegistered.equals("0")){
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        Registration.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }else if(userLogout.equals("1")){
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        Logout.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }
        else{
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        HomeWeather.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }
    }
}