package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;
import static os.com.krishirasayan.consts.Url.API_MY_GIFT_List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.GiftAdapter;
import os.com.krishirasayan.adapters.MyGiftAdapter;
import os.com.krishirasayan.adapters.MyGiftRedeemAdapter;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.GiftRedeemModel;
import os.com.krishirasayan.models.OrderModel;

public class MyGiftItemActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rcvMyGiftList;
    ArrayList<GiftModel> giftListItem;
    Button btnRedeemGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift_item);
        bindToolbar(context,R.string.my_gifts);
        btnRedeemGift = findViewById(R.id.btnRedeemGift);
        btnRedeemGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftRedeemActivity.class);
                open(context, intent, true);
            }
        });
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MyRewardActivity.class);
        open(context, intent, true);
    }

    private void bindReferences() {
        rcvMyGiftList = findViewById(R.id.rcvMyGiftList);
        downloadMyGiftItems();
    }

    private void downloadMyGiftItems() {
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
                        List<GiftRedeemModel> giftList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            GiftRedeemModel giftModel = new GiftRedeemModel(context);
                            giftModel.setRequestTime(response.optJSONObject(i).optString("created_at"));
                            giftModel.setUpdateTime(response.optJSONObject(i).optString("updated_at"));
                            giftModel.setRequestStatus(response.optJSONObject(i).optString("status"));
                            giftModel.setTotalRedeemPoint(response.optJSONObject(i).optInt("points_redeemed"));
                            giftModel.setTotalQuantity(response.optJSONObject(i).optInt("item_count"));


                            JSONArray jsonArray = response.optJSONObject(i).optJSONArray("data");
                            giftListItem = new ArrayList<>();
                            for (int x = 0; x < jsonArray.length(); x++) {
                                GiftModel giftItemModel = new GiftModel(context);
                                giftItemModel.setName(jsonArray.optJSONObject(x).optString("name"));
                                giftItemModel.setPoints(jsonArray.optJSONObject(x).optInt("points_required"));
                                giftItemModel.setQuantity(jsonArray.optJSONObject(x).optInt("quantity"));
                                giftListItem.add(giftItemModel);
                            }
                            giftModel.setGiftList(giftListItem);
                            giftList.add(giftModel);
                            //setMyGiftListItemRecycler(giftListItem);
                        }
                        setMyGiftListRecycler(giftList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_MY_GIFT_List);
    }

    private void setMyGiftListRecycler(List<GiftRedeemModel> giftList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvMyGiftList.setLayoutManager(layoutManager);
        rcvMyGiftList.setAdapter(new MyGiftRedeemAdapter(context, giftList));
    }

    private void setMyGiftListItemRecycler(ArrayList<GiftModel> giftListItem) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvMyGiftList.setLayoutManager(layoutManager);
        rcvMyGiftList.setAdapter(new MyGiftAdapter(context, giftListItem,true));
    }
}