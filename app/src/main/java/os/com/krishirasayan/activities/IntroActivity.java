package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.openAndFinish;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import os.com.krishirasayan.onboarding.OnboardingActivity;
import os.com.krishirasayan.R;

public class IntroActivity extends AppCompatActivity {
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //startActivity(new Intent(this, OnboardingActivity.class));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        Boolean firstStart = sharedPreferences.getBoolean("firstStartBoarding", true);
        if (firstStart) {
            firstRun();
        }else {
            //firstRun();
            openAndFinish(context, SplashActivity.class);
            //startActivity(new Intent(context, SplashActivity.class));
        }
    }

    public void firstRun() {
        Intent intent = new Intent(context, OnboardingActivity.class);
        startActivity(intent);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStartBoarding", false);
        editor.apply();
    }
}