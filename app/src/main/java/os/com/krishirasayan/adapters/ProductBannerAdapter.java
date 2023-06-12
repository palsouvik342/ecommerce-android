package os.com.krishirasayan.adapters;


import android.content.Context;
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
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.ProductModel;


public class ProductBannerAdapter extends RecyclerView.Adapter<ProductBannerAdapter.ProductViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductModel> allProductList;
    ICallback iCallback;


    public ProductBannerAdapter(Context context, List<ProductModel> allProductList) {
        this.context = context;
        this.allProductList = allProductList;
    }

    public ProductBannerAdapter(Context context, List<ProductModel> allProductList, boolean selectable) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
    }

    public ProductBannerAdapter(Context context, List<ProductModel> allProductList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_product_banner_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductModel listProduct= allProductList.get(position);
        Picasso.get().load(listProduct.getBannerImageUrl()).into(holder.ivProductBanner);
    }

    @Override
    public int getItemCount() {
        return allProductList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductBanner;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProductBanner = itemView.findViewById(R.id.ivProductBanner);

        }

    }

}
