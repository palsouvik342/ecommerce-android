package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_WISHLIST;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.AuthActivity;
import os.com.krishirasayan.activities.ProductListActivity;
import os.com.krishirasayan.activities.SingleProductActivity;
import os.com.krishirasayan.activities.SplashActivity;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.ProductModel;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductModel> allProductList;
    ICallback iCallback;


    public ProductAdapter(Context context, List<ProductModel> allProductList) {
        this.context = context;
        this.allProductList = allProductList;
    }

    public ProductAdapter(Context context, List<ProductModel> allProductList, boolean selectable) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
    }

    public ProductAdapter(Context context, List<ProductModel> allProductList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.ivHomeListProductWishlistAdded.setVisibility(View.GONE);
        ProductModel listProduct= allProductList.get(position);
           // w(listProduct.getProductWishlist());
        if(!listProduct.getProductName().isEmpty()) {
            holder.ivHomeListProductTitle.setText(listProduct.getProductName());
        }
        if(!listProduct.getDescription().isEmpty()) {
            holder.ivHomeListProductDescription.setText(listProduct.getDescription());
        }
        holder.ivHomeListProductPrice.setText("\u20B9 "+ listProduct.getPrice());
        holder.ivHomeListProductPoint.setText("\u20B9 "+ listProduct.getRetailerPrice());
        if(!listProduct.getImageUrl().isEmpty()) {
            Picasso.get().load(listProduct.getImageUrl()).into(holder.ivHomeListProductImage);
        }

        holder.ivHomeListProductPoint.setVisibility(View.GONE);
        holder.ivHomeListProductPrice.setVisibility(View.GONE);
        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.ivHomeListProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            holder.ivHomeListProductPoint.setVisibility(View.VISIBLE);
        }

        if(listProduct.getProductWishlisted()){
            holder.ivHomeListProductWishlistAdded.setVisibility(View.VISIBLE);
            holder.ivHomeListProductWishlistIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.ivHomeListProductWishlistAdded.setVisibility(View.INVISIBLE);
            holder.ivHomeListProductWishlistIcon.setVisibility(View.VISIBLE);
        }

        holder.buttonProductAddToCart.setOnClickListener(new View.OnClickListener() {
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
                rvVariantList.setAdapter(new ProductVariantAdapter(context, listProduct.getProductVariantModel()));
                rvVariantList.scheduleLayoutAnimation();

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.ivHomeListProductImage.setTransitionName("productImageTransition");
                holder.ivHomeListProductTitle.setTransitionName("productTitleTransition");
                holder.ivHomeListProductDescription.setTransitionName("productUspTransition");
                holder.ivHomeListProductPrice.setTransitionName("productPriceTransition");
                Pair<View, String> pair1 = Pair.create(holder.ivHomeListProductImage, holder.ivHomeListProductImage.getTransitionName());
                Pair<View, String> pair2 = Pair.create(holder.ivHomeListProductTitle, holder.ivHomeListProductTitle.getTransitionName());
                Pair<View, String> pair3 = Pair.create(holder.ivHomeListProductDescription, holder.ivHomeListProductDescription.getTransitionName());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1,pair2,pair3);
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("product_id", listProduct.getId());
                if(listProduct.getProductWishlisted()){
                    intent.putExtra("wishlist_has", "1");
                }else {
                    intent.putExtra("wishlist_has", "0");
                }
                intent.putExtra("list_type", "buy_product");
                intent.putExtra("is_wishlisted", listProduct.getProductWishlisted());
                context.startActivity(intent, optionsCompat.toBundle());
            }
        });

        holder.ivHomeListProductWishlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("product_id", listProduct.getId());
                open(context, intent, true);*/

                //holder.ivHomeListProductWishlistIcon.setVisibility(View.GONE);
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_id",listProduct.getId())
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {
                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONArray response) {
                                holder.ivHomeListProductWishlistAdded.setVisibility(View.VISIBLE);
                                holder.ivHomeListProductWishlistIcon.setVisibility(View.GONE);
                                Toast.makeText(context, "Added to wishlist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {
                                // popupError(context, "Product already added to wishlist ");
                            }

                        }).put(API_WISHLIST);
            }
        });

        holder.ivHomeListProductWishlistAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("product_id", listProduct.getId());
                open(context, intent, true);*/
                holder.ivHomeListProductWishlistIcon.setVisibility(View.VISIBLE);
                holder.ivHomeListProductWishlistAdded.setVisibility(View.GONE);
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_id",listProduct.getId())
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
        });
    }

    @Override
    public int getItemCount() {
        return allProductList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHomeListProductImage, ivHomeListProductWishlistIcon, ivHomeListProductWishlistAdded;
        TextView ivHomeListProductTitle, ivHomeListProductDescription, ivHomeListProductPrice, ivHomeListProductPoint, ivHomeListProductAddToCartText;
        Button buttonProductAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            //Product list
            ivHomeListProductTitle = itemView.findViewById(R.id.ivHomeListProductTitle);
            ivHomeListProductDescription = itemView.findViewById(R.id.ivHomeListProductDescription);
            ivHomeListProductPrice = itemView.findViewById(R.id.ivHomeListProductPrice);
            ivHomeListProductPoint = itemView.findViewById(R.id.ivHomeListProductPoint);
            ivHomeListProductImage = itemView.findViewById(R.id.ivHomeListProductImage);
            ivHomeListProductWishlistIcon = itemView.findViewById(R.id.ivHomeListProductWishlistIcon);
            ivHomeListProductWishlistAdded = itemView.findViewById(R.id.ivHomeListProductWishlistAdded);
            //ivHomeListProductAddToCartText = itemView.findViewById(R.id.ivHomeListProductAddToCartText);
            buttonProductAddToCart = itemView.findViewById(R.id.buttonProductAddToCart);

            //ivHomeListProductWishlistAdded.setAlpha(0);
            /*//Reward Banner List
            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);*/

        }

    }

}
