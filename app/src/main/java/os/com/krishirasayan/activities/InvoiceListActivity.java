package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.InvoiceAdapter;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;

public class InvoiceListActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rvInvoiceOrderList;
    ProgressBar pbInvoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        bindToolbar(context,R.string.my_invoices);
        bindReferences();
    }

    private void bindReferences() {
        rvInvoiceOrderList = findViewById(R.id.rvInvoiceOrderList);
        pbInvoiceList = findViewById(R.id.pbInvoiceList);
        downloadInvoiceOrderItems();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void downloadInvoiceOrderItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        pbInvoiceList.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        pbInvoiceList.setVisibility(View.GONE);
                        List<OrderModel> orderList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            OrderModel orderModel = new OrderModel(context);
                            orderModel.setId(response.optJSONObject(i).optString("id"));
                            orderModel.setOrderNumber(response.optJSONObject(i).optString("order_number"));
                            orderModel.setOrderedTime(response.optJSONObject(i).optString("created_at"));
                            orderModel.setStatus(response.optJSONObject(i).optString("status"));
                            orderModel.setTotalPrice(response.optJSONObject(i).optString("total_price"));
                            orderModel.setOrderInvoiceApproved(response.optJSONObject(i).optInt("approved"));
                            orderModel.setOrderedUpdateTime(response.optJSONObject(i).optString("updated_at"));
                            orderList.add(orderModel);
                        }

                        setOrderListRecycler(orderList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_RETAILER_ORDERS);
    }

    private void setOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvInvoiceOrderList.setLayoutManager(layoutManager);
        rvInvoiceOrderList.setAdapter(new InvoiceAdapter(context, orderList));
    }
}