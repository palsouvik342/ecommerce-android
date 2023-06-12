package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ALL_COUPONS;
import static os.com.krishirasayan.consts.Url.API_COUPON_DETAILS;
import static os.com.krishirasayan.consts.Url.API_COUPON_REDEEM;
import static os.com.krishirasayan.consts.Url.API_READ_PROFILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.MyRewardAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.OrderModel;

public class MyWalletActivity extends AppCompatActivity {
    Context context = this;
    ConstraintLayout clCouponPopup;
    TextView tvWalletBalanceValue;
    EditText etRedeemCoupon;
    Button btnRedeem, btRedeemConfirm, btRedeemCancel;
    RadioGroup rgCouponOption;
    RadioButton rbWalletPoint, rbRewardPoint;
    RecyclerView rcvAllCouponList;
    String couponCode, redeemType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        bindToolbar(context, R.string.my_wallet);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rcvAllCouponList = findViewById(R.id.rcvAllCouponList);
        clCouponPopup = findViewById(R.id.clCouponPopup);
        clCouponPopup.setVisibility(View.GONE);

        tvWalletBalanceValue = findViewById(R.id.tvWalletBalanceValue);
        etRedeemCoupon = findViewById(R.id.etRedeemCoupon);
        btnRedeem = findViewById(R.id.btnRedeem);
        btRedeemConfirm = findViewById(R.id.btRedeemConfirm);
        btRedeemCancel = findViewById(R.id.btRedeemCancel);
        rgCouponOption = findViewById(R.id.rgCouponOption);
        rbWalletPoint = findViewById(R.id.rbWalletPoint);
        rbRewardPoint = findViewById(R.id.rbRewardPoint);
        downloadCouponItems();

        btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponValidation();
            }
        });

        btRedeemCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clCouponPopup.setVisibility(View.GONE);
            }
        });

        btRedeemConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponAddToAccount();
            }
        });

        rgCouponOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbWalletPoint:
                        w("TAG", "1");
                        redeemType = "WALLET_AMOUNT";
                        break;
                    case R.id.rbRewardPoint:
                        w("TAG", "2");
                        redeemType = "REWARD_POINTS";
                        break;
                }
            }
        });


    }

    private void downloadCouponItems() {
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
                            orderModel.setOrderNumber(response.optJSONObject(i).optString("notes"));
                            orderModel.setOrderedTime(response.optJSONObject(i).optString("date"));
                            orderModel.setTotalPoints(response.optJSONObject(i).optString("coupon_code"));
                            orderList.add(orderModel);
                        }
                        setOrderListRecycler(orderList);
                    }
                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_ALL_COUPONS);

        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        tvWalletBalanceValue.setText(response.optString("wallet_amount"));
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_READ_PROFILE);
    }

    private void couponValidation() {
        couponCode = etRedeemCoupon.getText().toString();
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        rbWalletPoint.setText(response.optString("wallet_amount") + " Wallet balance to your account");
                        rbRewardPoint.setText(response.optString("reward_points") + " Reward Point to your account");
                        clCouponPopup.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        Toast.makeText(context, "Coupon is invalid!", Toast.LENGTH_SHORT).show();
                    }
                }).get(API_COUPON_DETAILS + couponCode);
    }

    private void couponAddToAccount() {
       if(rbWalletPoint.isChecked() == false && rbRewardPoint.isChecked() == false){
            Toast.makeText(context, "Please choose an option to redeem coupon!", Toast.LENGTH_SHORT).show();
        }else {
            new VolleyService(context)
                    .withParam("token", UserData.getToken(context))
                    .withParam("code", couponCode)
                    .withParam("redeem_type", redeemType)
                    .withCallbacks(new IVolleyResult() {
                        @Override
                        public void notifyRequestQueued(String requestName) {
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONObject response) {
                            Toast.makeText(context, "Hurry!! Coupon redeemed successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONArray response) {
                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                            Toast.makeText(context, "Oops! something went wrong. Please try again later..", Toast.LENGTH_SHORT).show();
                        }
                    }).post(API_COUPON_REDEEM);
        }
    }

    private void setOrderListRecycler(List<OrderModel> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllCouponList.setLayoutManager(layoutManager);
        rcvAllCouponList.setAdapter(new MyRewardAdapter(context, orderList));
    }

}