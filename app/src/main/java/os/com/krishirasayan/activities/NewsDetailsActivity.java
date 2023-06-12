package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.NewsModel;

public class NewsDetailsActivity extends AppCompatActivity {

    Context context = this;
    ImageView ivBlogFeatureImage, ivBlogPostShare;
    TextView tvBlogPostTitle, tvBlogPostTime, tvBlogPostDetails;
    String newsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        bindToolbar(context,R.string.news_details);
        bindReferences();
    }

    private void bindReferences() {
        tvBlogPostTitle = findViewById(R.id.tvBlogPostTitle);
        tvBlogPostTime = findViewById(R.id.tvBlogPostTime);
        tvBlogPostDetails = findViewById(R.id.tvBlogPostDetails);
        ivBlogFeatureImage = findViewById(R.id.ivBlogFeatureImage);
        ivBlogPostShare = findViewById(R.id.ivBlogPostShare);

        ivBlogPostShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = tvBlogPostTitle.getText().toString() + "Download our app from Playstore. Click here https://play.google.com/store/apps";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
            }
        });

        downloadNewsItems();
    }

    @Override
    public void onBackPressed() {
        open(context,NewsActivity.class);
    }

    private void downloadNewsItems() {
        if (getIntent().hasExtra("news_id")) {
            newsId = getIntent().getStringExtra("news_id");
           // Toast.makeText(context, newsId, Toast.LENGTH_SHORT).show();
            new VolleyService(context)
                    //.withParam("token", UserData.getToken(context))
                    //.withParam("post",newsId)
                    .withCallbacks(new IVolleyResult() {
                        @Override
                        public void notifyRequestQueued(String requestName) {
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONObject response) {
                            tvBlogPostTitle.setText(response.optString("title"));
                            tvBlogPostTime.setText(response.optString("created_at"));
                            //tvBlogPostDetails.setText(response.optString("body"));
                            if (Build.VERSION.SDK_INT >= 24)
                            {
                                tvBlogPostDetails.setText(Html.fromHtml(response.optString("body") , Html.FROM_HTML_MODE_LEGACY));
                            }
                            else
                            {
                                tvBlogPostDetails.setText(Html.fromHtml(response.optString("body")));
                            }
                            Picasso.get()
                                    .load(response.optString("image_url"))
                                    .into(ivBlogFeatureImage);
                        }

                        @Override
                        public void notifySuccess(String requestName, JSONArray response) {
                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        }
                    }).get(API_NEWS_List + "/" + newsId);
        }
    }
}