package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.NewsDetailsActivity;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.NewsModel;


public class NewsFeatureAdapter extends RecyclerView.Adapter<NewsFeatureAdapter.AddressViewHolder> {

    Context context;
    boolean selectable = false;
    List<NewsModel> allNewsList;
    ICallback iCallback;


    public NewsFeatureAdapter(Context context, List<NewsModel> allNewsList) {
        this.context = context;
        this.allNewsList = allNewsList;
    }

    public NewsFeatureAdapter(Context context, List<NewsModel> allNewsList, boolean selectable) {
        this.context = context;
        this.allNewsList = allNewsList;
        this.selectable = selectable;
    }

    public NewsFeatureAdapter(Context context, List<NewsModel> allNewsList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allNewsList = allNewsList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       // View view = LayoutInflater.from(context).inflate(R.layout.item_account_address, parent, false);
       // View view = LayoutInflater.from(context).inflate(R.layout.list_news_item, parent, false);
       View view = LayoutInflater.from(context).inflate(R.layout.list_news_featured_item, parent, false);
       //  View view = LayoutInflater.from(context).inflate(R.layout.list_product_list_item, parent, false);
       //  View view = LayoutInflater.from(context).inflate(R.layout.list_cart_product, parent, false);
        // View view = LayoutInflater.from(context).inflate(R.layout.list_gift_item_reward, parent, false);
        // View view = LayoutInflater.from(context).inflate(R.layout.list_address_item_menu, parent, false);
       //  View view = LayoutInflater.from(context).inflate(R.layout.list_address_checkout_page, parent, false);
       //  View view = LayoutInflater.from(context).inflate(R.layout.list_reward_banner_item, parent, false);
       //  View view = LayoutInflater.from(context).inflate(R.layout.list_product_wishlist_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        NewsModel listNews= allNewsList.get(position);

        holder.tvFeatureBlogTitle.setText(listNews.getContactName());
        //Picasso.get().load(listNews.getImageUrl()).into(holder.ivFeatureBlogImgae);
        if(!listNews.getImageUrl().isEmpty()) {
            Picasso.get().load( listNews.getImageUrl())
                    .error( R.drawable.account_icon )
                    .placeholder( R.drawable.account_icon )
                    .into( holder.ivFeatureBlogImgae );
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailsActivity.class);
                intent.putExtra("news_id", listNews.getId());
                open(context, intent, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allNewsList.size();
    }


    public static final class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView tvContact, tvAddress, tvLocation;

        ImageView ivBlogListImage;
        TextView tvBlogListTitle;

        ImageView ivFeatureBlogImgae;
        TextView tvFeatureBlogTitle;

        ImageView ivHomeListProductImage;
        TextView ivHomeListProductPrice, ivHomeListProductTitle, ivHomeListProductDescription;

        ImageView ivWishListProductImage;
        TextView ivWishListProductPrice, ivWishListProductTitle, ivWishListProductDescription, ivWishListProductRemove;

        ImageView ivCartProductImage;
        TextView tvCartProductTitle, tvProductListCartQtyValue, tvCartRemove, tvCartProductPrice, tvCartProdutVariation;


        ImageView ivGiftImage;
        TextView tvPrizeListTitle, tvPrizeListDescription, tvPrizeListNeededPoints;

        ImageView ivAddressListImage;
        TextView tvAddressListValue;

        TextView tvCheckoutAddressText;

        ImageView ivPrizeOfferBanner;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            /* //address list
            tvContact = itemView.findViewById(R.id.tvContact);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLocation = itemView.findViewById(R.id.tvLocation);*/

            /* //news list
            tvBlogListTitle = itemView.findViewById(R.id.tvBlogListTitle);
            ivBlogListImage = itemView.findViewById(R.id.ivBlogListImage);*/

            //Featured news list
            tvFeatureBlogTitle = itemView.findViewById(R.id.tvFeatureBlogTitle);
            ivFeatureBlogImgae = itemView.findViewById(R.id.ivFeatureBlogImgae);

            /* //All Product list
            ivHomeListProductTitle = itemView.findViewById(R.id.ivHomeListProductTitle);
            ivHomeListProductDescription = itemView.findViewById(R.id.ivHomeListProductDescription);
            ivHomeListProductPrice = itemView.findViewById(R.id.ivHomeListProductPrice);
            ivHomeListProductImage = itemView.findViewById(R.id.ivHomeListProductImage);*/

             /* //My Cart list
            tvCartProductTitle = itemView.findViewById(R.id.tvCartProductTitle);
            tvProductListCartQtyValue = itemView.findViewById(R.id.tvProductListCartQtyValue);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvCartProdutVariation = itemView.findViewById(R.id.tvCartProdutVariation);
            tvCartRemove = itemView.findViewById(R.id.tvCartRemove);
            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);*/

             /* //My Wishlist
            ivWishListProductTitle = itemView.findViewById(R.id.ivWishListProductTitle);
            ivWishListProductPrice = itemView.findViewById(R.id.ivWishListProductPrice);
            ivWishListProductDescription = itemView.findViewById(R.id.ivWishListProductDescription);
            ivWishListProductRemove = itemView.findViewById(R.id.ivWishListProductRemove);
            ivWishListProductImage = itemView.findViewById(R.id.ivWishListProductImage);*/

            /* //Gift list
            tvPrizeListTitle = itemView.findViewById(R.id.tvPrizeListTitle);
            tvPrizeListDescription = itemView.findViewById(R.id.tvPrizeListDescription);
            tvPrizeListNeededPoints = itemView.findViewById(R.id.tvPrizeListNeededPoints);
            ivGiftImage = itemView.findViewById(R.id.ivGiftImage);*/

            /* //Address Menu list
            tvAddressListValue = itemView.findViewById(R.id.tvAddressListValue);
            ivAddressListImage = itemView.findViewById(R.id.ivAddressListImage);*/

            /* //Address Checkout List
            tvCheckoutAddressText = itemView.findViewById(R.id.tvCheckoutAddressText);*/

            /*//Reward Banner List
            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);*/

        }

    }

}
