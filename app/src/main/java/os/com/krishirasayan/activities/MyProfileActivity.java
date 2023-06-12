package os.com.krishirasayan.activities;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static os.com.krishirasayan.consts.Helper.bindBottomNavigation;
import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.coordsToAddress;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.getBase64FromFileURI;
import static os.com.krishirasayan.consts.Helper.logout;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.popupSuccess;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import static os.com.krishirasayan.consts.Url.API_ALL_STATE;
import static os.com.krishirasayan.consts.Url.API_PROFILE_UPDATE;
import static os.com.krishirasayan.consts.Url.API_READ_PROFILE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.VolleyError;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.Language;
import os.com.krishirasayan.classes.State;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AccountModel;
import os.com.krishirasayan.models.NewsModel;

public class MyProfileActivity extends AppCompatActivity {

    Context context = this;
    AwesomeValidation loginValidation;
    ImageView ivUploadProfileImage, ivLicencePreview, ivGetMyLocation, ivRefreshButton;
    CircleImageView ivProfile;
    String encodedImage,profileName,profileAddress,profileEmailid, USER_ROLE_ID, profilePinCode, licenceImage;
    EditText etProfileDistrict, etProfilePincodeOnly, etProfileName,etProfileMobileNumber,etProfileAddress,etProfileEmailId, etDistributorCode,etProfileState, etProfileCountry;
    Button btSaveProfileButton, buttonUploadLicence, btLogoutConfirm, btLogoutCancel;
    BottomNavigationView bottomNavigationView;
    String selectedLicense = "";
    String selectedLocale = "";
    String LANGUAGETYPE ;
    LinearLayout llLicenseUpload, llDistributorCode;
    ConstraintLayout clVerifiedBadge, clNotVerified, clLogoutPopup, clProfileDetailsPage;
    Spinner languageSpinner;
    String districtName = "", contactName = "", contactNo = "", lineOne = "", lineTwo = "", postalCodd = "", city = "", state = "", country = "", distributorCode = "", addressLocation = "";

    final int LOCATION_PICKER_REQUEST_CODE = 99;
    final int RESULT_CODE_PROFILE_IMAGE = 998;
    final int RESULT_CODE_PROFILE_LICENSE = 999;
    LatLng location = new LatLng(0, 0);
    LatLng locationMain = new LatLng(0, 0);

    Language selectedLanguage = null;
    ArrayAdapter adapter;
    Integer stateId;
    String languageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        bindToolbar(context,R.string.my_profile);

        bindBottomNavigation(context);

        bindReferences();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if(USER_ROLE_ID.equals("4") || USER_ROLE_ID.equals("5")) {
            ivRefreshButton.setVisibility(View.VISIBLE);
            if (UserData.isVerified(context)) bottomNavigationView.setVisibility(View.VISIBLE);
            else bottomNavigationView.setVisibility(View.GONE);
        }else{
            bottomNavigationView.setVisibility(View.VISIBLE);
            ivRefreshButton.setVisibility(View.GONE);
        }

        clLogoutPopup = findViewById(R.id.clLogoutPopup);
        clLogoutPopup.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(USER_ROLE_ID.equals("4") || USER_ROLE_ID.equals("5")) {
            if (UserData.isVerified(context)) open(context, MainActivity.class);
            else clLogoutPopup.setVisibility(View.VISIBLE);
        }else {
            open(context,MainActivity.class);
        }

    }

    private void bindReferences() {
        selectedLocale = UserData.getLocale(context);
        setAppLocale(selectedLocale);

        LANGUAGETYPE = "English";
        languageSpinner = findViewById(R.id.languageSpinner);
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
        getLanguage();


        clProfileDetailsPage = findViewById(R.id.clProfileDetailsPage);

        btLogoutCancel = findViewById(R.id.btLogoutCancel);
        btLogoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              clLogoutPopup.setVisibility(View.GONE);
            }
        });

        btLogoutConfirm = findViewById(R.id.btLogoutConfirm);
        btLogoutConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               logout(context);
            }
        });

        ivGetMyLocation = findViewById(R.id.ivGetMyLocation);
        ivGetMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                startActivityForResult(intent, LOCATION_PICKER_REQUEST_CODE);
            }
        });

        ivRefreshButton = findViewById(R.id.ivRefreshButton);
        ivRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateToken();
            }
        });

        llLicenseUpload = findViewById(R.id.llLicenseUpload);
        buttonUploadLicence = findViewById(R.id.buttonUploadLicence);

        llDistributorCode = findViewById(R.id.llDistributorCode);
        etDistributorCode = findViewById(R.id.etDistributorCode);

        clVerifiedBadge = findViewById(R.id.clVerifiedBadge);
        clVerifiedBadge.setVisibility(View.GONE);

        clNotVerified = findViewById(R.id.clNotVerified);
        clNotVerified.setVisibility(View.GONE);


        llLicenseUpload.setVisibility(View.GONE);
        llDistributorCode.setVisibility(View.GONE);

        showVerifiedBadge();

        buttonUploadLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(MyProfileActivity.this).crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(RESULT_CODE_PROFILE_LICENSE);
            }
        });

        ivLicencePreview  = findViewById(R.id.ivLicencePreview);
        ivLicencePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePopup();
            }
        });

        etProfileName  = findViewById(R.id.etProfileName);
        etProfileState  = findViewById(R.id.etProfileState);
        etProfileCountry  = findViewById(R.id.etProfileCountry);
        etProfileMobileNumber  = findViewById(R.id.etProfileMobileNumber);
        etProfileAddress  = findViewById(R.id.etProfileAddress);
        etProfilePincodeOnly  = findViewById(R.id.etProfilePincodeOnly);
        etProfileDistrict  = findViewById(R.id.etProfileDistrict);
        etProfileEmailId  = findViewById(R.id.etProfileEmailId);
        btSaveProfileButton  = findViewById(R.id.btSaveProfileButton);
        ivProfile = findViewById(R.id.ivProfile);

        loginValidation = new AwesomeValidation(BASIC);
        loginValidation.addValidation((Activity) context, R.id.etProfileName, RegexTemplate.NOT_EMPTY, R.string.require);
        loginValidation.addValidation((Activity) context, R.id.etProfileDistrict, RegexTemplate.NOT_EMPTY, R.string.require);
        loginValidation.addValidation((Activity) context, R.id.etProfileCountry, RegexTemplate.NOT_EMPTY, R.string.require);
        loginValidation.addValidation((Activity) context, R.id.etProfileState, RegexTemplate.NOT_EMPTY, R.string.require);

        btSaveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginValidation.validate()) {
                    updateProfileData();
                    updateAddressData();
                    changeLanguage();
                }
            }
        });

        ivLicencePreview = findViewById(R.id.ivLicencePreview);

        ivProfile = findViewById(R.id.ivProfile);
        ivUploadProfileImage = findViewById(R.id.ivUploadProfileImage);
        ivUploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(MyProfileActivity.this).crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(RESULT_CODE_PROFILE_IMAGE);
                
            }
        });
        downloadProfileDetails();
    }

    private void validateToken() {
        String token = UserData.getToken(context);
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
                UserData.save(context, accountModel);
                showVerifiedBadge();
                if(UserData.isVerified(context)) {
                    Toast.makeText(context, R.string.user_verified, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, R.string.user_not_verified, Toast.LENGTH_SHORT).show();
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

    public  void updateProfileData(){
        profileName =  etProfileName.getText().toString();
        //profileAddress =  etProfileAddress.getText().toString();
        profilePinCode =  etProfilePincodeOnly.getText().toString();
        profileEmailid =  etProfileEmailId.getText().toString();
        distributorCode =  etDistributorCode.getText().toString();
        uploadProfileDetails();
    }

    private void uploadProfileDetails() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("name", profileName)
                .withParam("email", profileEmailid)
                .withParam("postal_code", profilePinCode)
                .withParam("license", selectedLicense)
                .withParam("distributor_code", distributorCode)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        popupSuccess(context,"Success");
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                        UpdateUi(response);

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context, responseData);
                    }
                }).put(API_PROFILE_UPDATE);
    }

    private void downloadProfileDetails() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        UpdateUi(response);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context, responseData);
                    }
                }).get(API_PROFILE_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        d(requestCode);
        d(resultCode);
        switch (requestCode) {
            case RESULT_CODE_PROFILE_IMAGE:
                ivProfile.setImageURI(uri);
                encodedImage = getBase64FromFileURI(uri, context, true);
                uploadProfileImage();
                //w(encodedImage);
                break;
            case RESULT_CODE_PROFILE_LICENSE:
                ivLicencePreview.setImageURI(uri);
                selectedLicense = getBase64FromFileURI(uri, context, true);
                break;
            case LOCATION_PICKER_REQUEST_CODE:
                if(resultCode == RESULT_OK) readLocation(data);
                else Toast.makeText(context, R.string.could_not_read_location, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void uploadProfileImage() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("avatar", encodedImage)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        popupSuccess(context,"Success");
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        UpdateUi(response);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context, responseData);
                    }
                }).put(API_PROFILE_UPDATE);
    }

    public void UpdateUi(JSONObject response) {
        JSONObject address = response.optJSONObject("address");
        if(address != null) {

            etProfileAddress.setText(address.optString("line1"));
            etProfilePincodeOnly.setText(address.optString("postal_code"));
            etProfileDistrict.setText(address.optString("district"));
            etProfileState.setText(address.optString("state"));
            etProfileCountry.setText(address.optString("country"));

            contactName = address.optString("contact_name");
            contactNo = address.optString("contact_number");
            lineOne = address.optString("line1");
            lineTwo = address.optString("line2");
            postalCodd = address.optString("postal_code");
            districtName = address.optString("district");
            country = address.optString("country");
            state = address.optString("state");
            city = address.optString("city");
            addressLocation = address.optString("location");
        }
        etProfileName.setText(response.optString("name"));
        etProfileMobileNumber.setText(response.optString("phone"));
        etProfileMobileNumber.setEnabled(false);
        //if(response.optString("email").equals(null))
        if(response.optString("email") != null &&
                !response.optString("email").isEmpty() &&
                !response.optString("email").equals("null") ){
            etProfileEmailId.setText(response.optString("email"));
        } else {
            etProfileEmailId.setText("");
        }

        if(response.optString("distributor_code") != null &&
                !response.optString("distributor_code").isEmpty() &&
                !response.optString("distributor_code").equals("null") ){
        etDistributorCode.setText(response.optString("distributor_code"));
        } else {
            etDistributorCode.setText("");
        }
        licenceImage =  response.optString("license_url");

        Picasso.get()
                .load( response.optString("avatar_url") )
                .error( R.drawable.image_avatar_default )
                .placeholder( R.drawable.image_avatar_default )
                .into( ivProfile );
        Picasso.get()
                .load( response.optString("license_url") )
                .error( R.drawable.image_avatar_default )
                .placeholder( R.drawable.image_avatar_default )
                .into( ivLicencePreview );

    }

    private void readLocation(Intent data) {
        double latitude = data.getDoubleExtra("latitude", 0.0);
        double longitude = data.getDoubleExtra("longitude", 0.0);

        Map<String, String> locationData = coordsToAddress(context, new LatLng(latitude, longitude));

        etProfileAddress.setText(locationData.get("address"));
        etProfilePincodeOnly.setText(locationData.get("postalCode"));
        lineOne =  locationData.get("address");
        lineTwo =  "";
        //postalCodd =  locationData.get("postalCode");
        city =  locationData.get("city");
        etProfileState.setText(locationData.get("state"));
        etProfileCountry.setText(locationData.get("country"));

        //state =  locationData.get("state");
        //country =  locationData.get("country");
        addressLocation = latitude + " " + longitude;
        location = new LatLng(latitude, longitude);
        w(location);
    }

    public  void updateAddressData(){
        contactName =  etProfileName.getText().toString();
        contactNo =  etProfileMobileNumber.getText().toString();
        districtName =  etProfileDistrict.getText().toString();
        postalCodd =  etProfilePincodeOnly.getText().toString();
        country =  etProfileCountry.getText().toString();
        state =  etProfileState.getText().toString();

        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("contact_name", contactName)
                .withParam("contact_number", contactNo)
                .withParam("line1", etProfileAddress.getText().toString())
                .withParam("line2", lineTwo)
                .withParam("postal_code", postalCodd)
                .withParam("district", districtName)
                .withParam("city", city)
                .withParam("state", state)
                .withParam("country", country)
                .withParam("location", addressLocation)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        popupSuccess(context,"Updating...");
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        popupSuccess(context,"Updated Successfully.");
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        if( TextUtils.isEmpty(etProfileAddress.getText())) {
                            popupError(context, "Address field is required");
                        } else if (TextUtils.isEmpty(etProfileDistrict.getText())) {
                            popupError(context, "District field is required");
                        } else if (TextUtils.isEmpty(etProfilePincodeOnly.getText())) {
                            popupError(context, "PinCode field is required");
                        }
                        else{
                            popupError(context, responseData);
                        }
                    }
                }).put(API_ADDRESS_List);
    }

    private void showVerifiedBadge(){
        USER_ROLE_ID=UserData.getKeyUserRoleId(context);

        if(USER_ROLE_ID.equals("4")){
            llLicenseUpload.setVisibility(View.VISIBLE);
        } else if (USER_ROLE_ID.equals("5")){
            llDistributorCode.setVisibility(View.VISIBLE);
        }

        if(USER_ROLE_ID.equals("4") || USER_ROLE_ID.equals("5")){
            if(UserData.isVerified(context)){
                clVerifiedBadge.setVisibility(View.VISIBLE);
                clNotVerified.setVisibility(View.GONE);
                buttonUploadLicence.setVisibility(View.GONE);
                etDistributorCode.setEnabled(false);
            }else {
                clNotVerified.setVisibility(View.VISIBLE);
                clVerifiedBadge.setVisibility(View.GONE);
                etDistributorCode.setEnabled(true);

            }
        }
    }

    private void showImagePopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.modal_popup_profile_image, null);

        final PopupWindow popupWindowTerms = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

        ImageView ivImage = popupView.findViewById(R.id.ivImage);
        ImageView ivImageClose = popupView.findViewById(R.id.ivProfilPicClose);

        Picasso.get()
                .load( licenceImage )
                .error( R.drawable.image_avatar_default )
                .placeholder( R.drawable.image_avatar_default )
                .into( ivImage );

        ivImageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowTerms.dismiss();
            }
        });

        popupWindowTerms.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //mealDetailsDelivery.setChecked(false);
                popupWindowTerms.dismiss();
            }
        });
        popupWindowTerms.setOutsideTouchable(false);
        popupWindowTerms.showAtLocation(clProfileDetailsPage, Gravity.CENTER, 0, 0);
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

    private void getLanguage() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        ArrayList<Language> languages = new ArrayList<>();
                        for(int j=0; j< response.optJSONArray("languages").length(); j++){
                            JSONObject language = response.optJSONArray("languages").optJSONObject(j);
                            languages.add(new Language(language.optInt("id"), language.optString("name"), language.optString("code")));
                        }
                        ArrayAdapter<Language> adapter = new ArrayAdapter<Language>(context, android.R.layout.simple_spinner_dropdown_item, languages);
                        languageSpinner.setAdapter(adapter);
                    }
                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                   /*     for(int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.optJSONObject(i);
                            ArrayList<Language> languages = new ArrayList<>();
                            for(int j=0; j< jsonObject.optJSONArray("languages").length(); j++){
                                languages.add(new Language(jsonObject.optJSONArray("languages").optJSONObject(j).optInt("id"), jsonObject.optJSONArray("languages").optJSONObject(j).optString("name"), jsonObject.optJSONArray("languages").optJSONObject(j).optString("code")));
                            }
                       }
                        ArrayAdapter<State> adapter = new ArrayAdapter<State>(context, android.R.layout.simple_spinner_dropdown_item, stateList);
                        languageSpinner.setAdapter(adapter);*/
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                }).get(API_READ_PROFILE);
    }

    private void changeLanguage(){
        setAppLocale(languageCode);
        finish();
        startActivity(getIntent());
    }
}