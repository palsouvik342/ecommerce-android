package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindBottomNavigation;
import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_ADDRESS_List;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.AddressAdapter;
import os.com.krishirasayan.adapters.NewsAdapter;
import os.com.krishirasayan.adapters.NewsFeatureAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.NewsModel;

public class NewsActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rcvNewsFeatured,rcvAllNewsList;
    Toolbar toolbar;
    TextView toolbar_title;
    SwipeRefreshLayout swipeLayoutNews;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        bindToolbar(context,R.string.news);
        bindBottomNavigation(context);
        bindReferences();
    }
    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rcvNewsFeatured = findViewById(R.id.rcvNewsFeatured);
        rcvAllNewsList = findViewById(R.id.rcvAllNewsList);
        downloadNewsItems();
        downloadNewsFeatureItems();
        swipeLayoutNews = findViewById(R.id.swipeLayoutNews);
        swipeLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadNewsItems();
                swipeLayoutNews.setRefreshing(false);
            }
        });

    }

    private void downloadNewsItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("role_id", UserData.getKeyUserRoleId(context))
                .withParam("post_category_id", "1")
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                       /* for(int i = 0; i < response.optJSONArray("order_items").length(); i++) {
                            JSONObject orderItem = response.optJSONArray("order_items").optJSONObject(i);

                            w(orderItem);
                        }*/
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        List<NewsModel> newsList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            NewsModel newsModel = new NewsModel(context);
                            //news
                            newsModel.setId(response.optJSONObject(i).optString("id"));
                            newsModel.setTitle(response.optJSONObject(i).optString("title"));
                            if(response.optJSONObject(i).optString("image_url").equals("")){
                                newsModel.setImageUrl("https://bandhansolution.com/storage/posts/April2022/xdeL9hOYtAxXIFfxKYtC-medium.jpg");
                            }else{
                                newsModel.setImageUrl(response.optJSONObject(i).optString("image_url"));
                            }

                            /* //gift list
                            newsModel.setContactName(response.optJSONObject(i).optString("name"));
                            newsModel.setContactNumber(response.optJSONObject(i).optString("created_at"));
                            newsModel.setPostalCode(response.optJSONObject(i).optString("points_required"));
                            newsModel.setImageUrl(response.optJSONObject(i).optString("image_url"));*/

                            newsList.add(newsModel);
                        }

                        setListNewsRecycler(newsList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_NEWS_List);
    }

    private void setListNewsRecycler(List<NewsModel> newsList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllNewsList.setLayoutManager(layoutManager);
        rcvAllNewsList.setAdapter(new NewsAdapter(context, newsList));
        rcvAllNewsList.scheduleLayoutAnimation();
    }

    private void downloadNewsFeatureItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("role_id", UserData.getKeyUserRoleId(context))
                .withParam("post_category_id", "1")
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        List<NewsModel> newsFeatureList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            NewsModel newsModel = new NewsModel(context);
                            newsModel.setId(response.optJSONObject(i).optString("id"));
                            newsModel.setContactName(response.optJSONObject(i).optString("title"));
                            newsModel.setImageUrl(response.optJSONObject(i).optString("image_url"));
                            newsFeatureList.add(newsModel);
                        }

                        setFeatureNewsRecycler(newsFeatureList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_NEWS_List);
    }

    private void setFeatureNewsRecycler(List<NewsModel> newsFeatureList) {
        //News Feature List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvNewsFeatured.setLayoutManager(layoutManager);
        rcvNewsFeatured.setAdapter(new NewsFeatureAdapter(context, newsFeatureList));
    }


}