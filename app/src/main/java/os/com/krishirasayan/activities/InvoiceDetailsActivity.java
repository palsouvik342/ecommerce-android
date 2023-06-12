package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.InvoiceOrderDetailsAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;

public class InvoiceDetailsActivity extends AppCompatActivity {

    Context context = this;
    TextView tvOrderIdValue, tvOrderStatusValue, tvOrderPaymentValue, tvOrderTotalItemValue, tvOrderTotalBillValue, tvOrderAddressName, tvOrderAddressLocation, tvOrderDistributorValue;
    String orderId, orderNum;
    Button buttonOrderAskQuestion;
    LinearLayout llOrderProductItem;
    RecyclerView rcvInvoiceOrderedProductItem;
    TextView ivOrderitemProductTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        bindToolbar(context,R.string.order_details);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,InvoiceListActivity.class);
    }

    private void bindReferences() {
        tvOrderIdValue = findViewById(R.id.tvOrderIdValue);
        tvOrderStatusValue = findViewById(R.id.tvOrderStatusValue);
        tvOrderPaymentValue = findViewById(R.id.tvOrderPaymentValue);
        tvOrderTotalItemValue = findViewById(R.id.tvOrderTotalItemValue);
        tvOrderTotalBillValue = findViewById(R.id.tvOrderTotalBillValue);
        tvOrderAddressName = findViewById(R.id.tvOrderAddressName);
        tvOrderAddressLocation = findViewById(R.id.tvOrderAddressLocation);
        llOrderProductItem = findViewById(R.id.llOrderProductItem);
        ivOrderitemProductTitle = findViewById(R.id.ivOrderitemProductTitle);
        tvOrderDistributorValue = findViewById(R.id.tvOrderDistributorValue);
        rcvInvoiceOrderedProductItem = findViewById(R.id.rcvInvoiceOrderedProductItem);
        downloadOrderItems();

        buttonOrderAskQuestion = findViewById(R.id.buttonOrderAskQuestion);
        buttonOrderAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });
    }

    private void downloadOrderItems() {
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
                            orderNum = response.optString("order_number");
                            tvOrderStatusValue.setText(response.optString("status"));
                            if(response.optInt("payment_method_id") == 1) {
                                tvOrderPaymentValue.setText("Cash on Delivery");
                            }
                            tvOrderTotalItemValue.setText(response.optString("total_items"));
                            tvOrderTotalBillValue.setText(response.optString("total_points"));
                            tvOrderDistributorValue.setText(response.optString("distributor_name"));
                            d("Users",response.optJSONArray("invoice_items"));

                            try {
                                JSONArray collection = response.optJSONArray("invoice_items");
                                for(int i = 0; i < collection.length(); i++) {
                                    JSONObject orderItem = collection.optJSONObject(i);
                                    OrderModel orderModel = new OrderModel(context);
                                    orderModel.setOrderProductName(orderItem.getString("product_variant_name"));
                                    orderModel.setOrderProductPrice("₹" + orderItem.getString("unit_price"));
                                    orderModel.setOrderProductPoint(orderItem.optJSONObject("product_variant").optString("reward_points"));
                                    orderModel.setOrderProductQty(orderItem.getString("quantity"));
                                    orderModel.setOrderProductVariant(collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    //w("Users",collection.optJSONObject(i).optJSONObject("product_variant").optJSONArray("values").optJSONObject(0).optString("name"));
                                    orderModel.setImageUrl(orderItem.optJSONObject("product_variant").optJSONObject("product").optString("image_url"));
                                    orderList.add(orderModel);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            setInvoiceOrderListRecycler(orderList);
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONArray response) {

                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        }

                    }).get(API_RETAILER_ORDERS + "/" + orderId);
        }
    }

    private void setInvoiceOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvInvoiceOrderedProductItem.setLayoutManager(layoutManager);
        rcvInvoiceOrderedProductItem.setAdapter(new InvoiceOrderDetailsAdapter(context, orderList));
    }

    private void openWhatsApp(){
        try {
            String text = "I have query for Order no: " + orderNum;
            String toNumber = "916292209495";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}