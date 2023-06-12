package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.bindToolbarWithNavDraw;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.navigation.NavigationView;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.AddressAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AddressModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {

    Context context = this;

    Button btnAddAddress, btAddressListEditButton, btAddressListDeleteButton;
    RecyclerView rvList;
    NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        //bindToolbarWithNavDraw(context,R.string.welcome);
        bindToolbar(context,R.string.address);
        bindReferences();
        downloadAddressItems();
    }


    private void bindReferences() {

        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, AddressAddActivity.class);
            }
        });

        rvList = findViewById(R.id.rvList);
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
        //super.onBackPressed();
    }

    private void downloadAddressItems() {
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
                        List<AddressModel> addressList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            AddressModel addressModel = new AddressModel(context);
                            addressModel.setId(response.optJSONObject(i).optString("id"));
                            addressModel.setContactName(response.optJSONObject(i).optString("contact_name"));
                            addressModel.setContactNumber(response.optJSONObject(i).optString("contact_number"));
                            addressModel.setLine1(response.optJSONObject(i).optString("line1"));
                            addressModel.setLine2(response.optJSONObject(i).optString("line2"));
                            addressModel.setPostalCode(response.optJSONObject(i).optString("postal_code"));
                            addressModel.setCity(response.optJSONObject(i).optString("city"));
                            addressModel.setState(response.optJSONObject(i).optString("state"));
                            addressModel.setCountry(response.optJSONObject(i).optString("country"));
                            addressList.add(addressModel);
                        }

                        setFollowingRecycler(addressList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_ADDRESS_List );
    }

    private void setFollowingRecycler(List<AddressModel> addressList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(new AddressAdapter(context, addressList));
    }
}