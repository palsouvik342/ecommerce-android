package os.com.krishirasayan.onboarding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.SplashActivity;


public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private os.com.krishirasayan.onboarding.OnboardingAdapter onboardingAdapter;
    Context context=this;
    private static int  start_next_slide = 4000;
    final Handler handler = new Handler();
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        makeStatusbarTransparent();

        viewPager = findViewById(R.id.onboarding_view_pager);

        onboardingAdapter = new os.com.krishirasayan.onboarding.OnboardingAdapter(context);
        viewPager.setAdapter(onboardingAdapter);
        viewPager.setPageTransformer(false, new os.com.krishirasayan.onboarding.OnboardingPageTransformer());



        final Runnable runnable = new Runnable() {
            public void run() {
                // need to do tasks on the UI thread
                Log.d("TAG", "Run test count: " + count);
                if (viewPager.getCurrentItem() < onboardingAdapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + count, true);
                }
                if (count++ < 2) {
                    handler.postDelayed(this, start_next_slide);
                }
            }
        };

        // trigger first time
        handler.post(runnable);


    }


    // Listener for next button press
    public void nextPage(View view) {
        if (view.getId() == R.id.button2) {
            if (viewPager.getCurrentItem() < onboardingAdapter.getCount() - 1) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
            }
        }
    }

    public void prevPage(View view) {
        if (view.getId() == R.id.button3) {
            Log.d("TAG", "prevPage: test -- " + onboardingAdapter.getCount() +" -- "+viewPager.getCurrentItem());
            if (viewPager.getCurrentItem() < onboardingAdapter.getCount()) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
            }
        }
    }
    public void finishPage(View view) {
        if (view.getId() == R.id.button4) {
            startActivity(new Intent(context, SplashActivity.class));
        }
    }

    private void makeStatusbarTransparent() {

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
}
