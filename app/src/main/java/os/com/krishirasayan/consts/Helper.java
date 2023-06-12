package os.com.krishirasayan.consts;


import static android.util.Base64.DEFAULT;
import static os.com.krishirasayan.consts.Config.FCM_ACTION_LOGOUT;
import static os.com.krishirasayan.consts.Config.FCM_ACTION_VIEW_HOME;
import static os.com.krishirasayan.consts.Config.FLAG_LOG_DEBUG;
import static os.com.krishirasayan.consts.Config.FLAG_LOG_ERROR;
import static os.com.krishirasayan.consts.Config.FLAG_LOG_INFO;
import static os.com.krishirasayan.consts.Config.FLAG_LOG_WTF;
import static os.com.krishirasayan.consts.Config.LOGTAG;
import static os.com.krishirasayan.consts.Config.POPUP_ALERT_ERROR;
import static os.com.krishirasayan.consts.Config.POPUP_ALERT_INFO;
import static os.com.krishirasayan.consts.Config.POPUP_ALERT_SUCCESS;
import static os.com.krishirasayan.consts.Config.POPUP_ALERT_WARNING;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;
import static os.com.krishirasayan.consts.Url.API_LOGOUT;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.AboutUsActivity;
import os.com.krishirasayan.activities.AddressListActivity;
import os.com.krishirasayan.activities.AuthActivity;
import os.com.krishirasayan.activities.CartInvoiceActivity;
import os.com.krishirasayan.activities.CartListActivity;
import os.com.krishirasayan.activities.DistributorConfirmedOrderActivity;
import os.com.krishirasayan.activities.DistributorOrderListActivity;
import os.com.krishirasayan.activities.GiftListActivity;
import os.com.krishirasayan.activities.InvoiceListActivity;
import os.com.krishirasayan.activities.LoginActivity;
import os.com.krishirasayan.activities.MainActivity;
import os.com.krishirasayan.activities.MyProfileActivity;
import os.com.krishirasayan.activities.MyRewardActivity;
import os.com.krishirasayan.activities.MyWalletActivity;
import os.com.krishirasayan.activities.NewsActivity;
import os.com.krishirasayan.activities.OrderListActivity;
import os.com.krishirasayan.activities.OtpSubmitActivity;
import os.com.krishirasayan.activities.ProductListActivity;
import os.com.krishirasayan.activities.RetailerProductListActivity;
import os.com.krishirasayan.activities.SetDistributorActivity;
import os.com.krishirasayan.activities.TermsConditionActivity;
import os.com.krishirasayan.activities.UserChooseActivity;
import os.com.krishirasayan.activities.WishlistActivity;
import os.com.krishirasayan.classes.AppData;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ISelectedMultiple;
import os.com.krishirasayan.interfaces.ISelectedSingle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Helper {
    public static void bindToolbar(Context context, int stringId) {
        AppCompatActivity activity = (AppCompatActivity) context;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }
        //For Custom Toolbar
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(context.getColor(R.color.newbasecolor));
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText(activity.getResources().getString(stringId));
        /*activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miCart:
                        open(context, CartListActivity.class);
                        break;
                    case R.id.miInvoiceCart:
                        open(context, CartInvoiceActivity.class);
                        break;
                    case R.id.nav_privacyPolicy:
                        open(context, TermsConditionActivity.class);
                        break;
                }
                return false;
            }
        });

        if (!UserData.isVerified(context)) {
            MenuItem myitem = toolbar.getMenu().findItem(R.id.miCart);
            myitem.setVisible(false);

            MenuItem myinvitem = toolbar.getMenu().findItem(R.id.miInvoiceCart);
            myinvitem.setVisible(false);
        }

            if (UserData.getKeyUserRoleId(context).equals("5")) {

                MenuItem myitem = toolbar.getMenu().findItem(R.id.miCart);
                myitem.setVisible(false);

                MenuItem myinvitem = toolbar.getMenu().findItem(R.id.miInvoiceCart);
                myinvitem.setVisible(false);
            } else if (UserData.getKeyUserRoleId(context).equals("4")) {

                MenuItem myitem = toolbar.getMenu().findItem(R.id.miCart);
                //myitem.setVisible(false);

            } else if (UserData.getKeyUserRoleId(context).equals("3")) {
                MenuItem myinvitem = toolbar.getMenu().findItem(R.id.miInvoiceCart);
                myinvitem.setVisible(false);
            }

            toolbar.setNavigationIcon(R.drawable.ic_baseline_navigate_before_24);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            });

    }

    public static void bindToolbarWithNavDraw(Context context, int stringId) {

        AppCompatActivity activity = (AppCompatActivity) context;
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().hide();
        }

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(context.getColor(R.color.newbasecolor));
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText(activity.getResources().getString(stringId));
        //toolbar.setTitle(activity.getResources().getString(stringId));
        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miCart:
                        open(context, CartListActivity.class);
                        break;
                    case R.id.miInvoiceCart:
                        open(context, CartInvoiceActivity.class);
                        break;
                    case R.id.nav_privacyPolicy:
                        open(context, TermsConditionActivity.class);
                        break;

                }
                return false;
            }
        });

        NavigationView nvDrawer;
        nvDrawer = activity.findViewById(R.id.nav_view);

        View headerView = nvDrawer.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.tvNavUserName);
        TextView tvNavUserPhone = headerView.findViewById(R.id.tvNavUserPhone);
        navUsername.setText(UserData.getName(context));
        tvNavUserPhone.setText(UserData.getPhone(context));
        /*ImageView ivBasicImage = headerView.findViewById(R.id.ivAvatarNav);
        Picasso.get().load(UserData.getPhotoUrl(context)).into(ivBasicImage);*/

        Menu nav_Menu = nvDrawer.getMenu();
        w("Users",UserData.getKeyUserRoleId(context));
        //nav_Menu.findItem(R.id.retailer).setVisible(false);

        if(UserData.getKeyUserRoleId(context).equals("4")){
            nav_Menu.setGroupVisible(R.id.customer,false);
            nav_Menu.setGroupVisible(R.id.retailer,true);

            MenuItem myitem = toolbar.getMenu().findItem(R.id.miCart);
            //myitem.setVisible(false);

        } else if(UserData.getKeyUserRoleId(context).equals("5")){
            nav_Menu.setGroupVisible(R.id.customer,false);
            nav_Menu.setGroupVisible(R.id.retailer,false);
            nav_Menu.setGroupVisible(R.id.distributor,true);

            MenuItem myitem = toolbar.getMenu().findItem(R.id.miCart);
            myitem.setVisible(false);

            MenuItem myinvitem = toolbar.getMenu().findItem(R.id.miInvoiceCart);
            myinvitem.setVisible(false);
        }else if(UserData.getKeyUserRoleId(context).equals("3")){
            nav_Menu.setGroupVisible(R.id.customer,true);
            nav_Menu.setGroupVisible(R.id.retailer,false);

            MenuItem myinvitem = toolbar.getMenu().findItem(R.id.miInvoiceCart);
            myinvitem.setVisible(false);
        }

        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home_customer:
                    case R.id.nav_home_retailer:
                    case R.id.nav_home_distributor:
                        open(context, MainActivity.class);
                        break;
                    case R.id.nav_profile_customer:
                    case R.id.nav_profile_retailer:
                    case R.id.nav_profile_distributor:
                        open(context, MyProfileActivity.class);
                        break;
                    case R.id.nav_order_customer:
                    case R.id.nav_retailer_order:
                        open(context, OrderListActivity.class);
                        break;
                    case R.id.nav_order_retailer:
                        open(context, InvoiceListActivity.class);
                        break;
                    case R.id.nav_order_assign_distributor:
                        open(context, DistributorOrderListActivity.class);
                        break;
                    case R.id.nav_order_confirm_distributor:
                        open(context, DistributorConfirmedOrderActivity.class);
                        break;
                    case R.id.nav_cart_customer:
                        open(context, CartListActivity.class);
                        break;
                    case R.id.nav_cart_retailer:
                        open(context, CartInvoiceActivity.class);
                        break;
                    case R.id.nav_address_customer:
                        open(context, AddressListActivity.class);
                        break;
                    case R.id.nav_distributor_retailer:
                        open(context, SetDistributorActivity.class);
                        break;
                    case R.id.nav_wishlist_customer:
                    case R.id.nav_wishlist_retailer:
                        open(context, WishlistActivity.class);
                        break;
                    case R.id.nav_products_customer:
                    case R.id.nav_products_retailer:
                        open(context, ProductListActivity.class);
                        break;
                    case R.id.nav_buy_products_retailer:
                        open(context, RetailerProductListActivity.class);
                        break;
                    case R.id.nav_reward_retailer:
                        open(context, MyRewardActivity.class);
                        break;
                    case R.id.nav_wallet_retailer:
                        open(context, MyWalletActivity.class);
                        break;
                    case R.id.nav_news_customer:
                    case R.id.nav_news_retailer:
                    case R.id.nav_news_distributor:
                        open(context, NewsActivity.class);
                        break;
                    case R.id.nav_logout_customer:
                    case R.id.nav_logout_retailer:
                    case R.id.nav_logout_distributor:
                        logout(context);
                        break;
                    case R.id.nav_share:
                        String message =  "Download our app from Playstore. Click here https://play.google.com/store/apps";
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, message);

                        open(context,Intent.createChooser(share, "Title of the dialog the system will open"));
                        break;
                    case R.id.nav_privacy_policy:
                        open(context, TermsConditionActivity.class);
                        break;

                }
                return false;
            }
        });


    }

    public static  void bindBottomNavigation(Context context){
        AppCompatActivity activity = (AppCompatActivity) context;
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miHome:
                        open(context, MainActivity.class);
                        break;
                    case R.id.miProfile:
                        open(context, MyProfileActivity.class);
                        break;
                    case R.id.miWishlist:
                        open(context, WishlistActivity.class);
                        break;
                    case R.id.miNews:
                        open(context, NewsActivity.class);
                        break;
                }
                return false;
            }
        });

    }

    public static void log(String logtag, String message, int flag) {
        String tag = (logtag == null) ? LOGTAG : logtag;
        switch (flag) {
            case FLAG_LOG_ERROR:
                Log.e(tag, message);
                break;
            case FLAG_LOG_WTF:
                Log.w(tag, message);
                break;
            case FLAG_LOG_INFO:
                Log.i(tag, message);
                break;
            case FLAG_LOG_DEBUG:
            default:
                Log.d(tag, message);
        }
    }

    public static void d(Object object) {
        log(null, String.valueOf(object), FLAG_LOG_DEBUG);
    }

    public static void d(String tag, Object object) {
        log(tag, String.valueOf(object), FLAG_LOG_DEBUG);
    }

    public static void e(Object object) {
        log(null, String.valueOf(object), FLAG_LOG_ERROR);
    }

    public static void e(String tag, Object object) {
        log(tag, String.valueOf(object), FLAG_LOG_ERROR);
    }

    public static void w(Object object) {
        log(null, String.valueOf(object), FLAG_LOG_WTF);
    }

    public static void w(String tag, Object object) {
        log(tag, String.valueOf(object), FLAG_LOG_WTF);
    }

    public static void i(Object object) {
        log(null, String.valueOf(object), FLAG_LOG_INFO);
    }

    public static void i(String tag, Object object) {
        log(tag, String.valueOf(object), FLAG_LOG_INFO);
    }

    public static String getStringById(Context context, int stringId) {
        String result = "";
        try {
            result = context.getResources().getString(stringId);
        } catch (Exception e) {
            e(e.toString());
        } finally {
            return result;
        }
    }

    public static void open(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public static void open(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void open(Context context, Class<?> cls, boolean finish) {
        context.startActivity(new Intent(context, cls));
        if (finish) ((Activity) context).finish();
    }

    public static void open(Context context, Intent intent, boolean finish) {
        context.startActivity(intent);
        if (finish) ((Activity) context).finish();
    }

    public static void openAndFinish(Context context, Class<?> cls) {
        open(context, cls, true);
    }

    public static void showSingleSelectDialog(Context context, Map<String, String> list, String selectedKey, ISelectedSingle iCallback) {
        ArrayList<String> keyList = new ArrayList<String>(list.keySet());
        ArrayList<String> valueList = new ArrayList<String>(list.values());

        final int[] selectedIndex = keyList.contains(selectedKey) ? new int[]{keyList.indexOf(selectedKey)} : new int[]{0};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(valueList);

        CharSequence[] itemList = valueList.toArray(new CharSequence[valueList.size()]);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Single");
        builder.setSingleChoiceItems(itemList, selectedIndex[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedIndex[0] = which;
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String key = keyList.size() > 0 ? keyList.get(selectedIndex[0]) : "";
                String value = valueList.size() > 0 ? valueList.get(selectedIndex[0]) : "";
                iCallback.onSelectedSingle(key, value);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showMultiSelectDialog(Context context, Map<String, String> list, ArrayList<String> selectedKeys, ISelectedMultiple iCallback) {
        ArrayList<String> keyList = new ArrayList<String>(list.keySet());
        ArrayList<String> valueList = new ArrayList<String>(list.values());

        final boolean[] selectedIndices = new boolean[keyList.size()];
        Arrays.fill(selectedIndices, false);

        for (String key : selectedKeys) selectedIndices[keyList.indexOf(key)] = true;

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_multichoice);
        arrayAdapter.addAll(valueList);

        CharSequence[] itemList = valueList.toArray(new CharSequence[valueList.size()]);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Select Multiple");
        builder.setMultiChoiceItems(itemList, selectedIndices, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                selectedIndices[which] = isChecked;
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> keys = new ArrayList<>();
                ArrayList<String> values = new ArrayList<>();
                for (int i = 0; i < keyList.size(); i++) {
                    if (selectedIndices[i]) {
                        keys.add(keyList.get(i));
                        values.add(valueList.get(i));
                    }
                }
                iCallback.onSelectedMultiple(keys, values);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static String getFormattedDate(String pattern) {
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        String formattingPattern = TextUtils.isEmpty(pattern) ? "dd-MM-yyy HH:mm:ss z" : pattern;
        DateFormat date = new SimpleDateFormat(formattingPattern);
        String formattedDate = date.format(currentLocalTime);

        return formattedDate;
    }

    public static void launchYouTubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void popupAlert(Context context, String message, int type) {
        Activity activity = (Activity) context;
        hideKeyboard(activity);
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        switch (type) {
            case POPUP_ALERT_INFO:
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
                break;
            case POPUP_ALERT_SUCCESS:
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
                break;
            case POPUP_ALERT_WARNING:
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
                break;
            case POPUP_ALERT_ERROR:
                Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    public static void popupInfo(Context context, String message) {
        popupAlert(context, message, POPUP_ALERT_INFO);
    }

    public static void popupInfo(Context context, int stringId) {
        String message = context.getResources().getString(stringId);
        popupAlert(context, message, POPUP_ALERT_INFO);
    }

    public static void popupSuccess(Context context, String message) {
        popupAlert(context, message, POPUP_ALERT_SUCCESS);
    }

    public static void popupSuccess(Context context, int stringId) {
        String message = context.getResources().getString(stringId);
        popupAlert(context, message, POPUP_ALERT_SUCCESS);
    }

    public static void popupWarning(Context context, String message) {
        popupAlert(context, message, POPUP_ALERT_WARNING);
    }

    public static void popupWarning(Context context, int stringId) {
        String message = context.getResources().getString(stringId);
        popupAlert(context, message, POPUP_ALERT_WARNING);
    }

    public static void popupError(Context context, String message) {
        popupAlert(context, message, POPUP_ALERT_ERROR);
    }

    public static void popupError(Context context, int stringId) {
        String message = context.getResources().getString(stringId);
        popupAlert(context, message, POPUP_ALERT_ERROR);
    }

    public static void showApiValidationError(Context context, View container, String apiErrorResponse) {
        try {
            JSONObject response = new JSONObject(apiErrorResponse);
            String message = response.optString("message", "");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            JSONObject errors = response.optJSONObject("errors");
            if (errors != null) {
                Iterator<String> keys = errors.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (errors.get(key) instanceof JSONArray) {
                        JSONArray errorStrings = errors.optJSONArray(key);
                        boolean set = setErrorAtTag(container, key, errorStrings.optString(0));
                        if (!set) {
                            popupError((Activity) context, errorStrings.optString(0));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean setErrorAtTag(View container, String tag, String error) {
        boolean set = true;
        try {
            EditText editText = container.findViewWithTag(tag);
            editText.setError(error);
        } catch (Exception e) {
            e.printStackTrace();
            set = false;
        } finally {
            return set;
        }
    }

    public static void runDelayed(Runnable runnable, int delay) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, delay);
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getBase64FromFileURI(Uri uri, Context context, boolean prependMimeType) {
        ContentResolver contentResolver = context.getContentResolver();
        String encoded = "";
        try {
            //Log.d(LOGTAG, "URI: " + uri);

            InputStream iStream = contentResolver.openInputStream(uri);
            byte[] inputData = getBytes(iStream);
            long fileSizeInBytes = inputData.length;
            encoded = Base64.encodeToString(inputData, DEFAULT);

            if(prependMimeType) {
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String extension = mime.getExtensionFromMimeType(contentResolver.getType(uri));
                String mimetype = contentResolver.getType(uri);
                /*if (mimetype == null) {
                    encoded = "data:image/jpeg;base64," + encoded;
                } else {
                    encoded = "data:" + mimetype + ";base64," + encoded;
                }*/
               encoded = String.format("%s%s", "data:image/jpeg;base64,", encoded);
            }

            encoded=encoded.replace("\n","");

           // Log.d(LOGTAG, "FileSize: " + fileSizeInBytes);
            //Log.d(LOGTAG, "Base64: " + encoded);
        } catch (IOException e) {
            e.printStackTrace();
            //Log.e(LOGTAG, e.getMessage());
        }
        return encoded;
    }

    public static Map<String, String> coordsToAddress(Context context, LatLng coords) {
        Geocoder geocoder;
        List<Address> addresses;
        HashMap<String,String> data = new HashMap<>();
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(coords.latitude, coords.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            data.put("address", address);
            data.put("city", city);
            data.put("state", state);
            data.put("country", country);
            data.put("postalCode", postalCode);
            data.put("knownName", knownName);

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }


    public static LatLng getLatLngFromString(String coords) {
        LatLng latlng = null;
        try {
            String[] latlong = coords.split(",");
            latlng = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        } catch (Exception e) {
            Log.d("UTILITY_LOG", "String: (" + coords + ") could not be split into coordinates");
            Log.d("UTILITY_LOG", e.toString());
        }
        return latlng;
    }

    public static Map<String, String> getCallingCodes() {
        List<String> data = new ArrayList<>();
        data.add("+1");
        data.add("+500");
        data.add("+501");
        data.add("+502");
        data.add("+503");
        data.add("+504");
        data.add("+505");
        data.add("+506");
        data.add("+507");
        data.add("+508");
        data.add("+509");
        data.add("+51");
        data.add("+52");
        data.add("+53");
        data.add("+54");
        data.add("+55");
        data.add("+56");
        data.add("+58");
        data.add("+590");
        data.add("+597");
        data.add("+592");
        data.add("+593");
        data.add("+594");
        data.add("+595");
        data.add("+596");
        data.add("+597");
        data.add("+598");
        data.add("+90");
        data.add("+91");
        data.add("+92");
        data.add("+93");
        data.add("+94");
        data.add("+95");
        data.add("+95");
        data.add("+960");
        data.add("+961");
        data.add("+962");
        data.add("+963");
        data.add("+964");
        data.add("+965");
        data.add("+966");
        data.add("+967");
        data.add("+968");
        data.add("+970");
        data.add("+971");
        data.add("+972");
        data.add("+973");
        data.add("+974");
        data.add("+975");
        data.add("+976");
        data.add("+977");
        data.add("+98");
        data.add("+992");
        data.add("+993");
        data.add("+994");
        data.add("+995");
        data.add("+996");
        data.add("+997");
        data.add("+998");
        data.add("+212");

        Map<String, String> map = new HashMap<>();
        for(String s : data) {
            map.put(s, s);
        }
        return map;
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        wm.updateViewLayout(container, p);
    }

    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getCartUrlByUserType(Context context) {
        switch(UserData.getUserType(context)) {
            case "customer":
                return API_CUSTOMER_CART;
            case "retailer":
                return API_RETAILER_CART;
            default:
                return "";
        }
    }

    public static String getCartItemKeyByUserType(Context context) {
        return "order_items";
    }

    public static String getCartItemKeyByRetailer(Context context) {
        return "invoice_items";
    }

    public static Intent getNotificationIntent(String action, String id, Context context) {
        Log.d(LOGTAG, "TRIGGER: CommonUtil.getNotificationIntent");
        Intent intent = null;
        if (!TextUtils.isEmpty(action)) {
            Log.d(LOGTAG, "GOT ACTION: " + action);
            Log.d(LOGTAG, "GOT ID: " + id);
            try {
                switch (Objects.requireNonNull(action)) {
                    case FCM_ACTION_LOGOUT:
                        logout(context);
                        break;
                    case FCM_ACTION_VIEW_HOME:
                        String token = UserData.getToken(context);
                        intent = new Intent(context, AuthActivity.class);
                        intent.putExtra("token", token);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return intent;
    }

    public static void saveFcmToken(Context context, String token) {
        AppData.setFcmToken(context, token);
        i("Saved FCM Token: " + token);
    }

    public static void logout(Context context) {
        new VolleyService(context).withParam("token", UserData.getToken((context))).get(API_LOGOUT);
        googleSignOut(context);
        facebookSignOut();
        UserData.clear(context);
        openAndFinish(context, UserChooseActivity.class);
    }

    public static GoogleSignInOptions googleSignInOptions(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_server_client_id))
                .requestEmail().build();
    }

    public static GoogleSignInClient googleSignInClient(Context context) {
        // Build a GoogleSignInClient with the options specified by gso.
        return GoogleSignIn.getClient(context, googleSignInOptions(context));
    }

    public static void googleSignOut(Context context) {
        googleSignInClient(context).signOut();
    }

    public static void facebookSignOut() {
        LoginManager.getInstance().logOut();
    }
}
