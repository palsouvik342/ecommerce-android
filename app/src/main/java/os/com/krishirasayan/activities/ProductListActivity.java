package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.popupError;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;
import static os.com.krishirasayan.consts.Url.API_WISHLIST;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.ProductAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.ProductModel;
import os.com.krishirasayan.models.ProductVariantModel;

public class ProductListActivity extends AppCompatActivity {

    Context context = this;
    RecyclerView rcvProductBanner, rcvAllProductList;
    SwipeRefreshLayout swipeLayoutNews;
    TextView tvAllProductsList;
    String wishListId;
    SearchView searchViewProductList;
    Integer filter_items,len;
    String filterSearchString;
    Button btnClearProductSearch;
    ProgressBar pbroductList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        bindToolbar(context, R.string.our_products);
        setDefaults();
        bindReferences();

        if (getIntent().hasExtra("product_id")) {
            addToWishlist();
        }
    }

    private void bindReferences() {
        pbroductList = findViewById(R.id.pbroductList);
        pbroductList.setVisibility(View.VISIBLE);
        //rcvProductBanner = findViewById(R.id.rcvProductBanner);
        rcvAllProductList = findViewById(R.id.rcvAllProductList);
        downloadProductItems();
        //downloadProductProductBannerItems();
        swipeLayoutNews = findViewById(R.id.swipeLayoutNews);
        swipeLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadProductItems();
                swipeLayoutNews.setRefreshing(false);
            }
        });


        searchViewProductList = findViewById(R.id.searchViewProductList);
        searchViewProductList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                len = newText.length();
                if(len >  1 || len == 0) {
                    filterSearchString = newText;
                    //filter_items = DISPLAY_LIMIT;
                    downloadProductItems();
                    //scrollData();
                } else {
                    popupError(context,R.string.err_search);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        btnClearProductSearch = findViewById(R.id.btnClearProductSearch);
        btnClearProductSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSearchString = "";
                searchViewProductList.setQuery("", true);
                //searchViewProductList.setIconified(false);
                downloadProductItems();
            }
        });

    }

    private void downloadProductItems() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("search", filterSearchString)
                .withParam("user_id", UserData.getId(context))
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                        pbroductList.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                        pbroductList.setVisibility(View.GONE);
                        List<ProductModel> productList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            ProductModel productModel = new ProductModel(context);
                            JSONObject productObject = response.optJSONObject(i);
                            productModel.setId(productObject.optString("id"));
                            productModel.setProductName(productObject.optString("name"));
                            productModel.setDescription(productObject.optString("usp"));
                            productModel.setPrice(productObject.optString("min_price"));
                            productModel.setRetailerPrice(productObject.optString("min_price_retailer"));
                            productModel.setProductWishlisted(productObject.optBoolean("wishlisted"));
                            JSONArray productVariants = response.optJSONObject(i).optJSONArray("product_variants");
                            if(productVariants.length() > 0) {
                                //productModel.setPoint(response.optJSONObject(i).optJSONObject("least_expensive_variant").optString("reward_points"));
                                productModel.setPoint(productObject.optString("min_reward_points_per_variant"));
                            }
                            else {
                                productModel.setPoint("0");
                            }
                            productModel.setImageUrl(productObject.optString("image_url"));

                            ArrayList<ProductVariantModel> productVariantModels = new ArrayList<>();

                            for(int j = 0; j < productObject.optJSONArray("product_variants").length(); j++) {

                                ProductVariantModel productVariantModel = new ProductVariantModel();
                                JSONObject productVariantObject = productObject.optJSONArray("product_variants").optJSONObject(j);
                                w("Users",productVariantObject);
                                productVariantModel.setId(productVariantObject.optString("id"));
                                productVariantModel.setName(productVariantObject.optString("name"));
                                productVariantModel.setListingPrice(productVariantObject.optString("listing_price"));
                                productVariantModel.setSellingPrice(productVariantObject.optString("selling_price"));
                                productVariantModel.setRetailerListingPrice(productVariantObject.optString("retailer_listing_price"));
                                productVariantModel.setRetailerSellingPrice(productVariantObject.optString("retailer_selling_price"));
                                productVariantModel.setPoint(productVariantObject.optString("reward_points"));
                                productVariantModel.setImageUrl(productObject.optString("image_url"));


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
        //downloadWishlistItems();
    }

    private void setProductListRecycler(List<ProductModel> productList) {
        //News List
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvAllProductList.setLayoutManager(layoutManager);
        rcvAllProductList.setAdapter(new ProductAdapter(context, productList));
        rcvAllProductList.scheduleLayoutAnimation();

       /* swipeLayoutNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadProductItems();
                swipeLayoutNews.setRefreshing(false);
            }
        });*/
    }

    private void addToWishlist() {
        wishListId = getIntent().getStringExtra("product_id");
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
                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                        /*downloadProductItems();*/
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                       // popupError(context, "Product already added to wishlist ");
                    }

                }).put(API_WISHLIST);
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
                        List<ProductModel> productList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            ProductModel productModel = new ProductModel(context);
                            JSONObject productObject = response.optJSONObject(i);
                            productModel.setProductWishlist(productObject.optJSONObject("wishlist").optString("product_id"));
                            productList.add(productModel);
                        }

                        setProductListRecycler(productList);                  }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                    }
                }).get(API_WISHLIST);
    }

    private void setDefaults() {
        filterSearchString = "";
    }
}


