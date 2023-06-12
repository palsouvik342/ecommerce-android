package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.ProductVariantModel;


public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.ProductViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductVariantModel> allProductVariantList;
    ICallback iCallback;


    public ProductVariantAdapter(Context context, List<ProductVariantModel> allProductVariantList) {
        this.context = context;
        this.allProductVariantList = allProductVariantList;
    }

    public ProductVariantAdapter(Context context, List<ProductVariantModel> allProductVariantList, boolean selectable) {
        this.context = context;
        this.allProductVariantList = allProductVariantList;
        this.selectable = selectable;
    }

    public ProductVariantAdapter(Context context, List<ProductVariantModel> allProductVariantList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allProductVariantList = allProductVariantList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_cart_product, parent, false);
        return new ProductViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.btProductListCartQtyPlus.setVisibility(View.VISIBLE);
        holder.btProductListCartQtyMinus.setVisibility(View.GONE);

        ProductVariantModel productVariant = allProductVariantList.get(position);
        holder.tvCartProductTitle.setText(productVariant.getName());
        holder.tvCartProdutVariation.setText(productVariant.getVariantLabel());
        holder.tvCartProductPrice.setText("\u20B9 "+ productVariant.getListingPrice());
        holder.tvCartProductPoint.setText("\u20B9 "+ productVariant.getRetailerListingPrice());
        Picasso.get().load(productVariant.getImageUrl()).into(holder.ivCartProductImage);
        //holder.tvProductListCartQtyValue.setText(String.valueOf(productVariant.getQuantity()));

        holder.tvCartProductPoint.setVisibility(View.GONE);
        holder.tvCartProductPrice.setVisibility(View.GONE);
        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.tvCartProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            holder.tvCartProductPoint.setVisibility(View.VISIBLE);
        }

        ArrayList<JSONObject> recentCart = UserData.getCartItems(context);
        w(recentCart);
        if(recentCart != null) {
            Optional<JSONObject> filtered = recentCart.stream().filter(x -> x.optInt("product_variant_id") == Integer.parseInt(productVariant.getId())).findFirst();
            if(filtered.isPresent()) {
                Integer quantity = (int)filtered.get().optDouble("quantity");
                productVariant.setQuantity(quantity);
                //holder.tvProductListCartQtyValue.setText(String.valueOf(quantity));
                if(quantity > 0){
                    holder.btProductListCartQtyPlus.setVisibility(View.GONE);
                    holder.btProductListCartQtyMinus.setVisibility(View.VISIBLE);
                }
            }
        }

        holder.btProductListCartQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productVariant.setQuantity(productVariant.getQuantity() + 1);
                holder.tvProductListCartQtyValue.setText(String.valueOf(productVariant.getQuantity()));
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_variant_id", productVariant.getId())
                        .withParam("quantity", "1")
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {
                                UserData.setCart(context, response);
                                holder.btProductListCartQtyPlus.setVisibility(View.GONE);
                                holder.btProductListCartQtyMinus.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void notifySuccess(String requestName, JSONArray response) {

                            }

                            @Override
                            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                            }
                        })
                        .put(API_CUSTOMER_CART);
            }
        });

        holder.btProductListCartQtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* productVariant.setQuantity(productVariant.getQuantity() - 1);
                holder.tvProductListCartQtyValue.setText(String.valueOf(productVariant.getQuantity()));
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_variant_id", productVariant.getId())
                        .withParam("quantity", productVariant.getQuantity())
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {
                                UserData.setCart(context, response);
                                holder.btProductListCartQtyPlus.setVisibility(View.VISIBLE);
                                holder.btProductListCartQtyMinus.setVisibility(View.GONE);
                            }

                            @Override
                            public void notifySuccess(String requestName, JSONArray response) {

                            }

                            @Override
                            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                            }
                        })
                        .delete(API_CUSTOMER_CART); */
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductVariantList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvCartProductTitle, tvCartProdutVariation, tvCartProductPrice,tvCartProductPoint;
        Button btProductListCartQtyMinus, btProductListCartQtyPlus;
        ImageView ivCartProductImage;
        TextView tvProductListCartQtyValue;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCartProductTitle = itemView.findViewById(R.id.tvCartProductTitle);
            btProductListCartQtyMinus = itemView.findViewById(R.id.btProductListCartQtyMinus);
            btProductListCartQtyPlus = itemView.findViewById(R.id.btProductListCartQtyPlus);
            tvCartProdutVariation = itemView.findViewById(R.id.tvCartProdutVariation);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvCartProductPoint = itemView.findViewById(R.id.tvCartProductPoint);
            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);
            tvProductListCartQtyValue = itemView.findViewById(R.id.tvProductListCartQtyValue);
        }

    }

}
