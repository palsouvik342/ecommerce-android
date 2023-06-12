package os.com.krishirasayan.models;

import static os.com.krishirasayan.classes.UserData.getCart;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.w;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import os.com.krishirasayan.classes.UserData;

public class ProductVariantModel {
    String id;

    String name;
    ArrayList<String> values;
    String variantLabel;

    String listingPrice;
    String sellingPrice;
    String retailerListingPrice;
    String retailerSellingPrice;

    String point;

    String imageUrl;

    Integer quantity = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String getListingPrice() {
        return "₹ " + listingPrice;
    }

    public void setListingPrice(String listingPrice) {
        this.listingPrice = listingPrice;
    }

    public String getSellingPrice() {
        return "₹ " + sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getRetailerListingPrice() {
        return retailerListingPrice;
    }

    public void setRetailerListingPrice(String retailerListingPrice) {
        this.retailerListingPrice = retailerListingPrice;
    }

    public String getRetailerSellingPrice() {
        return retailerSellingPrice;
    }

    public void setRetailerSellingPrice(String retailerSellingPrice) {
        this.retailerSellingPrice = retailerSellingPrice;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVariantLabel() {
        return values.size() > 0 ? values.get(0) : "-";
    }

    public Integer getQuantity() {
        if(quantity > 99) quantity = 99;
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if(quantity < 0) quantity = 0;
        this.quantity = quantity;
    }
}
