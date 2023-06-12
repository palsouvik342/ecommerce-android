package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.saveFcmToken;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;
import static os.com.krishirasayan.consts.Url.API_READ_PROFILE;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AccountModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    Context context = this;
    String token, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FirebaseApp.initializeApp(context);
        fcmPrep();
    }

    private void fcmPrep() {
        // Try and get existing FCM token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                try {

                    if (!task.isSuccessful()) {
                        w("Fetching FCM registration token failed", task.getException());
                        w( "No FCM Token Exists ! Try clearing data and restarting / reinstalling App to generate a new one.");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    // Log and toast
                    w( "Current FCM token: " + token);
                    saveFcmToken(context, token);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    setup();
                }
            }
        });
    }

    private void setup() {
        context = this;

        if (getIntent().hasExtra("token") && !TextUtils.isEmpty(getIntent().getStringExtra("token"))) {
            token = getIntent().getStringExtra("token");
            i("New token provided: " + token);
            i("Validating Token: " + token);
            validateToken();
        } else if (UserData.hasToken(context)) {
            token = UserData.getToken(context);
            i("Saved token found: " + token);
            i("Validating Token: " + token);
            validateToken();
        } else {
            w("Authentication failed because token missing");
            openAndFinish(context, UserChooseActivity.class);
        }
    }

    private void validateToken() {
        new VolleyService(context).withParam("token", token).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                AccountModel accountModel = new AccountModel();
                accountModel.setToken(token);
                accountModel.setId(response.optString("id"));
                accountModel.setName(response.optString("name"));
                accountModel.setEmail(response.optString("email"));
                accountModel.setAvatar(response.optString("avatar_url"));
                accountModel.setVerified(response.optInt("verified") > 0);
                //accountModel.setPhotoUrl(response.optString("avatar_url"));
                accountModel.setLocale("en");
                accountModel.setCurrency("inr");
                accountModel.setTimezone("Asia/Kolkata");
                accountModel.setAddress("");
                accountModel.setUserType(response.optString("user_type"));
                accountModel.setRoleId(response.optString("role_id"));
                accountModel.setRewardPoints(response.optString("reward_points"));
                accountModel.setDistrictCode(response.optString("district_code"));
                UserData.save(context, accountModel);
                storeCartData();
                storeRetailerBuyProductCartData();
                if(accountModel.getRoleId().equals("4")){
                }
                boolean needsVerification = false;
                if(accountModel.getRoleId().equals("4") || accountModel.getRoleId().equals("5")) {
                    if(!accountModel.isVerified()) {
                        needsVerification = true;
                    }
                }
                if(needsVerification) {
                    openAndFinish(context, MyProfileActivity.class);
                }
                else {
                    openAndFinish(context, MainActivity.class);
                }
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                w("Authentication failed because token invalid");
                openAndFinish(context, UserChooseActivity.class);
            }
        }).get(API_READ_PROFILE);
    }

    private void storeCartData() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        UserData.setCart(context, response);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                })
                .get(API_CUSTOMER_CART);
    }

    private void storeRetailerBuyProductCartData() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        UserData.setRetailerCart(context, response);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                })
                .get(API_RETAILER_CART);
    }
}