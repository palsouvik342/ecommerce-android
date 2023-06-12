package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;
import static os.com.krishirasayan.consts.Url.API_RETAILER_ORDERS;
import static os.com.krishirasayan.consts.Url.API_SCHEME;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.HomeNewsFeatureAdapter;
import os.com.krishirasayan.adapters.InvoiceAdapter;
import os.com.krishirasayan.adapters.KishanVedikaAdapter;
import os.com.krishirasayan.adapters.NewsAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.NewsModel;
import os.com.krishirasayan.models.OrderModel;

public class SchemeListActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rvSchemeList;
    ProgressBar pbSchemeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_list);
        bindToolbar(context,R.string.schemes);
        bindReferences();
    }

    private void bindReferences() {
        rvSchemeList = findViewById(R.id.rvSchemeList);
        pbSchemeList = findViewById(R.id.pbSchemeList);
        downloadSchemeItems();
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void downloadSchemeItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("post_category_id", "2")
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

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
                            newsList.add(newsModel);
                        }
                        setOrderListRecycler(newsList);
                        pbSchemeList.setVisibility(View.GONE);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_NEWS_List);
    }

    private void setOrderListRecycler(List<NewsModel> newsList) {
        //News Feature List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvSchemeList.setLayoutManager(layoutManager);
        rvSchemeList.setAdapter(new NewsAdapter(context, newsList));
        rvSchemeList.scheduleLayoutAnimation();
    }
}