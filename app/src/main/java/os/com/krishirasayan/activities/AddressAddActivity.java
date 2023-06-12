package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.coordsToAddress;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AddressModel;

import java.util.Map;

public class AddressAddActivity extends AppCompatActivity {

    Context context = this;
    final int LOCATION_PICKER_REQUEST_CODE = 99;

    LatLng location = new LatLng(0, 0);
    EditText etContactName, etContactNumber, etLine1, etLine2, etDistrict, etPostalCode, etCity, etState, etCountry;
    Button btnLocation, btnAddAddress, btnSaveAddress;
    String addressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        bindToolbar(context,R.string.add_address);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnSaveAddress.setVisibility(View.GONE);
        btnLocation = findViewById(R.id.btnLocation);

        /*addressId = not null;*/
        if(getIntent().hasExtra("address_id")) {
            addressId = getIntent().getStringExtra("address_id");
            btnAddAddress.setVisibility(View.GONE);
            btnSaveAddress.setVisibility(View.VISIBLE);
            downloadAddressItem();
        }else {
            btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    startActivityForResult(intent, LOCATION_PICKER_REQUEST_CODE);
                }
            });
        }

        /*
        if(getIntent().hasExtra("address_delete_id")) {
            addressId = getIntent().getStringExtra("address_delete_id");
            btnAddAddress.setAlpha(0);
            btnSaveAddress.setAlpha(1);
            downloadAddressItem();
        }
        */

        etContactName = findViewById(R.id.etContactName);
        etContactNumber = findViewById(R.id.etContactNumber);
        etLine1 = findViewById(R.id.etLine1);
        etLine2 = findViewById(R.id.etLine2);
        etPostalCode = findViewById(R.id.etPostalCode);
        etDistrict = findViewById(R.id.etDistrict);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etCountry = findViewById(R.id.etCountry);




        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressModel addressModel = new AddressModel(context);
                addressModel.setContactName(etContactName.getText().toString());
                addressModel.setContactNumber(etContactNumber.getText().toString());
                addressModel.setLine1(etLine1.getText().toString());
                addressModel.setLine2(etLine2.getText().toString());
                addressModel.setDistrict(etDistrict.getText().toString());
                addressModel.setPostalCode(etPostalCode.getText().toString());
                addressModel.setCity(etCity.getText().toString());
                addressModel.setState(etState.getText().toString());
                addressModel.setCountry(etCountry.getText().toString());
                addressModel.setLocation(location);
                addressModel.setOnPending(new ICallback() {
                    @Override
                    public void function(Object object) {
                        boolean status = (boolean) object;
                        if(status) {
                            popupInfo(context, R.string.please_wait);
                        }
                    }
                });
                addressModel.setOnSuccess(new ICallback() {
                    @Override
                    public void function(Object object) {
                        onBackPressed();
                    }
                });
                addressModel.setOnFailure(new ICallback() {
                    @Override
                    public void function(Object object) {
                        if( TextUtils.isEmpty(etDistrict.getText())) {
                            popupError(context, "District field is empty");
                        }
                        else{
                            popupError(context, R.string.something_wrong);
                        }
                    }
                });
                addressModel.create();
            }
        });
    }

    private void downloadAddressItem() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                       // w(response);
                        etContactName.setText(response.optString("contact_name"));
                        etContactNumber.setText(response.optString("contact_number"));
                        etLine1.setText(response.optString("line1"));
                        etLine2.setText(response.optString("line2"));
                        etPostalCode.setText(response.optString("postal_code"));
                        etDistrict.setText(response.optString("district"));
                        etCity.setText(response.optString("city"));
                        etState.setText(response.optString("state"));
                        etCountry.setText(response.optString("country"));
                        //location.setText(response.optString("location"));String str = response.optString("location");
                        w(response.optString("location"));
                        String str = response.optString("location");
                        String[] splitStr = str.split("\\s+");
                        Double lat = Double.valueOf(splitStr[0]);
                        Double lng = Double.valueOf(splitStr[1]);
                        w(lat);
                        w(lng);

                        btnLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, MapsActivity.class);
                                //Double lat = 22.09976;
                                //Double lng = 88.09976;
                                if(!response.optString("location").equals(" ")){
                                    intent.putExtra("lat",lat);
                                    intent.putExtra("lng",lng);
                                }

                                startActivityForResult(intent, LOCATION_PICKER_REQUEST_CODE);
                            }
                        });
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }

                }).get(API_ADDRESS_List + "/" + addressId);


        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressModel addressModel = new AddressModel(context);
                addressModel.setId(addressId);
                addressModel.setContactName(etContactName.getText().toString());
                addressModel.setContactNumber(etContactNumber.getText().toString());
                addressModel.setLine1(etLine1.getText().toString());
                addressModel.setLine2(etLine2.getText().toString());
                addressModel.setDistrict(etDistrict.getText().toString());
                addressModel.setPostalCode(etPostalCode.getText().toString());
                addressModel.setCity(etCity.getText().toString());
                addressModel.setState(etState.getText().toString());
                addressModel.setCountry(etCountry.getText().toString());
                addressModel.setLocation(location);
                addressModel.setOnPending(new ICallback() {
                    @Override
                    public void function(Object object) {
                        boolean status = (boolean) object;
                        if(status) {
                            popupInfo(context, R.string.please_wait);
                        }
                    }
                });
                addressModel.setOnSuccess(new ICallback() {
                    @Override
                    public void function(Object object) {
                        //onBackPressed();
                            finish();
                            open(context, MainActivity.class);

                    }
                });
                addressModel.setOnFailure(new ICallback() {
                    @Override
                    public void function(Object object) {
                        if( TextUtils.isEmpty(etDistrict.getText())) {
                            popupError(context, "District field is empty");
                        }
                        else{
                            popupError(context, R.string.something_wrong);
                        }
                    }
                });
                addressModel.update();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case LOCATION_PICKER_REQUEST_CODE:
                if(resultCode == RESULT_OK) readLocation(data);
                else Toast.makeText(context, R.string.could_not_read_location, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void readLocation(Intent data) {
        double latitude = data.getDoubleExtra("latitude", 0.0);
        double longitude = data.getDoubleExtra("longitude", 0.0);

        Map<String, String> locationData = coordsToAddress(context, new LatLng(latitude, longitude));

        etLine1.setText(locationData.get("address"));
        etPostalCode.setText(locationData.get("postalCode"));
        etCity.setText(locationData.get("city"));
        etState.setText(locationData.get("state"));
        etCountry.setText(locationData.get("country"));

        location = new LatLng(latitude, longitude);
        w(location);
    }
}