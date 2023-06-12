package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_READ_PROFILE;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.MyRewardAdapter;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;

public class MyRewardActivity extends AppCompatActivity {
    Context context = this;
    ImageView ivGiftListShowIcon;
    RecyclerView rcvAllRewardList;
    TextView tvEarnedPointValue, tvEarnedBonusPoint, tvPendingPoint;
    Button btnRedeemGift, btnMyGiftList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reward);
        bindToolbar(context, R.string.nav_reward);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rcvAllRewardList = findViewById(R.id.rcvAllRewardList);
        ivGiftListShowIcon = findViewById(R.id.ivGiftListShowIcon);
        tvEarnedPointValue = findViewById(R.id.tvEarnedPointValue);
        tvPendingPoint = findViewById(R.id.tvPendingPoint);
        tvEarnedBonusPoint = findViewById(R.id.tvEarnedBonusPoint);
        downloadRewardItems();


        btnRedeemGift = findViewById(R.id.btnRedeemGift);
        btnRedeemGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context,GiftRedeemActivity.class);
            }
        });

        btnMyGiftList = findViewById(R.id.btnMyGiftList);
        btnMyGiftList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context,MyGiftItemActivity.class);
            }
        });
    }

    private void downloadRewardItems() {

        ivGiftListShowIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, GiftListActivity.class);
                open(context, intent, true);
            }
        });

        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        List<OrderModel> orderList = new ArrayList<>();
                        JSONArray collection = response.optJSONArray("reward_point_logs");
                        for(int i = 0; i < collection.length(); i++) {
                            JSONObject orderItem = collection.optJSONObject(i);
                            OrderModel orderModel = new OrderModel(context);
                            orderModel.setOrderNumber(orderItem.optString("notes"));
                            orderModel.setOrderedTime(orderItem.optString("created_at"));
                            orderModel.setTotalPoints("+" + orderItem.optString("reward_points"));
                            //orderModel.setTotalPrice(orderItem.optString("user_id"));
                            orderList.add(orderModel);
                        }
                        setOrderListRecycler(orderList);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_READ_PROFILE);

        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        tvEarnedPointValue.setText(response.optString("reward_points"));
                        if(response.optString("reward_points_bonus") != null && !response.optString("reward_points_bonus").isEmpty()){
                            tvEarnedBonusPoint.setText(response.optString("reward_points_bonus"));
                        }
                        if(response.optString("reward_points_pending") != null && !response.optString("reward_points_pending").isEmpty()){
                            tvPendingPoint.setText(response.optString("reward_points_pending"));
                        }
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_READ_PROFILE);
    }



    private void setOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllRewardList.setLayoutManager(layoutManager);
        rcvAllRewardList.setAdapter(new MyRewardAdapter(context, orderList));
    }

}