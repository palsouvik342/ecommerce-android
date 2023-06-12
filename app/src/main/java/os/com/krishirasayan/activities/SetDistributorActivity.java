package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.popupSuccess;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_DISTRIBUTORS;
import static os.com.krishirasayan.consts.Url.API_GET_DISTRIBUTOR;
import static os.com.krishirasayan.consts.Url.API_SET_DISTRIBUTORS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.anurag.multiselectionspinner.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.Distributor;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;

public class SetDistributorActivity extends AppCompatActivity {
    Context context = this;

    Spinner spiDistributors;
    Button btnAssign;
    ArrayList<Distributor> distributors = new ArrayList<>();
    Distributor selectedDistributor = null;
    String TAG = "Users";
    TextView tvDistMobile, tvDistName, tvDistId;
    ListView listApprovedDistributor;
    MultiSpinner spinnerMultiSpinner;
    ArrayList<String> allDistributor = new ArrayList<>();
    ArrayList<String> approvedDistributor = new ArrayList<>();
    ArrayAdapter adapter;
    String listAddedDistId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_distributor);
        bindToolbar(context,R.string.set_distributor);

        w(UserData.getUserType(context));

        if(!UserData.getUserType(context).equals("retailer")) {
            Toast.makeText(context, R.string.must_be_retailer, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }


        listApprovedDistributor = findViewById(R.id.listApprovedDistributor);


        btnAssign = findViewById(R.id.btnAssign);
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDistributor();
            }
        });

        spinnerMultiSpinner = findViewById(R.id.spinnerMultiSpinner);
        downloadDistributors();
        downloadApprovedDistributors();
    }

    private void downloadApprovedDistributors() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {

            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {


                for(int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.optJSONObject(i);
                    approvedDistributor.add(jsonObject.optString("name")+" - id :"+ jsonObject.optString("id"));
                }

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, approvedDistributor);
                listApprovedDistributor.setAdapter(adapter);

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

            }
        }).get(API_SET_DISTRIBUTORS);
    }

    private void downloadDistributors() {
        new VolleyService(context)
                .withParam("district_code", UserData.getDistrictCode(context))
                .withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {

            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {


                for(int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.optJSONObject(i);
                    allDistributor.add(jsonObject.optString("name")+" - id :"+ jsonObject.optString("id"));
                }

                spinnerMultiSpinner.setAdapterWithOutImage(context, allDistributor, new MultiSelectionSpinnerDialog.OnMultiSpinnerSelectionListener() {
                    @Override
                    public void OnMultiSpinnerItemSelected(List<String> chosenItems) {
                        //This is where you get all your items selected from the Multi Selection Spinner :)

                        ArrayList<String> distIds = new ArrayList<>();

                        for (int i = 0; i < chosenItems.size(); i++){
                            Log.i("Users", chosenItems.get(i));
                            String str =  chosenItems.get(i);
                            String[] arrOfStr  = str.split(":",2);
                            distIds.add(arrOfStr[1]);
                        }
                        listAddedDistId = TextUtils.join(",", distIds);
                        Log.i("Users", TextUtils.join(",", distIds));

                    }
                });
                spinnerMultiSpinner.initMultiSpinner(context,spinnerMultiSpinner);

                /*adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, allDistributor);
                listApprovedDistributor.setAdapter(adapter);*/

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

            }
        }).get(API_DISTRIBUTORS);
    }

    private void setDistributor() {
        if(TextUtils.isEmpty(listAddedDistId)) {
            Toast.makeText(context, R.string.please_select_distributor, Toast.LENGTH_SHORT).show();
            return;
        }

        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("user_ids", listAddedDistId)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                        popupSuccess(context,"Dist Added");
                        openAndFinish(context,SetDistributorActivity.class);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                })
                .put(API_SET_DISTRIBUTORS);
        Toast.makeText(context, R.string.done, Toast.LENGTH_SHORT).show();
    }
}