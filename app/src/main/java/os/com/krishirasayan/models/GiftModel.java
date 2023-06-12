package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_GIFT_List;

import android.content.Context;

import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class GiftModel {

    Context context;
    VolleyService volleyService;
    ICallback onSuccess, onFailure, onPending;
    String url = API_GIFT_List;
    String id;
    String name;
    Integer points;
    String ImageUrl;
    String bannerImageUrl;
    Integer quantity;
    String requestTime;
    String updateTime;
    String requestStatus;
    Integer totalRedeemPoint;
    Integer totalQuantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public GiftModel(Context context) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTotalRedeemPoint() {
        return totalRedeemPoint;
    }

    public void setTotalRedeemPoint(Integer totalRedeemPoint) {
        this.totalRedeemPoint = totalRedeemPoint;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
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


    //This private field to maintain to every row's state...!
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object object) {
        return this.id.equals(((GiftModel) object).id);
    }

    public void subQuantity(int i) {
        if(this.quantity > 0) {
            quantity -= i;
            if(quantity < 1) quantity = 1;
        }
    }

    public void addQuantity(int i) {
        if(this.quantity < 99) {
            quantity += i;
            if(quantity > 99) quantity = 99;
        }
    }

    @Override
    public String toString() {
        return "GiftModel{" +
                "context=" + context +
                ", volleyService=" + volleyService +
                ", onSuccess=" + onSuccess +
                ", onFailure=" + onFailure +
                ", onPending=" + onPending +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", bannerImageUrl='" + bannerImageUrl + '\'' +
                ", quantity=" + quantity +
                ", requestTime='" + requestTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", totalRedeemPoint=" + totalRedeemPoint +
                ", totalQuantity=" + totalQuantity +
                ", isSelected=" + isSelected +
                '}';
    }
}
