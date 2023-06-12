package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_ASSIGNED_ORDER;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.DistributorAssignedOrderAdapter;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;

public class DistributorOrderListActivity extends AppCompatActivity {
    Context context = this;
    RecyclerView rvDistributorOrderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_order_list);
        bindToolbar(context,R.string.assigned_order);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rvDistributorOrderList = findViewById(R.id.rvDistributorOrderList);
        downloadAssignedOrderItems();
    }

    private void downloadAssignedOrderItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        List<OrderModel> orderList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            OrderModel orderModel = new OrderModel(context);
                            orderModel.setId(response.optJSONObject(i).optString("id"));
                            orderModel.setOrderNumber(response.optJSONObject(i).optString("order_number"));
                            orderModel.setOrderedTime(response.optJSONObject(i).optString("created_at"));
                            orderModel.setOrderedUpdateTime(response.optJSONObject(i).optString("updated_at"));
                            orderModel.setStatus(response.optJSONObject(i).optString("status"));
                            orderModel.setTotalPrice(response.optJSONObject(i).optString("total_price"));
                            orderList.add(orderModel);
                        }

                        setAssignedOrderListRecycler(orderList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_ASSIGNED_ORDER);
    }

    private void setAssignedOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvDistributorOrderList.setLayoutManager(layoutManager);
        rvDistributorOrderList.setAdapter(new DistributorAssignedOrderAdapter(context, orderList));
    }
}