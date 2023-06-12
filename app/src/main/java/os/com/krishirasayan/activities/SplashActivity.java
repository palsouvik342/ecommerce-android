package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.showSingleSelectDialog;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import os.com.krishirasayan.R;

public class SplashActivity extends AppCompatActivity {

    Context context = this;
    private static int SPALASH_SCREEN = 100;
    Animation bottom_anim;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkForUpdates();
    }

    public void proceed() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottom_anim = AnimationUtils.loadAnimation(this, R.anim.left_anim);
        logo = findViewById(R.id.ivLogoSplash);
        logo.setAnimation(bottom_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, AuthActivity.class);
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(logo, "logo_image");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                startActivity(intent, options.toBundle());
                finish();

            }
        }, SPALASH_SCREEN);
    }

    @Override
    public void onBackPressed() {
    }

    public void checkForUpdates() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                showUpdateAlert();
            }
            else {
                proceed();
            }
        });
    }

    public void showUpdateAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("App Out of date");
        alertDialog.setMessage("Please update this App to continue using it");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
        alertDialog.show();
    }
}