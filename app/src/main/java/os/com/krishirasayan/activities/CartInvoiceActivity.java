package os.com.krishirasayan.activities;

import static os.com.krishirasayan.classes.UserData.USER_TYPE_CUSTOMER;
import static os.com.krishirasayan.classes.UserData.USER_TYPE_RETAILER;
import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.getBase64FromFileURI;
import static os.com.krishirasayan.consts.Helper.getCartItemKeyByRetailer;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_PAYMENT_METHODS;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_SET_DISTRIBUTORS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.AddressAdapter;
import os.com.krishirasayan.adapters.CartInvoiceListAdapter;
import os.com.krishirasayan.adapters.CartListAdapter;
import os.com.krishirasayan.adapters.PaymentMethodAdapter;
import os.com.krishirasayan.classes.Distributor;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.PaymentMethodModel;
import os.com.krishirasayan.models.ProductModel;

public class CartInvoiceActivity extends AppCompatActivity {

    final static int PICK_FROM_GALLERY = 90;
    Context context = this;
    RecyclerView rcvCartItem;
    TextView tvTotalCartPrice,tvTotalCartPoint, tvTotalCartItem, tvCartAddressName, tvCartAddressLocation;
    ConstraintLayout clUploadInvoice, clChooseAddress;
    TextView tvCartAddressChange;
    AddressModel selectedAddress;
    PaymentMethodModel selectedPaymentMethod;
    Button buttonCartPlaceOrder, buttonUploadInvoice, btnUpdateCart;
    String selectedInvoice;
    ImageView ivFilePreview;
    List<ProductModel> allProductList;
    ArrayList<Distributor> distributors = new ArrayList<>();
    Spinner spinnerCartDistributor;
    Spinner spiDistributors;
    Distributor selectedDistributor = null;
    String disId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_invoice);
        bindToolbar(context, R.string.nav_invoice_cart);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        spiDistributors = findViewById(R.id.spiDistributors);
        spiDistributors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDistributor = (Distributor) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        downloadDistributors();

        rcvCartItem = findViewById(R.id.rcvCartItem);
        downloadProductItems();
        selectedAddress = null;
        selectedPaymentMethod = null;
        selectedInvoice = null;
       // tvTotalCartPrice = findViewById(R.id.tvTotalCartPrice);
        tvTotalCartPoint = findViewById(R.id.tvTotalCartPoint);
        tvTotalCartItem = findViewById(R.id.tvTotalCartItem);
        clUploadInvoice = findViewById(R.id.clUploadInvoice);
        clChooseAddress = findViewById(R.id.clChooseAddress);
        ivFilePreview = findViewById(R.id.ivFilePreview);
        tvCartAddressName = findViewById(R.id.tvCartAddressName);
        tvCartAddressLocation = findViewById(R.id.tvCartAddressLocation);
        /*tvCartAddressChange = findViewById(R.id.tvCartAddressChange);
        tvCartAddressChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });*/
        switch (UserData.getUserType(context)) {
            case USER_TYPE_CUSTOMER:
                clUploadInvoice.setVisibility(View.GONE);
               // clChooseAddress.setVisibility(View.VISIBLE);
                break;
            case USER_TYPE_RETAILER:
                clUploadInvoice.setVisibility(View.VISIBLE);
               // clChooseAddress.setVisibility(View.GONE);
                break;
        }
        buttonCartPlaceOrder = findViewById(R.id.buttonCartPlaceOrder);
        buttonCartPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (UserData.getUserType(context)) {
                    case USER_TYPE_CUSTOMER:
                        showPaymentMethods();
                        break;
                    case USER_TYPE_RETAILER:
                        submitInvoice();
                        break;
                }
            }
        });
        buttonUploadInvoice = findViewById(R.id.buttonUploadInvoice);
        buttonUploadInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(CartInvoiceActivity.this).crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        btnUpdateCart = findViewById(R.id.btnUpdateCart);
        btnUpdateCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Updating your cart...", Toast.LENGTH_SHORT).show();
                VolleyService volleyService = new VolleyService(context);
                int index = 0;
                for(ProductModel productModel : allProductList) {
                    volleyService.withParamArray("product_variant_ids", index, productModel.getId());
                    volleyService.withParamArray("quantities", index++, productModel.getProductQuantity());
                }
                volleyService.withParam("token", UserData.getToken(context));
                volleyService.withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        downloadProductItems();
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                });
                volleyService.post(API_RETAILER_CART);
            }
        });
    }

    private void downloadDistributors() {
        new VolleyService(context).withParam("token", UserData.getToken(context)).withCallbacks(new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {

            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {

            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

                distributors.add(new Distributor(0, "-Select-"));
                for(int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.optJSONObject(i);
                    distributors.add(new Distributor(jsonObject.optInt("id"), jsonObject.optString("name")));
                }
                ArrayAdapter<Distributor> adapter = new ArrayAdapter<Distributor>(context, android.R.layout.simple_spinner_dropdown_item, distributors);
                spiDistributors.setAdapter(adapter);
                //spiDistributors.setSelection(adapter.getPosition(myItem));//Optional to set the selected item.*/
            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

            }
        }).get(API_SET_DISTRIBUTORS);
    }

    private void showPaymentMethods() {
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
                dialog.setContentView(R.layout.list_product_variant_layout);

                RecyclerView rvVariantList = dialog.findViewById(R.id.rvVariantList);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                rvVariantList.setLayoutManager(layoutManager);
                rvVariantList.setAdapter(new PaymentMethodAdapter(context, paymentMethodList, true, new ICallback() {
                    @Override
                    public void function(Object object) {
                        selectedPaymentMethod = (PaymentMethodModel) object;
                        dialog.dismiss();
                        placeOrder();
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
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
                        UserData.setCart(context, null);
                        UserData.setRetailerCart(context, null);
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
       // w("Users",selectedInvoice);
        w("Users",selectedDistributor.getId());

        if (selectedInvoice == null) {
            Toast.makeText(context, R.string.please_select_invoice, Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedDistributor.getId() == 0) {
            Toast.makeText(context, R.string.please_select_distributor, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!UserData.isVerified(context)) {
            Toast.makeText(context, "Your account isn't verified yet.", Toast.LENGTH_SHORT).show();
            open(context, MyProfileActivity.class);
            return;
        }
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("invoice", selectedInvoice)
                .withParam("distributor_id", String.valueOf(selectedDistributor.getId()))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        Toast.makeText(context, R.string.please_wait, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, R.string.success, Toast.LENGTH_SHORT).show();
                        UserData.setCart(context, null);
                        openAndFinish(context, InvoiceListActivity.class);
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
                allProductList = new ArrayList<>();
                tvTotalCartPoint.setText("Total Point: " + response.optString("total_points"));
                tvTotalCartItem.setText(response.optString("total_items") + " items");
                try {

                    JSONArray collection = response.optJSONArray(getCartItemKeyByRetailer(context));

                    for (int i = 0; i < collection.length(); i++) {
                        //w("Users", collection.optJSONObject(i).optJSONObject("product_variant").optJSONObject("product").optString("image_url"));
                        JSONObject item = collection.optJSONObject(i);
                        ProductModel productModel = new ProductModel(context);
                        productModel.setId(item.getString("product_variant_id"));
                        productModel.setProductName(item.getString("product_variant_name"));
                        productModel.setProductVariant(collection.optJSONObject(i).optJSONObject("product_variant").optString("variant"));
                        productModel.setProductQuantity(item.getString("quantity"));
                        productModel.setImageUrl(collection.optJSONObject(i).optJSONObject("product_variant").optJSONObject("product").optString("image_url"));
                        productModel.setPrice(item.getString("unit_price"));
                        productModel.setRetailerPrice(item.getString("unit_price"));
                        productModel.setPoint(collection.optJSONObject(i).optJSONObject("product_variant").optString("reward_points"));

                        //w("Users",productModel.getImageUrl());
                        allProductList.add(productModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setCartListRecycler(allProductList);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
            }
        }).get(API_RETAILER_CART);
    }

    private void setCartListRecycler(List<ProductModel> productList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCartItem.setLayoutManager(layoutManager);
        rcvCartItem.setAdapter(new CartInvoiceListAdapter(context, productList, false, new ICallback() {
            @Override
            public void function(Object object) {
                if(object == null) {
                    downloadProductItems();
                }
                else {
                    allProductList = (List<ProductModel>) object;
                    i("Product QTY updated");
                }
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
}