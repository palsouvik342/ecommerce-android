package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.facebookSignOut;
import static os.com.krishirasayan.consts.Helper.googleSignOut;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_SIGNIN_FACEBOOK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

public class SignInFacebookActivity extends AppCompatActivity {

    Context context = this;
    CallbackManager callbackManager;
    LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_facebook);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                d("Facebook Login Success");
                handleSignInResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                w("Facebook Login Cancelled");
                facebookSignOut();
                openAndFinish(context, os.com.krishirasayan.activities.LoginActivity.class);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                e("Facebook Login Error");
                exception.printStackTrace();
                facebookSignOut();
                openAndFinish(context, os.com.krishirasayan.activities.LoginActivity.class);
            }
        });
        loginManager.logInWithReadPermissions(SignInFacebookActivity.this, Arrays.asList("email", "public_profile", "user_friends"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(AccessToken accessToken) {
        try {
            facebookSignIn(accessToken.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            facebookSignOut();
            openAndFinish(context, os.com.krishirasayan.activities.LoginActivity.class);
        }
    }

    private void facebookSignIn(String token) {
        new VolleyService(context).withParam("access_token", token).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                String token = response.optString("token");
                startActivity(new Intent(SignInFacebookActivity.this, os.com.krishirasayan.activities.AuthActivity.class).putExtra("token", token));
                finish();
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                googleSignOut(context);
                Toast.makeText(context, "Google sign-in failed because server rejected token", Toast.LENGTH_LONG).show();
                openAndFinish(context, os.com.krishirasayan.activities.LoginActivity.class);
            }
        }).post(API_SIGNIN_FACEBOOK);
    }
}