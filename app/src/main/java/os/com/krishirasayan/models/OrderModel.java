package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;

import android.content.Context;

import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class OrderModel {

    Context context;
    VolleyService volleyService;
    ICallback onSuccess, onFailure, onPending;
    String url = API_CUSTOMER_ORDERS;
    String id;
    String orderNumber;
    String status;
    String orderedTime;
    String orderedUpdateTime;
    String statusTime;
    String totalPrice;
    String ImageUrl;
    String totalPoints;
    String orderProductName;
    String orderProductPrice;
    String orderProductPoint;
    String orderProductQty;
    String orderProductVariant;
    String orderDistributor;
    int orderInvoiceApproved;

    public int getOrderInvoiceApproved() {
        return orderInvoiceApproved;
    }

    public void setOrderInvoiceApproved(int orderInvoiceApproved) {
        this.orderInvoiceApproved = orderInvoiceApproved;
    }

    public String getOrderedUpdateTime() {
        return orderedUpdateTime;
    }

    public void setOrderedUpdateTime(String orderedUpdateTime) {
        this.orderedUpdateTime = orderedUpdateTime;
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

    public OrderModel(Context context) {
        this.context = context;
        this.volleyService = new VolleyService(this.context);
        this.onPending = this.onFailure = this.onSuccess = null;
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

    public String getOrderProductPrice() {
        return orderProductPrice;
    }

    public void setOrderProductPrice(String orderProductPrice) {
        this.orderProductPrice = orderProductPrice;
    }

    public String getOrderProductPoint() {
        return orderProductPoint;
    }

    public void setOrderProductPoint(String orderProductPoint) {
        this.orderProductPoint = orderProductPoint;
    }

    public String getOrderProductQty() {
        return orderProductQty;
    }

    public void setOrderProductQty(String orderProductQty) {
        this.orderProductQty = orderProductQty;
    }

    public String getOrderProductVariant() {
        return orderProductVariant;
    }

    public void setOrderProductVariant(String orderProductVariant) {
        this.orderProductVariant = orderProductVariant;
    }

    public String getOrderProductName() {
        return orderProductName;
    }

    public void setOrderProductName(String orderProductName) {
        this.orderProductName = orderProductName;
    }
    public String getOrderDistributor() {
        return orderDistributor;
    }

    public void setOrderDistributor(String orderDistributor) {
        this.orderDistributor = orderDistributor;
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
