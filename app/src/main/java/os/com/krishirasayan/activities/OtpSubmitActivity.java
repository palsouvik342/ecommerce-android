package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_LOGIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.AppData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

public class OtpSubmitActivity extends AppCompatActivity {

    Context context = this;
    OtpTextView etLoginOtpCode;
    TextView tvUserType;
    Button btVerifyGetOtp;
    String TAG,USER_ROLE,MOBILE_NUMBER,USER_ROLE_TITLE, STATE_ID;
    String otpNumber = "";
    TextView tvLoginBottomTermsText, tvLoginPolicyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);

        USER_ROLE = getIntent().getStringExtra("USER_ROLE");
        USER_ROLE_TITLE = getIntent().getStringExtra("USER_ROLE_TITLE");
        MOBILE_NUMBER = getIntent().getStringExtra("MOBILE_NUMBER");
        STATE_ID = String.valueOf(getIntent().getIntExtra("STATE_ID",0));

        bindReferences();
    }

    private void bindReferences() {
        tvUserType = findViewById(R.id.tvUserType);
        tvUserType.setText(USER_ROLE_TITLE + " ("+  MOBILE_NUMBER +") ");
        etLoginOtpCode = findViewById(R.id.etLoginOtpCode);


        etLoginOtpCode.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                otpNumber = otp;
                login(otp);
                //signUpWithOtp(otp);
            }
        });

        btVerifyGetOtp = findViewById(R.id.btVerifyGetOtp);
        btVerifyGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!otpNumber.isEmpty()) {
                    login(otpNumber);
                }else{
                    popupError(context,"Please Enter Correct otp");
                }
            }
        });

        tvLoginBottomTermsText = findViewById(R.id.tvLoginBottomTermsText);
        tvLoginBottomTermsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context,TermsConditionActivity.class);
                startActivity(intent);
            }});

        tvLoginPolicyText = findViewById(R.id.tvLoginPolicyText);
        tvLoginPolicyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context,TermsConditionActivity.class);
                startActivity(intent);
            }});
    }


    private void login(String otp) {
        new VolleyService(context)
                .withParam("phone",  MOBILE_NUMBER)
                .withParam("otp", otp)
                .withParam("type", USER_ROLE)
                .withParam("fcm_token", AppData.getFcmToken(context))
                .withParam("state_id", STATE_ID)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        popupInfo(context, R.string.please_wait);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        String token = response.optString("token");
                        startActivity(new Intent(OtpSubmitActivity.this, AuthActivity.class).putExtra("token", token));
                        finish();
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        if(statusCode != null) {
                            popupError(context, R.string.login_failed);
                        }
                        else {
                            popupError(context, R.string.no_connection);
                        }
                    }
                }).post(API_LOGIN);
    }
}