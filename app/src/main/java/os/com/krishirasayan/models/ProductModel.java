package os.com.krishirasayan.models;

import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;

import android.content.Context;

import java.util.ArrayList;

import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;

public class ProductModel {

    Context context;
    VolleyService volleyService;
    ICallback onSuccess, onFailure, onPending;
    String url = API_PRODUCT_List;
    String id;
    String name;
    String description;
    String price;
    String point;
    String ImageUrl;
    String productImageUrl;
    String productName;
    String productVariant;
    String productQuantity;
    String productWishlist;
    Boolean productWishlisted;
    String retailerPrice;


    Integer bannerImageUrl;

    ArrayList<ProductVariantModel> productVariantModel;
/*
    //for wishlist
    public ProductModel(String name, String description, String price, Integer bannerImageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.bannerImageUrl = bannerImageUrl;
    }
    */

    public ProductModel(Integer bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public Integer getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(Integer bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public ProductModel(Context context) {
        this.context = context;
        this.volleyService = new VolleyService(this.context);
        this.onPending = this.onFailure = this.onSuccess = null;
    }

    public String getRetailerPrice() {
        return retailerPrice;
    }

    public void setRetailerPrice(String retailerPrice) {
        this.retailerPrice = retailerPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(String productVariant) {
        this.productVariant = productVariant;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public ArrayList<ProductVariantModel> getProductVariantModel() {
        return productVariantModel;
    }

    public void setProductVariantModel(ArrayList<ProductVariantModel> productVariantModel) {
        this.productVariantModel = productVariantModel;
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

    public void setProductImageUrl(String image_url) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setBannerImageUrl(String image_url) {
    }


    public String getProductWishlist() {
        return productWishlist;
    }

    public void setProductWishlist(String productWishlist) {
        this.productWishlist = productWishlist;
    }

    public Boolean getProductWishlisted() {
        return productWishlisted;
    }

    public void setProductWishlisted(Boolean productWishlisted) {
        this.productWishlisted = productWishlisted;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "context=" + context +
                ", volleyService=" + volleyService +
                ", onSuccess=" + onSuccess +
                ", onFailure=" + onFailure +
                ", onPending=" + onPending +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", point='" + point + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", productImageUrl='" + productImageUrl + '\'' +
                ", productName='" + productName + '\'' +
                ", productVariant='" + productVariant + '\'' +
                ", productQuantity='" + productQuantity + '\'' +
                ", bannerImageUrl=" + bannerImageUrl +
                ", productVariantModel=" + productVariantModel +
                '}';
    }
}
