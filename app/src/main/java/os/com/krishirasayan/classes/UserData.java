package os.com.krishirasayan.classes;

import static android.content.Context.MODE_PRIVATE;
import static os.com.krishirasayan.consts.Config.SF_USERDATA_FILENAME;
import static os.com.krishirasayan.consts.Helper.getCartItemKeyByRetailer;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.w;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import os.com.krishirasayan.models.AccountModel;

public class UserData {
    public static final String KEY_TOKEN = "access_token";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_LOCALE = "locale";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_TIMEZONE = "timezone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_PHOTO_URL = "photo_url";
    public static final String KEY_WALLET_BALANCE = "wallet_balance";
    public static final String KEY_FOLLOWER_COUNT = "follower_count";
    public static final String KEY_FOLLOWING_COUNT = "following_count";
    public static final String KEY_USER_TYPE = "user_type";
    public static final String KEY_USER_ROLE_ID = "role_id";
    public static final String KEY_USER_REWARD_POINTS = "reward_points";
    public static final String KEY_CART = "cart";
    public static final String KEY_INVOICE_CART = "invoice_cart";
    public static final String KEY_VERIFIED = "verified";
    public static final String KEY_DISTRICT_CODE = "district_code";

    public static final String USER_TYPE_CUSTOMER = "customer";
    public static final String USER_TYPE_RETAILER = "retailer";
    public static final String USER_TYPE_DISTRIBUTOR = "distributor";

    public static AccountModel load(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        AccountModel accountModel = new AccountModel();
        accountModel.setToken(sharedPreferences.getString(KEY_TOKEN, ""));
        accountModel.setId(sharedPreferences.getString(KEY_ID, ""));
        accountModel.setName(sharedPreferences.getString(KEY_NAME, ""));
        accountModel.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
        accountModel.setPhone(sharedPreferences.getString(KEY_PHONE, ""));
        accountModel.setLocale(sharedPreferences.getString(KEY_LOCALE, ""));
        accountModel.setCurrency(sharedPreferences.getString(KEY_CURRENCY, ""));
        accountModel.setTimezone(sharedPreferences.getString(KEY_TIMEZONE, ""));
        accountModel.setAddress(sharedPreferences.getString(KEY_ADDRESS, ""));
        accountModel.setAddress(sharedPreferences.getString(KEY_PHOTO_URL, ""));
        accountModel.setWallet(sharedPreferences.getString(KEY_WALLET_BALANCE, "0.00"));
        accountModel.setFollowerCount(sharedPreferences.getString(KEY_FOLLOWER_COUNT, "0"));
        accountModel.setFollowingCount(sharedPreferences.getString(KEY_FOLLOWING_COUNT, "0"));
        accountModel.setUserType(sharedPreferences.getString(KEY_USER_TYPE, ""));
        accountModel.setRoleId(sharedPreferences.getString(KEY_USER_ROLE_ID, ""));
        accountModel.setRewardPoints(sharedPreferences.getString(KEY_USER_REWARD_POINTS, "0"));
        accountModel.setFollowingCount(sharedPreferences.getString(KEY_CART, ""));
        accountModel.setDistrictCode(sharedPreferences.getString(KEY_DISTRICT_CODE, ""));
        accountModel.setVerified(sharedPreferences.getBoolean(KEY_VERIFIED, false));
        return accountModel;
    }

    public static void save(Context context, AccountModel accountModel) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, accountModel.getToken());
        editor.putString(KEY_ID, accountModel.getId());
        editor.putString(KEY_NAME, accountModel.getName());
        editor.putString(KEY_EMAIL, accountModel.getEmail());
        editor.putString(KEY_PHONE, accountModel.getPhone());
        editor.putString(KEY_LOCALE, accountModel.getLocale());
        editor.putString(KEY_CURRENCY, accountModel.getCurrency());
        editor.putString(KEY_TIMEZONE, accountModel.getTimezone());
        editor.putString(KEY_ADDRESS, accountModel.getAddress());
        editor.putString(KEY_PHOTO_URL, accountModel.getPhotoUrl());
        editor.putString(KEY_WALLET_BALANCE, accountModel.getWallet());
        editor.putString(KEY_FOLLOWER_COUNT, accountModel.getFollowerCount());
        editor.putString(KEY_FOLLOWING_COUNT, accountModel.getFollowingCount());
        editor.putString(KEY_USER_TYPE, accountModel.getUserType());
        editor.putString(KEY_USER_ROLE_ID, accountModel.getRoleId());
        editor.putString(KEY_USER_REWARD_POINTS, accountModel.getRewardPoints());
        editor.putString(KEY_DISTRICT_CODE, accountModel.getDistrictCode());
        editor.putBoolean(KEY_VERIFIED, accountModel.isVerified());
        editor.putString(KEY_CART, "");
        editor.apply();
        i("Userdata is being saved");
    }

    public static boolean hasToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(KEY_TOKEN)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public static void setToken(Context context, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public static String getId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, "");
    }

    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public static String getEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public static String getPhone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public static String getLocale(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_LOCALE, "en");
    }

    public static void setLocale(Context context, String locale) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOCALE, locale);
        editor.apply();
    }

    public static String getCurrency(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_CURRENCY, "");
    }

    public static String getTimezone(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TIMEZONE, "");
    }

    public static String getAddress(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public static String getPhotoUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHOTO_URL, "");
    }

    public static String getWalletBalance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_WALLET_BALANCE, "");
    }

    public static String getFollowerCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FOLLOWER_COUNT, "");
    }

    public static String getFollowingCount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_FOLLOWING_COUNT, "");
    }
    public static String getDistrictCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DISTRICT_CODE, "");
    }

    public static String getUserType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TYPE, "");
    }

    public static String getKeyUserRoleId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ROLE_ID, "");
    }

    public static String getRewardPoints(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_REWARD_POINTS, "0");
    }

    public static boolean isVerified(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_VERIFIED, false);
    }

    public static boolean isComplete(Context context) {
        String phone = UserData.getPhone(context);
        String email = UserData.getEmail(context);
        String address = UserData.getAddress(context);
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(address)) {
            return false;
        }
        return true;
    }

    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void setCart(Context context, JSONObject cartData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(cartData == null){
            editor.putString(KEY_CART, "");
        } else {
            editor.putString(KEY_CART, cartData.toString());
        }
        editor.apply();
    }

    public static JSONObject getCart(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        try {
            return new JSONObject(sharedPreferences.getString(KEY_CART, ""));
        }
        catch (JSONException e) {
            return null;
        }
    }

    public static void setRetailerCart(Context context, JSONObject cartData) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(cartData == null){
            editor.putString(KEY_INVOICE_CART, "");
        } else {
            editor.putString(KEY_INVOICE_CART, cartData.toString());
        }
        editor.apply();
    }

    public static JSONObject getRetailerCart(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, Context.MODE_PRIVATE);
        try {
            return new JSONObject(sharedPreferences.getString(KEY_INVOICE_CART, ""));
        }
        catch (JSONException e) {
            return null;
        }
    }

    public static ArrayList<JSONObject> getCartItems(Context context) {
        ArrayList<JSONObject> items = null;
        try {
            JSONObject jsonObject = UserData.getCart(context);
            w(jsonObject);
            if(jsonObject != null) {
                items = new ArrayList<>();
                for(int i = 0; i < jsonObject.optJSONArray("order_items").length(); i++) {
                    items.add(jsonObject.optJSONArray("order_items").getJSONObject(i));
                }
            }
        }
        catch (JSONException e) {
            return items;
        }
        return items;
    }

    public static ArrayList<JSONObject> getRetailerCartItems(Context context) {
        ArrayList<JSONObject> items = null;
        try {
            JSONObject jsonObject = UserData.getRetailerCart(context);
            w(jsonObject);
            if(jsonObject != null) {
                items = new ArrayList<>();
               //w(getCartItemKeyByRetailer(context));
                for(int i = 0; i < jsonObject.optJSONArray(getCartItemKeyByRetailer(context)).length(); i++) {
                    items.add(jsonObject.optJSONArray(getCartItemKeyByRetailer(context)).getJSONObject(i));
                }
            }
        }
        catch (JSONException e) {
            return items;
        }
        return items;
    }
}
