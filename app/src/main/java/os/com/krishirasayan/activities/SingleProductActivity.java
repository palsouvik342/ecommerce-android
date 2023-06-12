package os.com.krishirasayan.activities;

import static os.com.krishirasayan.consts.Helper.bindToolbar;
import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_PRODUCT_List;
import static os.com.krishirasayan.consts.Url.API_WISHLIST;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import os.com.krishirasayan.R;
import os.com.krishirasayan.adapters.ProductVariantAdapter;
import os.com.krishirasayan.adapters.ProductVariantRetailerBuyAdapter;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.ProductVariantModel;

public class SingleProductActivity extends AppCompatActivity {

    Context context = this;
    ImageView ivProductDetailsImage, ivHomeListProductWishlistAdded2, ivHomeListProductWishlistIcon2;
    TextView tvProductDetailsUspDescription, tvProductDetailsTitle, ivHomeListProductPoint, tvProductDetailsTechnicalValue, tvProductDetailsTargetPestValue, tvProductDetailsDoseValue, tvProductDetailsTimeOfAppValue, tvProductDetailsTotalAmountValue;
    String productId, hasWishlist, productType;
    Button btnProductDetailsAddToCart;
    RadioGroup rgProductDetailsVariationOne;
    Boolean isWishlisted;
    ConstraintLayout  ivHomeListProductVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);
        bindToolbar(context, R.string.product_details);
        productId = getIntent().getStringExtra("product_id");
        hasWishlist = getIntent().getStringExtra("wishlist_has");
        isWishlisted = getIntent().getBooleanExtra("is_wishlisted",false);
        productType = getIntent().getStringExtra("list_type");

        bindReferences();

    }

    private void bindReferences() {
        tvProductDetailsUspDescription = findViewById(R.id.ivHomeListProductDescription);
        tvProductDetailsTitle = findViewById(R.id.ivHomeListProductTitle);
        tvProductDetailsTechnicalValue = findViewById(R.id.tvProductDetailsTechnicalValue);
        tvProductDetailsTargetPestValue = findViewById(R.id.tvProductDetailsTargetPestValue);
        tvProductDetailsDoseValue = findViewById(R.id.tvProductDetailsDoseValue);
        tvProductDetailsTimeOfAppValue = findViewById(R.id.tvProductDetailsTimeOfAppValue);
        tvProductDetailsTotalAmountValue = findViewById(R.id.ivHomeListProductPrice);
        ivHomeListProductPoint = findViewById(R.id.ivHomeListProductPoint);
        ivProductDetailsImage = findViewById(R.id.ivHomeListProductImage);
        rgProductDetailsVariationOne = findViewById(R.id.rgProductDetailsVariationOne);
        btnProductDetailsAddToCart = findViewById(R.id.btnProductDetailsAddToCart);
        ivHomeListProductVideo = findViewById(R.id.ivHomeListProductVideo);

        ivHomeListProductVideo.setVisibility(View.GONE);


        ivHomeListProductWishlistIcon2 = findViewById(R.id.ivHomeListProductWishlistIcon2);
        ivHomeListProductWishlistIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishlist();
            }
        });

        ivHomeListProductWishlistAdded2 = findViewById(R.id.ivHomeListProductWishlistAdded2);
        //ivHomeListProductWishlistAdded2.setVisibility(View.GONE);
        ivHomeListProductWishlistAdded2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromWishlist();
            }
        });

        if (UserData.getKeyUserRoleId(context).equals("3")) {
            ivHomeListProductPoint.setVisibility(View.GONE);
            tvProductDetailsTotalAmountValue.setVisibility(View.VISIBLE);
        } else if (UserData.getKeyUserRoleId(context).equals("4")) {
            tvProductDetailsTotalAmountValue.setVisibility(View.GONE);
            ivHomeListProductPoint.setVisibility(View.VISIBLE);
        }


        if(isWishlisted){
            ivHomeListProductWishlistAdded2.setVisibility(View.VISIBLE);
            ivHomeListProductWishlistIcon2.setVisibility(View.GONE);
        }else {
            ivHomeListProductWishlistAdded2.setVisibility(View.GONE);
        }


        downloadNewsItems();
    }

    private void downloadNewsItems() {
        if (getIntent().hasExtra("product_id")) {
            productId = getIntent().getStringExtra("product_id");
            new VolleyService(context).withCallbacks(new IVolleyResult() {
                @Override
                public void notifyRequestQueued(String requestName) {
                }

                @Override
                public void notifySuccess(String requestName, JSONObject response) {

                    if (!response.optString("name").isEmpty()) {
                        tvProductDetailsTitle.setText(response.optString("name"));
                    }
                    if (!response.optString("technical").isEmpty()) {
                        tvProductDetailsTechnicalValue.setText(response.optString("technical"));
                    }
                    if (!response.optString("target_pest").isEmpty()) {
                        tvProductDetailsTargetPestValue.setText(response.optString("target_pest"));
                    }
                    if (!response.optString("dose_acre").isEmpty()) {
                        tvProductDetailsDoseValue.setText(response.optString("dose_acre"));
                    }
                    if (!response.optString("time_of_application").isEmpty()) {
                        tvProductDetailsTimeOfAppValue.setText(response.optString("time_of_application"));
                    }
                    if (!response.optString("selling_price").isEmpty()) {
                        tvProductDetailsTotalAmountValue.setText("₹" + response.optString("selling_price"));
                    }
                    if (!response.optString("reward_points").isEmpty()) {
                        ivHomeListProductPoint.setText(response.optString("reward_points") + " Point");
                    }
                    if (!response.optString("usp").isEmpty()) {
                        tvProductDetailsUspDescription.setText(response.optString("usp"));
                    }
                    //w("Users",response.optString("youtube_video_id"));
                    //if (!response.optString("youtube_url").equals("")) {
                    if (response.optString("youtube_video_id") != null && !response.optString("youtube_video_id").isEmpty() && !response.optString("youtube_video_id").equals("null") ) {
                        ivHomeListProductVideo.setVisibility(View.VISIBLE);
                        ivHomeListProductVideo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(context, SingleProductVideoActivity.class);
                                intent.putExtra("youtube_link", response.optString("youtube_video_id"));
                                open(context, intent, true);
                            }
                        });
                    }

//                            Picasso.get()
//                                    .load(response.optString("image_url"))
//                                    .into(ivProductDetailsImage);
                    Picasso.get().load(response.optString("image_url")).into(ivProductDetailsImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            ivProductDetailsImage.setTransitionName("productImageTransition");
                            getWindow().setSharedElementEnterTransition(TransitionInflater.from(context).inflateTransition(R.transition.shared_element_transition));
                        }

                        @Override
                        public void onError(Exception e) {

                        }


                    });

                    ArrayList<ProductVariantModel> productVariantModelArrayList = new ArrayList<>();
                    rgProductDetailsVariationOne.removeAllViews();
                    JSONArray productVariants = response.optJSONArray("product_variants");
                    for (int i = 0; i < productVariants.length(); i++) {
                        JSONObject productVariant = productVariants.optJSONObject(i);

                        ProductVariantModel productVariantModel = new ProductVariantModel();
                        productVariantModel.setId(productVariant.optString("id"));
                        productVariantModel.setName(productVariant.optString("name"));
                        productVariantModel.setListingPrice(productVariant.optString("listing_price"));
                        productVariantModel.setSellingPrice(productVariant.optString("selling_price"));
                        productVariantModel.setRetailerListingPrice(productVariant.optString("retailer_listing_price"));
                        productVariantModel.setRetailerSellingPrice(productVariant.optString("retailer_selling_price"));
                        productVariantModel.setImageUrl(response.optString("image_url"));
                        if (productVariant.length() > 0) {
                            productVariantModel.setPoint(productVariant.optString("reward_points"));
                        } else {
                            productVariantModel.setPoint("0");
                        }


                        ArrayList<String> productVariantValues = new ArrayList<>();
                        for (int k = 0; k < productVariant.optJSONArray("values").length(); k++) {

                            JSONObject variantValue = productVariant.optJSONArray("values").optJSONObject(k);
                            productVariantValues.add(variantValue.optString("name"));
                        }

                        productVariantModel.setValues(productVariantValues);
                        productVariantModelArrayList.add(productVariantModel);


                        RadioGroup.LayoutParams layoutParam = (RadioGroup.LayoutParams) new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParam.setMargins(10, 0, 10, 0);

                        RadioButton radioButton = new RadioButton(context);
                        radioButton.setId(View.generateViewId());
                        radioButton.setText(productVariant.optJSONArray("values").optJSONObject(0).optString("name"));
                        radioButton.setLayoutParams(layoutParam);
                        radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.item_rounded_new_base_color));
                        radioButton.setButtonDrawable(ContextCompat.getDrawable(context, R.drawable.shape_food_mile_qtty_round));
                        radioButton.setTextSize(18);
                        radioButton.setPadding(5, 5, 5, 5);
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tvProductDetailsTotalAmountValue.setText("₹" + productVariant.optString("listing_price"));
                                ivHomeListProductPoint.setText(productVariant.optString("reward_points")  + " Point");
                            }
                        });

                        rgProductDetailsVariationOne.addView(radioButton);
                    }

                    btnProductDetailsAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.list_product_variant_layout);

                            RecyclerView rvVariantList = dialog.findViewById(R.id.rvVariantList);
                            Button btnDoneCatVariant = dialog.findViewById(R.id.btnDoneCatVariant);
                            btnDoneCatVariant.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                            rvVariantList.setLayoutManager(layoutManager);
                            if(productType.equals("buy_product")){
                                w(productType);
                                rvVariantList.setAdapter(new ProductVariantAdapter(context, productVariantModelArrayList));
                            }else{
                                w(productType);
                                rvVariantList.setAdapter(new ProductVariantRetailerBuyAdapter(context, productVariantModelArrayList));
                            }
                            rvVariantList.scheduleLayoutAnimation();

                            dialog.show();
                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                            dialog.getWindow().setGravity(Gravity.BOTTOM);
                        }
                    });
                }

                @Override
                public void notifySuccess(String requestName, JSONArray response) {
                }

                @Override
                public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                }

            }).get(API_PRODUCT_List + "/" + productId);
        }
    }

    private void addToWishlist() {
        new VolleyService(context)
                .withParam("token", UserData.getToken(context))
                .withParam("product_id", productId)
                .withCallbacks(new IVolleyResult() {
                    @Override
                    public void notifyRequestQueued(String requestName) {
                    }

                    @Override
                    public void notifySuccess(String requestName, JSONObject response) {

                    }

                    @Override
                    public void notifySuccess(String requestName, JSONArray response) {
                       ivHomeListProductWishlistAdded2.setVisibility(View.VISIBLE);
                       ivHomeListProductWishlistIcon2.setVisibility(View.GONE);
                        Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        // popupError(context, "Product already added to wishlist ");
                    }

                }).put(API_WISHLIST);

    }

    private void removeFromWishlist() {
            ivHomeListProductWishlistIcon2.setVisibility(View.VISIBLE);
            ivHomeListProductWishlistAdded2.setVisibility(View.GONE);
            new VolleyService(context)
                    .withParam("token", UserData.getToken(context))
                    .withParam("product_id", productId)
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
                        }

                        @Override
                        public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                        }

                    }).delete(API_WISHLIST);

    }
}