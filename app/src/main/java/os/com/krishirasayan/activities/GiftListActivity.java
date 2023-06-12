package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_BANNER_List;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.GiftAdapter;
import os.com.krishirasayan.adapters.GiftBannerAdapter;
import os.com.krishirasayan.adapters.NewsAdapter;
import os.com.krishirasayan.adapters.NewsFeatureAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.GiftModel;

public class GiftListActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rcvGiftBanner,rcvAllGiftList;
    Toolbar toolbar;
    TextView toolbar_title;
    SwipeRefreshLayout swipeLayoutNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_list);
        bindToolbar(context,R.string.gift);
        bindReferences();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, MyRewardActivity.class);
        open(context, intent, true);
    }

    private void bindReferences() {
        rcvGiftBanner = findViewById(R.id.rcvGiftBanner);
        rcvAllGiftList = findViewById(R.id.rcvAllGiftList);
        downloadGiftItems();
        downloadGiftBannerItemsApi();
        swipeLayoutNews = findViewById(R.id.swipeLayoutNews);

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
        rcvAllGiftList.setLayoutManager(layoutManager);
        rcvAllGiftList.setAdapter(new GiftAdapter(context, giftList));
        rcvAllGiftList.scheduleLayoutAnimation();

        swipeLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadGiftItems();
                swipeLayoutNews.setRefreshing(false);
            }
        });
    }

    private void downloadGiftBannerItemsApi() {
        new VolleyService(context)
                .withParam("banner_category_id", "2")
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        //w(response);
                        List<GiftModel> giftBannerList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            //w(response.optJSONObject(i).optString("image_url"));
                            GiftModel giftModel = new GiftModel(context);
                            giftModel.setBannerImageUrl(response.optJSONObject(i).optString("image_url"));
                            giftBannerList.add(giftModel);
                        }

                        setGiftBannerRecycler(giftBannerList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_BANNER_List);
    }

    private void setGiftBannerRecycler(List<GiftModel> giftBannerList) {
        //News Feature List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvGiftBanner.setLayoutManager(layoutManager);
        rcvGiftBanner.setAdapter(new GiftBannerAdapter(context, giftBannerList));
    }


}