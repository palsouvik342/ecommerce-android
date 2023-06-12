package os.com.krishirasayan.activities;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Url.API_LOGIN;
import static os.com.krishirasayan.consts.Url.API_LOGIN_OTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

public class LoginActivity extends AppCompatActivity {

    Context context = this;
    EditText etLoginMobileNumber;
    MaterialSpinner spinner;
    TextView tvUserType;
    Button btLoginGetOtp;
    String TAG,USER_ROLE,USER_ROLE_TITLE, ROLETYPE, ROLETYPE_TITLE;
    Integer STATE_ID;
    AwesomeValidation loginValidation;
    TextView tvLoginBottomTermsText, tvLoginPolicyText;
    String selectedLocale = "",customerString,retailerString,distributorString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        if(!getIntent().hasExtra("STATE_ID")) {
            onBackPressed();
        }
        STATE_ID = getIntent().getIntExtra("STATE_ID", 0);
        w(STATE_ID);

        bindReferences();
    }

    private void bindReferences() {
        tvUserType = findViewById(R.id.tvUserType);
        tvUserType.setText(USER_ROLE_TITLE);
        etLoginMobileNumber = findViewById(R.id.etLoginMobileNumber);
        loginValidation = new AwesomeValidation(BASIC);
        loginValidation.addValidation((Activity) context, R.id.etLoginMobileNumber, RegexTemplate.NOT_EMPTY, R.string.require);
        loginValidation.addValidation((Activity) context, R.id.etLoginMobileNumber, Patterns.PHONE, R.string.enter_mobile_number);

        btLoginGetOtp = findViewById(R.id.btLoginGetOtp);
        btLoginGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginValidation.validate()) {
                    sendOtp();
                }
            }
        });
        //GetNumber();

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


        ROLETYPE = "customer";

        customerString = getResources().getString(R.string.customer);
        retailerString = getResources().getString(R.string.retailer);
        distributorString = getResources().getString(R.string.distributor);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        spinner.setItems(customerString, retailerString, distributorString);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                w(position);
                switch(position)
                {
                    case 0:
                        ROLETYPE = "customer";
                        chnageTitle();
                        break;
                    case 1:
                        ROLETYPE = "retailer";
                        chnageTitle();
                        break;
                    case 2:
                        ROLETYPE = "distributor";
                        chnageTitle();
                        break;
                }
                Snackbar.make(view, "Clicked " + ROLETYPE, Snackbar.LENGTH_LONG).show();
            }
        });

    }


   // Function will run after click to button
   /* public void GetNumber() {

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission check

            // Create obj of TelephonyManager and ask for current telephone service
            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getLine1Number();
            try {
                w("Users",phoneNumber);
                String loginPhoneNumber = phoneNumber.replace("+91", "");;
                w("Users",loginPhoneNumber);
                etLoginMobileNumber.setText(loginPhoneNumber);
            }
            catch (Exception e) {
                e("Could not set phone number");
            }
            return;
        } else {
            // Ask for permission
            requestPermission();
        }
    }
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String phoneNumber = telephonyManager.getLine1Number();
                w(phoneNumber);
                String loginPhoneNumber = phoneNumber.replace("91", "");;
                w(loginPhoneNumber);
                etLoginMobileNumber.setText(loginPhoneNumber);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }*/

    public void chnageTitle() {
        switch (ROLETYPE){
            case "customer":
                ROLETYPE_TITLE =  customerString;
                break;
            case "retailer":
                ROLETYPE_TITLE =  retailerString;
                break;
            case "distributor":
                ROLETYPE_TITLE =  distributorString;
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void sendOtp() {
        new VolleyService(context)
                .withParam("phone",  etLoginMobileNumber.getText().toString())
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        popupInfo(context, R.string.please_wait);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Intent intent =  new Intent(context, OtpSubmitActivity.class);
                        intent.putExtra("USER_ROLE", ROLETYPE);
                        intent.putExtra("USER_ROLE_TITLE", ROLETYPE_TITLE);
                        intent.putExtra("MOBILE_NUMBER", etLoginMobileNumber.getText().toString());
                        intent.putExtra("STATE_ID", STATE_ID);
                        startActivity(intent);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        //popupError(context, responseData);
                        String json = responseData;
                        try {
                            JSONObject obj = new JSONObject(json);
                            w("TAG", obj.optString("errors"));
                            popupError(context, obj.optString("message"));
                        } catch (Throwable t) {
                            w("TAG", "Could not parse malformed JSON: \"" + json + "\"");
                        }
                    }
                }).post(API_LOGIN_OTP);
    }

}