package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.NewsDetailsActivity;
import os.com.krishirasayan.activities.WishlistActivity;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.ProductModel;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductModel> allWishList;
    ICallback iCallback;


    public WishlistAdapter(Context context, List<ProductModel> allWishList) {
        this.context = context;
        this.allWishList = allWishList;
    }

    public WishlistAdapter(Context context, List<ProductModel> allWishList, boolean selectable) {
        this.context = context;
        this.allWishList = allWishList;
        this.selectable = selectable;
    }

    public WishlistAdapter(Context context, List<ProductModel> allWishList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allWishList = allWishList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_product_wishlist_item, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {

        ProductModel wishList= allWishList.get(position);
        holder.tvWishListProductTitle.setText(wishList.getProductName());
        holder.tvWishListProductDescription.setText(wishList.getDescription());
        holder.tvWishListProductPrice.setText("₹" + wishList.getPrice());
        holder.tvWishListProductPoint.setText("Pt. " + wishList.getPoint());
        //Picasso.get().load(wishList.getImageUrl()).into(holder.ivWishListProductImage);
        if(!wishList.getImageUrl().isEmpty()) {
            Picasso.get().load( wishList.getImageUrl()).into( holder.ivWishListProductImage );
        }

        holder.tvWishListProductPoint.setVisibility(View.GONE);
        holder.tvWishListProductPrice.setVisibility(View.GONE);
        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.tvWishListProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            holder.tvWishListProductPoint.setVisibility(View.VISIBLE);
        }

        holder.ivWishListProductRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, wishList.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, WishlistActivity.class);
                intent.putExtra("wishlist_id", wishList.getId());
                //context.startActivity(i);
                open(context, intent, true);
            }
        });

        holder.ivWishListProductAddToCartText.setOnClickListener(new View.OnClickListener() {
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
                rvVariantList.setAdapter(new ProductVariantAdapter(context, wishList.getProductVariantModel()));
                rvVariantList.scheduleLayoutAnimation();


//        rvVariantList

                dialog.show();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);

            }
        });
    }

    @Override
    public int getItemCount() {
        return allWishList.size();
    }


    public static final class WishlistViewHolder extends RecyclerView.ViewHolder {

        TextView tvWishListProductTitle, tvWishListProductDescription, tvWishListProductPrice, ivWishListProductAddToCartText, tvWishListProductPoint;
        ImageView ivWishListProductImage, ivWishListProductRemove;
        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWishListProductTitle = itemView.findViewById(R.id.tvWishListProductTitle);
            tvWishListProductDescription = itemView.findViewById(R.id.tvWishListProductDescription);
            tvWishListProductPrice = itemView.findViewById(R.id.tvWishListProductPrice);
            tvWishListProductPoint = itemView.findViewById(R.id.tvWishListProductPoint);
            ivWishListProductImage = itemView.findViewById(R.id.ivWishListProductImage);
            ivWishListProductRemove = itemView.findViewById(R.id.ivWishListProductRemove);
            ivWishListProductAddToCartText = itemView.findViewById(R.id.ivWishListProductAddToCartText);

        }

    }

}
