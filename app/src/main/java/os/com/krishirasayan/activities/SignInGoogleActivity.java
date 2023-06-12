package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.googleSignInClient;
import static os.com.krishirasayan.consts.Helper.googleSignOut;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Url.API_SIGNIN_GOOGLE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignInGoogleActivity extends AppCompatActivity {

    Context context = this;

    final int RC_SIGN_IN = 999;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_google);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Intent signInIntent = googleSignInClient(context).getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            googleSignIn(account.getIdToken());

        } catch (ApiException e) {
            e.printStackTrace();
            googleSignOut(context);
            openAndFinish(context, os.com.krishirasayan.activities.LoginActivity.class);
        }
    }

    private void googleSignIn(String idToken) {
        new VolleyService(context).withParam("id_token", idToken).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                String token = response.optString("token");
                startActivity(new Intent(SignInGoogleActivity.this, os.com.krishirasayan.activities.AuthActivity.class).putExtra("token", token));
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
        }).post(API_SIGNIN_GOOGLE);
    }
}