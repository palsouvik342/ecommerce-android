package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ALL_STATE;
import static os.com.krishirasayan.consts.Url.API_SET_DISTRIBUTORS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.Distributor;
import os.com.krishirasayan.classes.Language;
import os.com.krishirasayan.classes.State;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

public class UserChooseActivity extends AppCompatActivity {

    Button btnChooseUser,btnChooseRetailer, btnChooseDistributor, btToLoginPage;
    Context context = this;
   // MaterialSpinner spinnerLanguage;
    Spinner stateSpinner, languageSpinner;
    String ROLETYPE, LANGUAGETYPE ,ROLETYPE_TITLE;
    String selectedLocale = "",customerString,retailerString,distributorString;
    TextView textView9, textView11;
    ListView listAllStates;
    ArrayList<State> stateList = new ArrayList<>();
    State selectedState = null;
    Language selectedLanguage = null;
    ArrayAdapter adapter;
    Integer stateId;
    String languageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_choose);
        bindrefarence();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("DO YOU REALLY WANT TO EXIT ??")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finishAffinity();
                        System.exit(0);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void bindrefarence() {
        selectedLocale = UserData.getLocale(context);
        setAppLocale(selectedLocale);

        languageSpinner = findViewById(R.id.languageSpinner);
        stateSpinner = findViewById(R.id.stateSpinner);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = (State) parent.getSelectedItem();
                stateId = selectedState.getId();
                State state = stateList.stream()
                        .filter(s -> stateId == s.getId())
                        .findAny()
                        .orElse(null);
                if(state != null){
                    ArrayList<Language> languages = state.getLanguages();
                    ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(context, android.R.layout.simple_spinner_dropdown_item, languages);

                    languageSpinner.setAdapter(adapter);
                    languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedLanguage = (Language) parent.getSelectedItem();
                            languageCode = selectedLanguage.getCode();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getStates();
        LANGUAGETYPE = "English";

        //spinnerLanguage.setItems("English", "Bengali");

       /* spinnerLanguage.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                LANGUAGETYPE = item.toLowerCase();
                Snackbar.make(view, "Clicked " + LANGUAGETYPE, Snackbar.LENGTH_LONG).show();
                switch (item) {
                    case "English":
                        selectedLocale = "en";
                        break;
                    case "Bengali":
                        selectedLocale = "bn";
                        break;
                }
                setAppLocale(selectedLocale);
                finish();
                startActivity(getIntent());
            }
        });

        switch (selectedLocale) {
            case "en":
                languageSpinner.setSelectedIndex(0);
                break;
            case "bn":
                languageSpinner.setSelectedIndex(1);
                break;
        }*/

        textView9 = findViewById(R.id.textView9);
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context,TermsConditionActivity.class);
                startActivity(intent);
            }});

        textView11 = findViewById(R.id.textView11);
        textView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context,TermsConditionActivity.class);
                startActivity(intent);
            }});


        btToLoginPage = findViewById(R.id.btToLoginPage);
        btToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLocale(languageCode);
                //startActivity(getIntent());

                Intent intent =  new Intent(context,LoginActivity.class);
                intent.putExtra("STATE_ID", stateId);
                finish();
                startActivity(intent);
            }
        });
    }


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

    private void setAppLocale(String localeCode) {
        UserData.setLocale(context, localeCode);
        //Toast.makeText(context, "Changed language to " + localeCode, Toast.LENGTH_SHORT).show();

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    private void getStates() {
        new VolleyService(context)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        stateList.add(new State(0, "-Select-", new ArrayList<>()));
                        for(int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.optJSONObject(i);
                            ArrayList<Language> languages = new ArrayList<>();
                            for(int j=0; j< jsonObject.optJSONArray("languages").length(); j++){
                                languages.add(new Language(jsonObject.optJSONArray("languages").optJSONObject(j).optInt("id"), jsonObject.optJSONArray("languages").optJSONObject(j).optString("name"), jsonObject.optJSONArray("languages").optJSONObject(j).optString("code")));
                            }
                            stateList.add(new State(jsonObject.optInt("id"), jsonObject.optString("name"), languages));
                        }
                        ArrayAdapter<State> adapter = new ArrayAdapter<State>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
                        stateSpinner.setAdapter(adapter);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                }).get(API_ALL_STATE);
    }

}