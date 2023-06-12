package os.com.krishirasayan.activities;


import static android.util.Log.w;
import static os.com.krishirasayan.consts.Helper.bindBottomNavigation;
import static os.com.krishirasayan.consts.Helper.bindToolbarWithNavDraw;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.getFormattedDate;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_BANNER_List;
import static os.com.krishirasayan.consts.Url.API_CROP_MSP;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;
import static os.com.krishirasayan.consts.Url.API_WEATHER_REPORT;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.GiftAdapter;
import os.com.krishirasayan.adapters.HomeCropMspAdapter;
import os.com.krishirasayan.adapters.HomeGiftAdapter;
import os.com.krishirasayan.adapters.HomeNewsFeatureAdapter;
import os.com.krishirasayan.adapters.HomeProductAdapter;
import os.com.krishirasayan.adapters.OrderAdapter;
import os.com.krishirasayan.adapters.ProductAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.BannerModel;
import os.com.krishirasayan.models.CropMspModel;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.NewsModel;
import os.com.krishirasayan.models.OrderModel;
import os.com.krishirasayan.models.ProductModel;
import os.com.krishirasayan.models.ProductVariantModel;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    ImageView ivKishanBazarIcon, ivKishanSandeshIcon, ivCropDoctorIcon, ivKishanVedikaIcon;
    ImageSlider ivHomeslider;
    RecyclerView rvNewsHome,rvProductsHome,rvCropMsp, rvHomeGift, rvTrdProductsHome;
    String username, USER_ROLE_ID;
    LinearLayout llWeather;
    ConstraintLayout clHomeProducts, clHomeReward;
    FloatingActionButton fab;
    CardView cvKishanVedika;

    TextView tvViewAllGift, tvWeatherTemp, tvWeatherDate, tvWeatherLocation, tvWeatherHumidity, tvWeatherBrief, tvWeatherWind, tvViewAllNews, tvViewAllProduct, tvViewAllTrdProduct,tvNavUserName;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindToolbarWithNavDraw(context,R.string.welcome);
        bindBottomNavigation(context);
        bindReferences();
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    private void bindReferences() {
        username = String.format("Hello, %s", UserData.getName(context));

        clHomeReward = findViewById(R.id.clHomeReward);
        clHomeReward.setVisibility(View.GONE);

        tvWeatherDate = findViewById(R.id.tvWeatherDate);
        tvWeatherLocation = findViewById(R.id.tvWeatherLocation);
        tvWeatherTemp = findViewById(R.id.tvWeatherTemp);
        tvWeatherHumidity = findViewById(R.id.tvWeatherHumidity);
        tvWeatherBrief = findViewById(R.id.tvWeatherBrief);
        tvWeatherWind = findViewById(R.id.tvWeatherWind);

        cvKishanVedika = findViewById(R.id.cvKishanVedika);
        cvKishanVedika.setVisibility(View.GONE);

        tvViewAllGift = findViewById(R.id.tvViewAllGift);
        tvViewAllGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, GiftListActivity.class);
            }
        });

        clHomeProducts = findViewById(R.id.clHomeProducts);
        USER_ROLE_ID=UserData.getKeyUserRoleId(context);
        if(USER_ROLE_ID.equals("5")){
            clHomeProducts.setVisibility(View.GONE);
        }

        USER_ROLE_ID=UserData.getKeyUserRoleId(context);
        if(USER_ROLE_ID.equals("4")){
            cvKishanVedika.setVisibility(View.VISIBLE);
            clHomeReward.setVisibility(View.VISIBLE);
        }

        llWeather = findViewById(R.id.llWeather);
        llWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, WeatherDetailsActivity.class);
            }
        });

        tvViewAllNews = findViewById(R.id.tvViewAllNews);
        tvViewAllNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, NewsActivity.class);
            }
        });

        tvViewAllProduct = findViewById(R.id.tvViewAllProduct);
        tvViewAllProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, ProductListActivity.class);
            }
        });

        //trending product view all button
        tvViewAllTrdProduct = findViewById(R.id.tvViewAllTrdProduct);
        tvViewAllTrdProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, ProductListActivity.class);
            }
        });

        ivKishanBazarIcon = findViewById(R.id.ivKishanBazarIcon);
        ivKishanBazarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(USER_ROLE_ID.equals("5")) {
                    open(context, DistributorOrderListActivity.class);
                } else {
                    open(context, ProductListActivity.class);
                }
            }
        });

        cvKishanVedika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, SchemeListActivity.class);
            }
        });

        ivKishanSandeshIcon = findViewById(R.id.ivKishanSandeshIcon);
        ivKishanSandeshIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(context, NewsActivity.class);
            }
        });

        ivCropDoctorIcon = findViewById(R.id.ivCropDoctorIcon);
        ivCropDoctorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });



        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp();
            }
        });

        ivHomeslider = findViewById(R.id.ivHomeslider);
        downloadBannerItems();
        rvNewsHome = findViewById(R.id.rvNewsHome);
        downloadHomeNewsFeatureItems();
        rvProductsHome = findViewById(R.id.rvProductsHome);
        rvTrdProductsHome = findViewById(R.id.rvProductsTrdHome);
        rvCropMsp = findViewById(R.id.rvCropMsp);
        downloadHomeProductsItems();
        downloadHomeProductsTrdItems();
        rvHomeGift = findViewById(R.id.rvHomeGift);
        downloadHomeGiftItems();
        downloadWeatherReport();
        downloadCropMsp();

       /* bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miHome:
                        open(context, MainActivity.class);
                        break;
                    case R.id.miProfile:
                        open(context, MyProfileActivity.class);
                        break;
                    case R.id.miWishlist:
                        open(context, WishlistActivity.class);
                        break;
                    case R.id.miNews:
                        open(context, NewsActivity.class);
                        break;
                }
            }
        });*/

    }

    private void openWhatsApp(){
        try {
            String text = "Hello, want to know more.";
            String toNumber = "916292209495";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void downloadBannerItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("banner_category_id", "1")
                .withParam("role_id", USER_ROLE_ID)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {

                        List<SlideModel> imageSlider  = new ArrayList<>();
                        List<BannerModel> bannerSlider  = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            bannerSlider.add(new BannerModel(response.optJSONObject(i).optString("image_url"), response.optJSONObject(i).optString("youtube_video_id")));
                            imageSlider.add(new SlideModel(response.optJSONObject(i).optString("image_url"), ScaleTypes.CENTER_CROP));
                        }
                        ivHomeslider.setImageList(imageSlider);
                        ivHomeslider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int position) {
                                String imageVideoId =  bannerSlider.get(position).getImageVideoLink();
                                w("HELLO", imageVideoId);
                                if(imageVideoId != null && !imageVideoId.isEmpty() && !imageVideoId.equals("null")){
                                    Intent intent = new Intent(context, SingleProductVideoActivity.class);
                                    intent.putExtra("youtube_link", imageVideoId);
                                    open(context, intent, true);
                                }

                            }
                        });
                    }
                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_BANNER_List);
    }

    private void downloadHomeNewsFeatureItems() {
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
        rvNewsHome.setLayoutManager(layoutManager);
        rvNewsHome.setAdapter(new HomeNewsFeatureAdapter(context, newsFeatureList));
    }

    private void downloadHomeProductsItems() {
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
                        List<ProductModel> productList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            ProductModel productModel = new ProductModel(context);
                            productModel.setId(response.optJSONObject(i).optString("id"));
                            productModel.setProductName(response.optJSONObject(i).optString("name"));
                            //productModel.setDescription(response.optJSONObject(i).optString("usp"));
                            productModel.setPrice(response.optJSONObject(i).optString("min_price"));
                            productModel.setProductWishlisted(response.optJSONObject(i).optBoolean("wishlisted"));
                            JSONArray productVariants = response.optJSONObject(i).optJSONArray("product_variants");
                            if(productVariants.length() > 0) {
                                productModel.setPoint(response.optJSONObject(i).optString("min_reward_points_per_variant"));
                            }
                            else {
                                productModel.setPoint("0");
                            }
                            productModel.setImageUrl(response.optJSONObject(i).optString("image_url"));

                            ArrayList<ProductVariantModel> productVariantModels = new ArrayList<>();

                            JSONObject productObject = response.optJSONObject(i);
                            for(int j = 0; j < productObject.optJSONArray("product_variants").length(); j++) {

                                ProductVariantModel productVariantModel = new ProductVariantModel();
                                JSONObject productVariantObject = productObject.optJSONArray("product_variants").optJSONObject(j);
                                productVariantModel.setId(productVariantObject.optString("id"));
                                productVariantModel.setName(productVariantObject.optString("name"));
                                productVariantModel.setListingPrice(productVariantObject.optString("listing_price"));
                                productVariantModel.setSellingPrice(productVariantObject.optString("selling_price"));
                                productVariantModel.setImageUrl(productObject.optString("image_url"));
                                if(productVariantObject.length() > 0) {
                                    productVariantModel.setPoint(productVariantObject.optString("reward_points"));
                                }
                                else {
                                    productVariantModel.setPoint("0");
                                }


                                ArrayList<String> productVariantValues = new ArrayList<>();
                                for(int k = 0; k < productVariantObject.optJSONArray("values").length(); k++) {

                                    JSONObject variantValue = productVariantObject.optJSONArray("values").optJSONObject(k);
                                    productVariantValues.add(variantValue.optString("name"));
                                }

                                productVariantModel.setValues(productVariantValues);
                                productVariantModels.add(productVariantModel);
                            }

                            productModel.setProductVariantModel(productVariantModels);
                            productList.add(productModel);
                        }

                        setProductListRecycler(productList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_PRODUCT_List);
    }

    //Trending Product
    private void downloadHomeProductsTrdItems() {
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
                        List<ProductModel> productList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            ProductModel productModel = new ProductModel(context);
                            productModel.setId(response.optJSONObject(i).optString("id"));
                            productModel.setProductName(response.optJSONObject(i).optString("name"));
                            //productModel.setDescription(response.optJSONObject(i).optString("usp"));
                            productModel.setPrice(response.optJSONObject(i).optString("min_price"));
                            productModel.setProductWishlisted(response.optJSONObject(i).optBoolean("wishlisted"));
                            JSONArray productVariants = response.optJSONObject(i).optJSONArray("product_variants");
                            if(productVariants.length() > 0) {
                                productModel.setPoint(response.optJSONObject(i).optString("min_reward_points_per_variant"));
                            }
                            else {
                                productModel.setPoint("0");
                            }
                            productModel.setImageUrl(response.optJSONObject(i).optString("image_url"));

                            ArrayList<ProductVariantModel> productVariantModels = new ArrayList<>();

                            JSONObject productObject = response.optJSONObject(i);
                            for(int j = 0; j < productObject.optJSONArray("product_variants").length(); j++) {

                                ProductVariantModel productVariantModel = new ProductVariantModel();
                                JSONObject productVariantObject = productObject.optJSONArray("product_variants").optJSONObject(j);
                                productVariantModel.setId(productVariantObject.optString("id"));
                                productVariantModel.setName(productVariantObject.optString("name"));
                                productVariantModel.setListingPrice(productVariantObject.optString("listing_price"));
                                productVariantModel.setSellingPrice(productVariantObject.optString("selling_price"));
                                productVariantModel.setImageUrl(productObject.optString("image_url"));
                                if(productVariantObject.length() > 0) {
                                    productVariantModel.setPoint(productVariantObject.optString("reward_points"));
                                }
                                else {
                                    productVariantModel.setPoint("0");
                                }


                                ArrayList<String> productVariantValues = new ArrayList<>();
                                for(int k = 0; k < productVariantObject.optJSONArray("values").length(); k++) {

                                    JSONObject variantValue = productVariantObject.optJSONArray("values").optJSONObject(k);
                                    productVariantValues.add(variantValue.optString("name"));
                                }

                                productVariantModel.setValues(productVariantValues);
                                productVariantModels.add(productVariantModel);
                            }

                            productModel.setProductVariantModel(productVariantModels);
                            productList.add(productModel);
                        }

                        setProductTrdListRecycler(productList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_PRODUCT_List);
    }

    private void setProductListRecycler(List<ProductModel> productList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProductsHome.setLayoutManager(layoutManager);
        rvProductsHome.setAdapter(new HomeProductAdapter(context, productList));
        rvProductsHome.scheduleLayoutAnimation();
    }

    //Trending Product
    private void setProductTrdListRecycler(List<ProductModel> productList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvTrdProductsHome.setLayoutManager(layoutManager);
        rvTrdProductsHome.setAdapter(new HomeProductAdapter(context, productList));
        rvTrdProductsHome.scheduleLayoutAnimation();
    }

    private void downloadWeatherReport() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("location", "");

        new VolleyService(context).get(API_WEATHER_REPORT, params, new IVolleyResult() {
            @Override
            public void notifyRequestQueued(String requestName) {
                tvWeatherTemp.setText(R.string.please_wait);
            }

            @Override
            public void notifySuccess(String requestName, JSONObject response) {
                setWeatherReport(response);
            }

            @Override
            public void notifySuccess(String requestName, JSONArray response) {

            }

            @Override
            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                e("Could not download Weather data: " + error.toString());
            }
        });
    }

    private void setWeatherReport(JSONObject response) {
        try {
            String tempNow = response.getJSONObject("compiled").getString("temp");
            String tempMin = response.getJSONObject("compiled").getString("temp_min");
            String tempMax = response.getJSONObject("compiled").getString("temp_max");
            String location = response.getJSONObject("compiled").getString("location");
            String humidity = response.getJSONObject("compiled").getString("humidity");
            String weather = response.getJSONObject("compiled").getString("weather");
            String wind = response.getJSONObject("compiled").getString("wind");
            String date =  getFormattedDate("EEE dd MMM");

            tvWeatherDate.setText(date);
            tvWeatherTemp.setText(tempNow);
            tvWeatherLocation.setText(location);
            tvWeatherHumidity.setText(humidity);
            tvWeatherBrief.setText(weather);
            tvWeatherWind.setText(wind);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadCropMsp() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
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
                        List<CropMspModel> CropMspList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            CropMspModel cropmspModel = new CropMspModel(context);
                            cropmspModel.setId(response.optJSONObject(i).optString("id"));
                            cropmspModel.setName(response.optJSONObject(i).optString("name"));
                            cropmspModel.setMspRate(response.optJSONObject(i).optString("price"));
                            cropmspModel.setMspUnit(response.optJSONObject(i).optString("unit"));
                            cropmspModel.setMspYear(response.optJSONObject(i).optString("year"));
                            cropmspModel.setImageUrl(response.optJSONObject(i).optString("image_url"));
                            CropMspList.add(cropmspModel);
                        }

                        setCropMspRecycler(CropMspList);
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_CROP_MSP);
    }

    private void setCropMspRecycler(List<CropMspModel> CropMspList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvCropMsp.setLayoutManager(layoutManager);
        rvCropMsp.setAdapter(new HomeCropMspAdapter(context, CropMspList));
    }

    private void downloadHomeGiftItems() {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvHomeGift.setLayoutManager(layoutManager);
        rvHomeGift.setAdapter(new HomeGiftAdapter(context, giftList));
        rvHomeGift.scheduleLayoutAnimation();
    }

}