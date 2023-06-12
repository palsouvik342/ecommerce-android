package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.openAndFinish;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;
import static os.com.krishirasayan.consts.Url.API_GIFT_REDEEM;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.CartListAdapter;
import os.com.krishirasayan.adapters.GiftAdapter;
import os.com.krishirasayan.adapters.GiftRedeemAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.GiftModel;

public class GiftRedeemActivity extends AppCompatActivity {

    Context context =  this;
    RecyclerView rcvAllRedeemGiftList;
    SwipeRefreshLayout swipeLayoutRedeemGift;
    TextView tvRedeemGiftTitle,tvRedeemGiftPoint,tvUserRedeemPoint;
    Button btnRedeemGiftChose;
    List<GiftModel> selectedGifts;
    int selectedPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_redeem);
        bindToolbar(context,R.string.redeem_gift);
        rcvAllRedeemGiftList = findViewById(R.id.rcvAllRedeemGiftList);
        tvRedeemGiftTitle = findViewById(R.id.tvRedeemGiftTitle);
        tvRedeemGiftPoint = findViewById(R.id.tvRedeemGiftPoint);
        tvUserRedeemPoint = findViewById(R.id.tvUserRedeemPoint);
        tvUserRedeemPoint.setText(UserData.getRewardPoints(context));
        downloadGiftItems();
        swipeLayoutRedeemGift = findViewById(R.id.swipeLayoutRedeemGift);
        selectedGifts = new ArrayList<>();
        btnRedeemGiftChose = findViewById(R.id.btnRedeemGiftChose);
        btnRedeemGiftChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedGifts.size() == 0) {
                    popupError(context,"Please choose gifts before redeeming");
                    return;
                }
                w("1");

                if(selectedPoints > Float.parseFloat(UserData.getRewardPoints(context))) {
                    popupError(context,"Insufficient points");
                    e("Insufficient points");
                    return;
                }

                w("2");

                VolleyService volleyService = new VolleyService(context);
                volleyService.withParam("token", UserData.getToken(context));
                int i = 0;
                for(GiftModel gift : selectedGifts) {
                    volleyService.withParamArray("gifts", i, gift.getId());
                    volleyService.withParamArray("quantities", i, String.valueOf(gift.getQuantity()));
                    i++;
                }
                volleyService.withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                        Toast.makeText(context, "Gift redeem requested to Admin", Toast.LENGTH_SHORT).show();
                        openAndFinish(context, MyRewardActivity.class);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                    }
                });
                volleyService.post(API_GIFT_REDEEM);
            }
        });

    }

    private void downloadGiftItems() {
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
                        List<GiftModel> giftList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            GiftModel giftModel = new GiftModel(context);
                            giftModel.setId(response.optJSONObject(i).optString("id"));
                            giftModel.setName(response.optJSONObject(i).optString("name"));
                            giftModel.setPoints(response.optJSONObject(i).optInt("points_required"));
                            giftModel.setImageUrl(response.optJSONObject(i).optString("image_url"));

                            giftList.add(giftModel);
                        }

                        setGiftListRecycler(giftList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_GIFT_List);
    }

    private void setGiftListRecycler(List<GiftModel> giftList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllRedeemGiftList.setLayoutManager(layoutManager);
        rcvAllRedeemGiftList.setAdapter(new GiftRedeemAdapter(context, giftList, false, new ICallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void function(Object object) {
//                downloadProductItems();
                selectedPoints = 0;
                List<GiftModel> selected = (List<GiftModel>) object;
                selected.stream().forEach(e -> selectedPoints += e.getQuantity() * e.getPoints());
                tvRedeemGiftPoint.setText(String.valueOf(selectedPoints));
                selectedGifts = selected;

            }
        }));
        rcvAllRedeemGiftList.scheduleLayoutAnimation();

        swipeLayoutRedeemGift.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadGiftItems();
                swipeLayoutRedeemGift.setRefreshing(false);
            }
        });
    }

    private void downloadProductItems(Float points) {

        w("Users",points);
        tvRedeemGiftPoint.setText(String.valueOf(points));
    }
}