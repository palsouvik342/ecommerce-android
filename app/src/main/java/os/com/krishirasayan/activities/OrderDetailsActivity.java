package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ALL_ORDERS;
import static os.com.krishirasayan.consts.Url.API_ASSIGNED_ORDER;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDER_REVIEW;
import static os.com.krishirasayan.consts.Url.API_DISTRIBUTORS_ORDER;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.adapters.OrderDetailsAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;
import os.com.krishirasayan.models.ProductModel;

public class OrderDetailsActivity extends AppCompatActivity {

    Context context = this;
    TextView tvAddressClosePopup, tvOrderIdValue, tvOrderStatusValue, tvOrderPaymentValue, tvOrderTotalItemValue, tvOrderTotalBillValue, tvOrderAddressName, tvOrderAddressLocation;
    String orderStatus, userMobileNo, orderId, USER_ROLE_ID, confirmOrder;
    Button buttonReviewYourOrder, buttonOrderReceived, buttonReviewAlreadySubmitted, buttonAddressPersonCall;
    Button buttonCancelOrder, buttonOrderApprove, buttonOrderDecine, buttonOrderDelivered, buttonOrderDeliverConfirm, buttonOrderDeliverNotConfirm;
    LinearLayout llOrderProductItem;
    RecyclerView rcvOrderedProductItem;
    EditText etOrderExperience;
    TextView tvCancelByValue, ivOrderitemProductTitle, tvClosePopup, tvAddressPersonName, tvAddressPersonMObile, tvFullAddress;
    ConstraintLayout clCancelledBy, clOrderAddressPopup, clChooseAddress, clOrderReviewPopup, clDistributAcceptDecline, clDistributorOrderDelivered, clUserOrderReview, clOrderDeliveryConfirmation;

    boolean assignOrder = false;
    boolean orderCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        bindToolbar(context,R.string.order_details);
        bindReferences();
    }

    @Override
    public void onBackPressed() {

        if(USER_ROLE_ID.equals("5")) {
            if (getIntent().hasExtra("assigned_order")){
                open(context,DistributorOrderListActivity.class);
            }else{
                open(context,DistributorConfirmedOrderActivity.class);
            }
        }else{
            open(context,OrderListActivity.class);
        }
    }

    private void bindReferences() {
        clOrderAddressPopup = findViewById(R.id.clOrderAddressPopup);
        clOrderAddressPopup.setVisibility(View.GONE);

        clCancelledBy = findViewById(R.id.clCancelledBy);
        clCancelledBy.setVisibility(View.GONE);

        tvAddressPersonName = findViewById(R.id.tvAddressPersonName);
        tvAddressPersonMObile = findViewById(R.id.tvAddressPersonMObile);
        tvFullAddress = findViewById(R.id.tvFullAddress);
        tvOrderIdValue = findViewById(R.id.tvOrderIdValue);
        tvOrderStatusValue = findViewById(R.id.tvOrderStatusValue);
        tvOrderPaymentValue = findViewById(R.id.tvOrderPaymentValue);
        tvOrderTotalItemValue = findViewById(R.id.tvOrderTotalItemValue);
        tvOrderTotalBillValue = findViewById(R.id.tvOrderTotalBillValue);
        tvOrderAddressName = findViewById(R.id.tvOrderAddressName);
        tvOrderAddressLocation = findViewById(R.id.tvOrderAddressLocation);
        llOrderProductItem = findViewById(R.id.llOrderProductItem);
        ivOrderitemProductTitle = findViewById(R.id.ivOrderitemProductTitle);
        rcvOrderedProductItem = findViewById(R.id.rcvOrderedProductItem);
        etOrderExperience = findViewById(R.id.etOrderExperience);
        tvCancelByValue = findViewById(R.id.tvCancelByValue);

        clDistributAcceptDecline = findViewById(R.id.clDistributAcceptDecline);
        clDistributAcceptDecline.setVisibility(View.GONE);

        clDistributorOrderDelivered = findViewById(R.id.clDistributorOrderDelivered);
        clDistributorOrderDelivered.setVisibility(View.GONE);

        clUserOrderReview = findViewById(R.id.clUserOrderReview);
        clUserOrderReview.setVisibility(View.GONE);

        clOrderDeliveryConfirmation = findViewById(R.id.clOrderDeliveryConfirmation);
        clOrderDeliveryConfirmation.setVisibility(View.GONE);

        clOrderReviewPopup = findViewById(R.id.clOrderReviewPopup);
        clOrderReviewPopup.setVisibility(View.GONE);

        buttonCancelOrder = findViewById(R.id.buttonCancelOrder);
        buttonCancelOrder.setVisibility(View.GONE);

        buttonReviewAlreadySubmitted = findViewById(R.id.buttonReviewAlreadySubmitted);
        buttonReviewAlreadySubmitted.setVisibility(View.GONE);
        buttonReviewAlreadySubmitted.setClickable(false);

        tvAddressClosePopup = findViewById(R.id.tvAddressClosePopup);
        tvAddressClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderAddressPopup.setVisibility(View.GONE);
            }
        });

        clChooseAddress = findViewById(R.id.clChooseAddress);
        clChooseAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderAddressPopup.setVisibility(View.VISIBLE);
            }
        });

        buttonReviewAlreadySubmitted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clOrderReviewPopup.setAlpha(1);
            }
        });

        buttonCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               cancelOrder();
            }
        });

        buttonReviewYourOrder = findViewById(R.id.buttonReviewYourOrder);
        buttonReviewYourOrder.setVisibility(View.GONE);
        buttonReviewYourOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderReviewPopup.setVisibility(View.VISIBLE);
            }
        });

        buttonOrderReceived = findViewById(R.id.buttonOrderReceived);
        buttonOrderReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });

        tvClosePopup = findViewById(R.id.tvClosePopup);
        tvClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderReviewPopup.setVisibility(View.GONE);
            }
        });

        buttonAddressPersonCall = findViewById(R.id.buttonAddressPersonCall);
        buttonAddressPersonCall.setVisibility(View.GONE);
        buttonAddressPersonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ userMobileNo));
                startActivity(callIntent);
            }
        });

        buttonOrderApprove = findViewById(R.id.buttonOrderApprove);
        buttonOrderApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorOrderApproved();
            }
        });

        buttonOrderDecine = findViewById(R.id.buttonOrderDecine);
        buttonOrderDecine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorOrderDecline();
            }
        });

        buttonOrderDelivered = findViewById(R.id.buttonOrderDelivered);
        buttonOrderDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderDeliveryConfirmation.setVisibility(View.VISIBLE);
            }
        });

        buttonOrderDeliverConfirm = findViewById(R.id.buttonOrderDeliverConfirm);
        buttonOrderDeliverConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distributorOrderDeliver();
            }
        });

        buttonOrderDeliverNotConfirm = findViewById(R.id.buttonOrderDeliverNotConfirm);
        buttonOrderDeliverNotConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clOrderDeliveryConfirmation.setVisibility(View.GONE);
            }
        });

        USER_ROLE_ID=UserData.getKeyUserRoleId(context);
        if(USER_ROLE_ID.equals("5")) {
            downloadDistributorOrderItems();
            buttonAddressPersonCall.setVisibility(View.VISIBLE);
            buttonCancelOrder.setVisibility(View.GONE);
        }else{
            downloadOrderItems();
        }

        if (getIntent().hasExtra("assigned_order")){
            assignOrder = getIntent().getBooleanExtra("assigned_order",false);
        }

    }

    private void downloadOrderItems() {
        clUserOrderReview.setVisibility(View.VISIBLE);
        if (getIntent().hasExtra("order_id")) {
            orderId = getIntent().getStringExtra("order_id");
            new VolleyService(context)
                    .withParam("token", UserData.getToken(context))
                    .withCallbacks(new IVolleyResult() {
                        @Override
                        public void notifyRequestQueued(String requestName) {
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONObject response) {
                            List<OrderModel> orderList = new ArrayList<>();
                            tvOrderIdValue.setText("#" + response.optString("order_number"));
                            tvOrderStatusValue.setText(response.optString("status"));
                            orderCancelled = response.optBoolean("can_be_cancelled");
                            if(response.optInt("payment_method_id") == 1) {
                                tvOrderPaymentValue.setText("Cash on Delivery");
                            }
                            if(response.optString("status").equals("DELIVERED")){
                                if(response.optBoolean("user_received")) {
                                    buttonReviewYourOrder.setVisibility(View.GONE);
                                    buttonReviewAlreadySubmitted.setVisibility(View.VISIBLE);
                                } else {
                                    buttonReviewYourOrder.setVisibility(View.VISIBLE);
                                }
                            }

                            if(response.optString("status").equals("CANCELLED")){
                                tvCancelByValue.setText(response.optString("cancelled_by"));
                                clCancelledBy.setVisibility(View.VISIBLE);
                            }


                            if(orderCancelled){
                                buttonCancelOrder.setVisibility(View.VISIBLE);
                            }

                            tvOrderTotalItemValue.setText(response.optString("total_items"));
                            tvOrderTotalBillValue.setText("₹" + response.optString("total_price"));

                            if(response.optJSONObject("delivery_address").equals(null)) {
                                w(response.optJSONObject("delivery_address"));
                                tvOrderAddressName.setText("User Name");
                                tvOrderAddressLocation.setText("No address found!");
                                tvAddressPersonName.setText("User Name");
                                tvAddressPersonMObile.setText("91 xxx-xxx-xxxx");
                                userMobileNo = "";
                                tvFullAddress.setText("No address found!");
                            }
                            else{
                                JSONObject addr = response.optJSONObject("delivery_address");
                                tvOrderAddressName.setText(addr.optString("contact_name"));
                                tvOrderAddressLocation.setText(addr.optString("line1") + ", " + addr.optString("line2") + ", " + addr.optString("city") + ", " + addr.optString("postal_code") + ", " + addr.optString("state") + ", " + addr.optString("country"));

                                tvAddressPersonName.setText(addr.optString("contact_name"));
                                tvAddressPersonMObile.setText(addr.optString("contact_number"));
                                userMobileNo = addr.optString("contact_number");
                                tvFullAddress.setText(addr.optString("line1") + ", " + addr.optString("line2") + ", " + addr.optString("city") + ", " + addr.optString("postal_code") + ", " + addr.optString("state") + ", " + addr.optString("country"));
                            }
                            try {
                                JSONArray collection = response.optJSONArray("order_items");
                                for(int i = 0; i < collection.length(); i++) {

                                    JSONObject orderItem = collection.optJSONObject(i);
                                    OrderModel orderModel = new OrderModel(context);
                                    orderModel.setOrderProductName(orderItem.getString("product_variant_name"));
                                    orderModel.setOrderProductPrice("₹" + orderItem.getString("unit_price"));
                                    orderModel.setOrderProductQty(orderItem.getString("quantity"));
                                    orderModel.setOrderProductVariant(collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    //w("Users",collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    orderModel.setImageUrl(orderItem.getString("product_image_url"));
                                    orderList.add(orderModel);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setOrderListRecycler(orderList);
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONArray response) {
                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        }

                    }).get(API_CUSTOMER_ORDERS + "/" + orderId);
        }
    }

    private void downloadDistributorOrderItems() {

        if (getIntent().hasExtra("order_id")) {
            orderId = getIntent().getStringExtra("order_id");
            confirmOrder = getIntent().getStringExtra("confirmed_order");

            new VolleyService(context)
                    .withParam("token", UserData.getToken(context))
                    .withCallbacks(new IVolleyResult() {
                        @Override
                        public void notifyRequestQueued(String requestName) {
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONObject response) {

                            List<OrderModel> orderList = new ArrayList<>();
                            tvOrderIdValue.setText("#" + response.optString("order_number"));
                            tvOrderStatusValue.setText(response.optString("status"));
                            orderStatus = response.optString("status");
                            tvOrderTotalItemValue.setText(response.optString("total_items"));
                            tvOrderTotalBillValue.setText("₹" + response.optString("total_price"));

                            if(response.optJSONObject("delivery_address") == null) {
                                w(response.optJSONObject("delivery_address"));
                                tvOrderAddressName.setText("Customer Name");
                                tvOrderAddressLocation.setText("No address found!");
                                tvAddressPersonName.setText("Customer Name");
                                tvAddressPersonMObile.setText("91 xxx-xxx-xxxx");
                                userMobileNo = "";
                                tvFullAddress.setText("No address found!");
                            }
                            else{

                                JSONObject addr = response.optJSONObject("delivery_address");
                                tvOrderAddressName.setText(addr.optString("contact_name"));
                                tvOrderAddressLocation.setText(addr.optString("line1") + ", " + addr.optString("line2") + ", " + addr.optString("city") + ", " + addr.optString("postal_code") + ", " + addr.optString("state") + ", " + addr.optString("country"));

                                tvAddressPersonName.setText(addr.optString("contact_name"));
                                tvAddressPersonMObile.setText(addr.optString("contact_number"));
                                userMobileNo = addr.optString("contact_number");
                                tvFullAddress.setText(addr.optString("line1") + ", " + addr.optString("line2") + ", " + addr.optString("city") + ", " + addr.optString("postal_code") + ", " + addr.optString("state") + ", " + addr.optString("country"));

                            }


                            if(response.optInt("payment_method_id") == 1) {
                                tvOrderPaymentValue.setText("Cash on Delivery");
                            }
                            if(assignOrder) {
                                clDistributAcceptDecline.setVisibility(View.VISIBLE);
                                clDistributorOrderDelivered.setVisibility(View.GONE);
                            }
                            else{
                                clDistributorOrderDelivered.setVisibility(View.VISIBLE);
                            }
                            if(response.optInt("approved")==1){
                                clDistributorOrderDelivered.setVisibility(View.GONE);
                            }

                            try {
                                JSONArray collection = response.optJSONArray("order_items");
                                for(int i = 0; i < collection.length(); i++) {

                                    JSONObject orderItem = collection.optJSONObject(i);
                                    OrderModel orderModel = new OrderModel(context);
                                    orderModel.setOrderProductName(orderItem.getString("product_variant_name"));
                                    //orderModel.setOrderProductPrice("₹" + orderItem.getString("unit_price"));
                                    orderModel.setOrderProductPrice("");
                                    orderModel.setOrderProductPoint("");
                                    orderModel.setOrderProductQty(orderItem.getString("quantity"));
                                    orderModel.setOrderProductVariant(collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    //w("Users",collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    orderModel.setImageUrl(orderItem.getString("product_image_url"));
                                    orderList.add(orderModel);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setOrderListRecycler(orderList);
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONArray response) {
                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        }

                    }).get(API_ALL_ORDERS + "/" + orderId);
        }
    }

    private void distributorOrderApproved() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "Order accepted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DistributorConfirmedOrderActivity.class);
                        open(context, intent, true);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context,responseData);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }).put(API_ASSIGNED_ORDER + "/" + orderId + "/accept");
    }

    private void distributorOrderDecline() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "Order declined", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DistributorOrderListActivity.class);
                        open(context, intent, true);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context,responseData);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }).put(API_ASSIGNED_ORDER + "/" + orderId + "/decline");
    }

    private void distributorOrderDeliver() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "Order successfully delivered", Toast.LENGTH_SHORT).show();
                        clOrderDeliveryConfirmation.setVisibility(View.GONE);
                        downloadDistributorOrderItems();
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context,responseData);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }).put(API_DISTRIBUTORS_ORDER + "/" + orderId + "/deliver");
    }

    private void submitReview() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("order_id", orderId)
                .withParam("rating", "5")
                .withParam("review", etOrderExperience.getText().toString())
                .withParam("received", "1")
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "Thank you for your Review", Toast.LENGTH_SHORT).show();
                        buttonReviewYourOrder.setVisibility(View.GONE);
                        buttonReviewAlreadySubmitted.setVisibility(View.VISIBLE);
                        clOrderReviewPopup.setVisibility(View.GONE);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context,responseData);
                        clOrderReviewPopup.setVisibility(View.GONE);
                    }

                }).post(API_CUSTOMER_ORDER_REVIEW);
    }

    private void cancelOrder() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "your order has been cancelled successfully", Toast.LENGTH_SHORT).show();
                        buttonCancelOrder.setVisibility(View.GONE);
                        open(context,OrderListActivity.class);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        popupError(context,responseData);
                    }

                }).put(API_CUSTOMER_ORDERS + "/" + orderId + "/cancel");
    }

    private void setOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvOrderedProductItem.setLayoutManager(layoutManager);
        rcvOrderedProductItem.setAdapter(new OrderDetailsAdapter(context, orderList));
    }

}