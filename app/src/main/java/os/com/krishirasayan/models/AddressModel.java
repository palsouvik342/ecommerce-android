package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;

import org.json.JSONArray;
import org.json.JSONObject;

import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class AddressModel {

    Context context;
    VolleyService volleyService;
    ICallback onSuccess, onFailure, onPending;
    String url = API_ADDRESS_List;
    String id, contactName, contactNumber, line1, line2, postalCode, city, district, state, country, location;

    public AddressModel(Context context) {
        this.context = context;
        this.volleyService = new VolleyService(this.context);
        this.onPending = this.onFailure = this.onSuccess = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location.latitude + " " + location.longitude;
    }

    public void setOnSuccess(ICallback callback) {
        this.onSuccess = callback;
    }

    public void setOnFailure(ICallback callback) {
        this.onFailure = callback;
    }

    public void setOnPending(ICallback callback) {
        this.onPending = callback;
    }

    private void setParams() {
        this.volleyService
                .withParam("contact_name", this.contactName)
                .withParam("contact_number", this.contactNumber)
                .withParam("line1", this.line1)
                .withParam("line2", this.line2)
                .withParam("district", this.district)
                .withParam("postal_code", this.postalCode)
                .withParam("city", this.city)
                .withParam("state", this.state)
                .withParam("country", this.country)
                .withParam("location", this.location)
                .withParam("token", UserData.getToken(this.context));
    }

    public void create() {
        AddressModel instance = this;
        this.setParams();
        this.volleyService.withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                if(instance.onPending != null) instance.onPending.function(true);
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                if(instance.onPending != null) instance.onPending.function(false);
                if(instance.onSuccess != null) instance.onSuccess.function(response);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {
                if(instance.onPending != null) instance.onPending.function(false);
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                if(instance.onPending != null) instance.onPending.function(false);
                if(instance.onFailure != null) instance.onFailure.function(responseData);
            }
        });
        this.volleyService.post(this.url);
    }

    public void update() {
        AddressModel instance = this;
        this.setParams();
        this.volleyService.withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                instance.onPending.function(true);
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                instance.onPending.function(false);
                instance.onSuccess.function(response);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {
                instance.onPending.function(false);
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                instance.onPending.function(false);
                instance.onFailure.function(responseData);
            }
        });
        this.volleyService.put(this.url + "/" + this.id);
    }

    public void delete() {
        AddressModel instance = this;
        this.volleyService.withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                instance.onPending.function(true);
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                instance.onPending.function(false);
                instance.onSuccess.function(response);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {
                instance.onPending.function(false);
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                instance.onPending.function(false);
                instance.onFailure.function(responseData);
            }
        });
        this.volleyService.delete(this.url + "/" + this.id);
    }
}
