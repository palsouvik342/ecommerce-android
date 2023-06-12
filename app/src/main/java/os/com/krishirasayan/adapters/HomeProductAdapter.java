package os.com.krishirasayan.adapters;


import android.app.Activity;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.SingleProductActivity;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.ProductModel;


public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ProductViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductModel> allProductList;
    ICallback iCallback;


    public HomeProductAdapter(Context context, List<ProductModel> allProductList) {
        this.context = context;
        this.allProductList = allProductList;
    }

    public HomeProductAdapter(Context context, List<ProductModel> allProductList, boolean selectable) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
    }

    public HomeProductAdapter(Context context, List<ProductModel> allProductList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_home_product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductModel listProduct= allProductList.get(position);
        if(!listProduct.getProductName().isEmpty()) {
            holder.ivHomeListProductTitle.setText(listProduct.getProductName());
        }
        holder.ivHomeListProductPrice.setText("\u20B9 "+ listProduct.getPrice());
        //holder.ivHomeListProductPoint.setText("Pt. " + listProduct.getPoint());
        if(!listProduct.getImageUrl().isEmpty()) {
            Picasso.get().load(listProduct.getImageUrl()).into(holder.ivHomeListProductImage);
        }

        //holder.ivHomeListProductPoint.setVisibility(View.GONE);
        //holder.ivHomeListProductPrice.setVisibility(View.GONE);
        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.ivHomeListProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            //holder.ivHomeListProductPoint.setVisibility(View.VISIBLE);
        }

        holder.buttonHomeProductAddToCart.setOnClickListener(new View.OnClickListener() {
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
                if(UserData.getKeyUserRoleId(context).equals("3")){
                    rvVariantList.setAdapter(new ProductVariantAdapter(context, listProduct.getProductVariantModel()));
                } else if(UserData.getKeyUserRoleId(context).equals("4")){
                    rvVariantList.setAdapter(new ProductVariantRetailerBuyAdapter(context, listProduct.getProductVariantModel()));
                }
                rvVariantList.scheduleLayoutAnimation();


//        rvVariantList

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
                //holder.ivHomeListProductDescription.setTransitionName("productUspTransition");
                //holder.ivHomeListProductPrice.setTransitionName("productPriceTransition");
                Pair<View, String> pair1 = Pair.create(holder.ivHomeListProductImage, holder.ivHomeListProductImage.getTransitionName());
                Pair<View, String> pair2 = Pair.create(holder.ivHomeListProductTitle, holder.ivHomeListProductTitle.getTransitionName());
                //Pair<View, String> pair3 = Pair.create(holder.ivHomeListProductDescription, holder.ivHomeListProductDescription.getTransitionName());
                //Pair<View, String> pair4 = Pair.create(holder.ivHomeListProductPrice, holder.ivHomeListProductPrice.getTransitionName());
                //Pair<View, String> pair5 = Pair.create(holder.ivHomeListProductPoint, holder.ivHomeListProductPoint.getTransitionName());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair1,pair2);
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


                //Intent intent = new Intent(context, SingleProductActivity.class);

                //open(context, intent, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHomeListProductImage;
        Button buttonHomeProductAddToCart;
        TextView ivHomeListProductTitle, ivHomeListProductPrice, ivHomeListProductPoint, ivHomeListProductAddToCartText;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            //Product list
            ivHomeListProductTitle = itemView.findViewById(R.id.ivHomeListProductTitle);
            ivHomeListProductPrice = itemView.findViewById(R.id.ivHomeListProductPrice);
            //ivHomeListProductPoint = itemView.findViewById(R.id.ivHomeListProductPoint);
            ivHomeListProductImage = itemView.findViewById(R.id.ivHomeListProductImage);
            buttonHomeProductAddToCart = itemView.findViewById(R.id.buttonHomeProductAddToCart);


            /*//Reward Banner List
            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);*/

        }

    }

}
