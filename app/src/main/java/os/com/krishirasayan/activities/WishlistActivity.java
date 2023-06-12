package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindBottomNavigation;
import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Url.API_GIFT_List;
import static os.com.krishirasayan.consts.Url.API_NEWS_List;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;
import static os.com.krishirasayan.consts.Url.API_WISHLIST;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.GiftAdapter;
import os.com.krishirasayan.adapters.GiftBannerAdapter;
import os.com.krishirasayan.adapters.NewsAdapter;
import os.com.krishirasayan.adapters.NewsFeatureAdapter;
import os.com.krishirasayan.adapters.WishlistAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.ProductModel;
import os.com.krishirasayan.models.ProductVariantModel;

public class WishlistActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rcvAllWishList;
    SwipeRefreshLayout swipeLayoutNews;
    String wishListId;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        bindToolbar(context,R.string.my_wishlist);
        bindBottomNavigation(context);
        bindReferences();
        if (getIntent().hasExtra("wishlist_id")) {
            removeFromWishlist();
        }
    }

    @Override
    public void onBackPressed() {
        open(context,MainActivity.class);
    }

    private void bindReferences() {
        rcvAllWishList = findViewById(R.id.rcvAllWishList);
       // downloadGiftItems();
        downloadWishlistItems();
        swipeLayoutNews = findViewById(R.id.swipeLayoutNews);
        swipeLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadWishlistItems();
                swipeLayoutNews.setRefreshing(false);
            }
        });

    }

    private void downloadWishlistItems() {
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
                        List<ProductModel> wishList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            ProductModel productModel = new ProductModel(context);
                            JSONObject productObject = response.optJSONObject(i);
                            productModel.setId(productObject.optString("id"));
                            productModel.setProductName(productObject.optString("name"));
                            productModel.setDescription(productObject.optString("usp"));
                            productModel.setPrice(productObject.optString("min_price"));
                            productModel.setImageUrl(productObject.optString("image_url"));

                            JSONArray productVariants = response.optJSONObject(i).optJSONArray("product_variants");
                            if(productVariants.length() > 0) {
                                productModel.setPoint(response.optJSONObject(i).optString("min_reward_points_per_variant"));
                            }
                            else {
                                productModel.setPoint("0");
                            }

                            ArrayList<ProductVariantModel> productVariantModels = new ArrayList<>();

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
                            wishList.add(productModel);
                        }

                        setWishListRecycler(wishList);                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_WISHLIST);
    }

    private void setWishListRecycler(List<ProductModel> wishList) {
        //News Feature List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllWishList.setLayoutManager(layoutManager);
        rcvAllWishList.setAdapter(new WishlistAdapter(context, wishList));

    }

    private void removeFromWishlist() {
        wishListId = getIntent().getStringExtra("wishlist_id");
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("product_id",wishListId)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        Toast.makeText(context, "Removed from wishlist", Toast.LENGTH_SHORT).show();
                        downloadWishlistItems();
                        /*finish();
                        startActivity(getIntent());*/
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }

                }).delete(API_WISHLIST);
    }



}