package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;

import android.content.Context;

import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class CropMspModel {

    Context context;
    VolleyService volleyService;
    ICallback onSuccess, onFailure, onPending;
    String url = API_CUSTOMER_ORDERS;
    String id;
    String orderNumber;
    String status;
    String orderedTime;
    String statusTime;
    String totalPrice;
    String totalPoints;

    String name;
    String ImageUrl;
    String mspRate;
    String mspUnit;
    String mspYear;


    public String getMspRate() {
        return mspRate;
    }

    public void setMspRate(String mspRate) {
        this.mspRate = mspRate;
    }

    public String getMspUnit() {
        return mspUnit;
    }

    public void setMspUnit(String mspUnit) {
        this.mspUnit = mspUnit;
    }

    public String getMspYear() {
        return mspYear;
    }

    public void setMspYear(String mspYear) {
        this.mspYear = mspYear;
    }



    /* public NewsModel(String ContactName, String ContactNumber, String postalCode, Integer ImageUrl) {
         this.contactName = ContactName;
         this.contactNumber = ContactNumber;
         this.postalCode = postalCode;
         this.ImageUrl = ImageUrl;
     }

 */
    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public CropMspModel(Context context) {
        this.context = context;
        this.volleyService = new VolleyService(this.context);
        this.onPending = this.onFailure = this.onSuccess = null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderedTime() {
        return orderedTime;
    }

    public void setOrderedTime(String orderedTime) {
        this.orderedTime = orderedTime;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
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
                .withParam("token", UserData.getToken(this.context));
    }

}
