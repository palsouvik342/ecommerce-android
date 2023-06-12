package os.com.krishirasayan.activities;

import static os.com.krishirasayan.classes.UserData.USER_TYPE_CUSTOMER;
import static os.com.krishirasayan.classes.UserData.USER_TYPE_RETAILER;
import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.getBase64FromFileURI;
import static os.com.krishirasayan.consts.Helper.getCartItemKeyByRetailer;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.popupInfo;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_PAYMENT_METHODS;
import static os.com.krishirasayan.consts.Url.API_READ_PROFILE;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.AddressAdapter;
import os.com.krishirasayan.adapters.CartListAdapter;
import os.com.krishirasayan.adapters.PaymentMethodAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.PaymentMethodModel;
import os.com.krishirasayan.models.ProductModel;

public class CartListActivity extends AppCompatActivity {

    final static int PICK_FROM_GALLERY = 90;
    Context context = this;
    RecyclerView rcvCartItem;
    TextView tvTotalCartPrice,tvTotalCartPoint, tvTotalCartItem, tvCartAddressName, tvCartAddressLocation;
    ConstraintLayout clChooseAddress, clSelectWallet;
    TextView tvCartAddressChange, tvAvailableBalance, tvPayAmount;
    AddressModel selectedAddress;
    PaymentMethodModel selectedPaymentMethod;
    Button buttonCartPlaceOrder;
    String selectedInvoice, walletChecked;
    Integer totalCart, availableWallet, payAfterWallet;
    ImageView ivFilePreview;
    CheckBox cbSelectWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        bindToolbar(context, R.string.nav_cart);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rcvCartItem = findViewById(R.id.rcvCartItem);
        tvAvailableBalance = findViewById(R.id.tvAvailableBalance);
        downloadProductItems();
        downloadWalletBalance();
        selectedAddress = null;
        selectedPaymentMethod = null;
        selectedInvoice = null;
        tvTotalCartPrice = findViewById(R.id.tvTotalCartPrice);
        tvTotalCartPoint = findViewById(R.id.tvTotalCartPoint);
        tvTotalCartItem = findViewById(R.id.tvTotalCartItem);
        clSelectWallet = findViewById(R.id.clSelectWallet);
        clChooseAddress = findViewById(R.id.clChooseAddress);
        ivFilePreview = findViewById(R.id.ivFilePreview);
        tvCartAddressName = findViewById(R.id.tvCartAddressName);
        tvCartAddressLocation = findViewById(R.id.tvCartAddressLocation);
        tvPayAmount = findViewById(R.id.tvPayAmount);
        tvCartAddressChange = findViewById(R.id.tvCartAddressChange);
        tvCartAddressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });
        buttonCartPlaceOrder = findViewById(R.id.buttonCartPlaceOrder);
        buttonCartPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaymentMethods();
            }
        });
    }

    private void showPaymentMethods() {
        cbSelectWallet = findViewById(R.id.cbSelectWallet);
        if(cbSelectWallet.isChecked()){
            walletChecked = "1";
        }else {
            walletChecked = "0";
        }
        if (selectedAddress == null) {
            Toast.makeText(context, R.string.please_select_address, Toast.LENGTH_SHORT).show();
            return;
        }

        new VolleyService(context).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {
                ArrayList<PaymentMethodModel> paymentMethodList = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.optJSONObject(i);
                    paymentMethodList.add(new PaymentMethodModel(jsonObject.optString("id"), jsonObject.optString("name")));
                }

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.list_payment_gateway_layout);

                RecyclerView rvVariantList = dialog.findViewById(R.id.rvVariantList);
                tvPayAmount = dialog.findViewById(R.id.tvPayAmount);
                if(cbSelectWallet.isChecked()){
                    payAfterWallet = totalCart - (availableWallet > totalCart ? totalCart : availableWallet);
                    tvPayAmount.setText(String.valueOf(payAfterWallet));
                }else {
                    tvPayAmount.setText(String.valueOf(totalCart));
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                rvVariantList.setLayoutManager(layoutManager);
                rvVariantList.setAdapter(new PaymentMethodAdapter(context, paymentMethodList, true, new ICallback() {
                    @Override
                    public void function(Object object) {
                        selectedPaymentMethod = (PaymentMethodModel) object;
                        if(selectedPaymentMethod.getId().equals("1")){
                            dialog.dismiss();
                            placeOrder();
                        } else if (selectedPaymentMethod.getId().equals("2")){
                            Toast.makeText(context, "Online payment is coming soon.. Please choose Cash on Delivery option", Toast.LENGTH_SHORT).show();
                        }
                        //dialog.dismiss();
                        //placeOrder();
                    }
                }));
                rvVariantList.scheduleLayoutAnimation();

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
            }
        }).get(API_PAYMENT_METHODS);
    }

    private void placeOrder() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("payment_method_id", selectedPaymentMethod.getId())
                .withParam("delivery_address_id", selectedAddress.getId())
                .withParam("billing_address_id", selectedAddress.getId())
                .withParam("wallet_used", walletChecked)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
                        UserData.setCart(context, null);
                        openAndFinish(context, OrderListActivity.class);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }
                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show();
                    }
                }).post(API_CUSTOMER_ORDERS);

    }

    private void submitInvoice() {

        if (selectedInvoice == null) {
            Toast.makeText(context, R.string.please_select_invoice, Toast.LENGTH_SHORT).show();
            return;
        }
        //w(selectedInvoice);
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("invoice", selectedInvoice)
                .withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
                openAndFinish(context, InvoiceDetailsActivity.class);

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                Toast.makeText(context, R.string.failure, Toast.LENGTH_SHORT).show();
            }
        }).post(API_RETAILER_ORDERS);
    }

    private void showAddressDialog() {
        new VolleyService(context).withParam("token", UserData.getToken(context)).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {
                if(response.length() != 0){


                    List<AddressModel> addressList = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {

                        AddressModel addressModel = new AddressModel(context);
                        addressModel.setId(response.optJSONObject(i).optString("id"));
                        addressModel.setContactName(response.optJSONObject(i).optString("contact_name"));
                        addressModel.setContactNumber(response.optJSONObject(i).optString("contact_number"));
                        addressModel.setLine1(response.optJSONObject(i).optString("line1"));
                        addressModel.setLine2(response.optJSONObject(i).optString("line2"));
                        addressModel.setPostalCode(response.optJSONObject(i).optString("postal_code"));
                        addressModel.setCity(response.optJSONObject(i).optString("city"));
                        addressModel.setState(response.optJSONObject(i).optString("state"));
                        addressModel.setCountry(response.optJSONObject(i).optString("country"));
                        addressList.add(addressModel);
                    }

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.list_product_variant_layout);

                    RecyclerView rvVariantList = dialog.findViewById(R.id.rvVariantList);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                    rvVariantList.setLayoutManager(layoutManager);
                    rvVariantList.setAdapter(new AddressAdapter(context, addressList, true, new ICallback() {
                        @Override
                        public void function(Object object) {
                            selectedAddress = (AddressModel) object;
                            displaySelectedAddress();
                            dialog.dismiss();
                        }
                    }));
                    rvVariantList.scheduleLayoutAnimation();

                    dialog.show();
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                }else {
                    //popupInfo(context,"Please add address from address manager");
                    openAndFinish(context,AddressAddActivity.class);
                }
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
            }
        }).get(API_ADDRESS_List);
    }

    private void displaySelectedAddress() {
        tvCartAddressName.setText(selectedAddress.getContactName());
        tvCartAddressLocation.setText(selectedAddress.getLine1());
    }

    private void downloadProductItems() {
        new VolleyService(context).withParam("token", UserData.getToken(context)).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                List<ProductModel> productList = new ArrayList<>();
                tvTotalCartPrice.setText("Total Order:  ₹" + response.optString("total_price"));
                totalCart = response.optInt("total_price");
                //tvTotalCartPoint.setText("Total Point: " + response.optString("total_points"));
                tvTotalCartItem.setText(response.optString("total_items") + " items");
                try {

                    JSONArray collection = response.optJSONArray("order_items");

                    for (int i = 0; i < collection.length(); i++) {
                        w("Users", collection.optJSONObject(i).optJSONObject("product_variant").optJSONObject("product").optString("image_url"));
                        JSONObject item = collection.optJSONObject(i);
                        ProductModel productModel = new ProductModel(context);
                        productModel.setId(item.getString("product_variant_id"));
                        productModel.setProductName(item.getString("product_variant_name"));
                        productModel.setProductQuantity(item.getString("quantity"));
                        productModel.setImageUrl(collection.optJSONObject(i).optJSONObject("product_variant").optJSONObject("product").optString("image_url"));
                        productModel.setPrice(item.getString("unit_price"));
                        productModel.setRetailerPrice(item.getString("unit_price"));
                        productModel.setPoint(item.getString("quantity"));
                        productModel.setProductVariant(collection.optJSONObject(i).optJSONObject("product_variant").optString("variant"));


                        //w("Users",productModel.getImageUrl());
                        productList.add(productModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setCartListRecycler(productList);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
            }
        }).get(API_CUSTOMER_CART);
    }

    private void setCartListRecycler(List<ProductModel> productList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCartItem.setLayoutManager(layoutManager);
        rcvCartItem.setAdapter(new CartListAdapter(context, productList, false, new ICallback() {
            @Override
            public void function(Object object) {
                downloadProductItems();
            }
        }));
    }

    public void openFolder() {
//        Intent intent = new Intent();
//        intent.setType("*/*");
////        intent.setType("file/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.putExtra("return-data", true);
//        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        ivFilePreview.setImageURI(uri);
        selectedInvoice = getBase64FromFileURI(uri, context, true);
        w("Users", selectedInvoice);
    }

    private void downloadWalletBalance(){
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        tvAvailableBalance.setText("Available : ₹ " + response.optString("wallet_amount"));
                        availableWallet = response.optInt("wallet_amount");
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_READ_PROFILE);
    }
}