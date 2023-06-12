package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.w;
import static os.com.krishirasayan.consts.Url.API_CUSTOMER_CART;
import static os.com.krishirasayan.consts.Url.API_RETAILER_CART;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.SingleProductActivity;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.classes.VolleyService;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.interfaces.IVolleyResult;
import os.com.krishirasayan.models.ProductModel;


public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ProductViewHolder> {

    Context context;
    boolean selectable = false;
    List<ProductModel> allProductList;
    ICallback iCallback;


    public CartListAdapter(Context context, List<ProductModel> allProductList) {
        this.context = context;
        this.allProductList = allProductList;
    }

    public CartListAdapter(Context context, List<ProductModel> allProductList, boolean selectable) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
    }

    public CartListAdapter(Context context, List<ProductModel> allProductList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allProductList = allProductList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_cart_page_product_items, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        ProductModel listProduct= allProductList.get(position);
        if(!listProduct.getProductName().isEmpty()) {
            holder.tvCartProductTitle.setText(listProduct.getProductName());
        }
       if(!listProduct.getProductQuantity().isEmpty()) {
            holder.tvProductListCartQtyValue.setText(listProduct.getProductQuantity());
        }
       if(!listProduct.getProductVariant().isEmpty()) {
            holder.tvCartProdutVariation.setText(listProduct.getProductVariant());
        }
        if(!listProduct.getPrice().isEmpty()) {
            if(UserData.getKeyUserRoleId(context).equals("3")) {
                holder.tvCartProductPrice.setText("₹" + listProduct.getPrice());
            } else if(UserData.getKeyUserRoleId(context).equals("4")){
                holder.tvCartProductPrice.setText("₹" + listProduct.getRetailerPrice());
            }
        }

        if(!listProduct.getPrice().isEmpty()) {
            holder.tvCartProductPoint.setText(listProduct.getPoint());
        }
        if(!listProduct.getImageUrl().isEmpty()) {
            Picasso.get().load(listProduct.getImageUrl()).into(holder.ivCartProductImage);
        }

        holder.tvCartProductPoint.setVisibility(View.GONE);
        holder.tvProductListCartQtyValue.setEnabled(false);

        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.tvCartProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            holder.tvCartProductPrice.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.tvCartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_variant_id", listProduct.getId())
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {
                                UserData.setCart(context, response);
                                iCallback.function(null);
                            }

                            @Override
                            public void notifySuccess(String requestName, JSONArray response) {

                            }

                            @Override
                            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                            }
                        })
                        .delete(API_CUSTOMER_CART);
            }
        });

        holder.btProductListCartQtyPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer newQuantity = (int)Double.parseDouble(listProduct.getProductQuantity()) + 1;
                if(newQuantity > 99) newQuantity = 99;
                holder.tvProductListCartQtyValue.setText(String.valueOf(newQuantity));
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_variant_id", listProduct.getId())
                        .withParam("quantity", "1")
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {
                                UserData.setCart(context, response);
                                iCallback.function(null);
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
                Integer newQuantity = (int)Double.parseDouble(listProduct.getProductQuantity()) - 1;
                if(newQuantity < 0) newQuantity = 0;
                holder.tvProductListCartQtyValue.setText(String.valueOf(newQuantity));
                new VolleyService(context)
                        .withParam("token", UserData.getToken(context))
                        .withParam("product_variant_id", listProduct.getId())
                        .withParam("quantity", "1")
                        .withCallbacks(new IVolleyResult() {
                            @Override
                            public void notifyRequestQueued(String requestName) {

                            }

                            @Override
                            public void notifySuccess(String requestName, JSONObject response) {
                                UserData.setCart(context, response);
                                iCallback.function(null);
                            }

                            @Override
                            public void notifySuccess(String requestName, JSONArray response) {

                            }

                            @Override
                            public void notifyError(String requestName, VolleyError error, String statusCode, String responseData) {

                            }
                        })
                        .delete(API_CUSTOMER_CART);
            }
        });

        holder.tvProductListCartQtyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String qty = String.valueOf(holder.tvProductListCartQtyValue.getText());
                w("Users",qty);
                listProduct.setProductQuantity(qty);
                allProductList.set(position, listProduct);
                iCallback.function(allProductList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductList.size();
    }


    public static final class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCartProductImage;
        TextView tvCartProductTitle, tvCartProductPrice, tvCartProductPoint;


        TextView tvCartProdutVariation, tvCartRemove;

        EditText tvProductListCartQtyValue;
        Button btProductListCartQtyMinus, btProductListCartQtyPlus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);


            //Cart list
            tvCartProductTitle = itemView.findViewById(R.id.tvCartProductTitle);
            tvProductListCartQtyValue = itemView.findViewById(R.id.tvProductListCartQtyValue);
            tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            tvCartProductPoint = itemView.findViewById(R.id.tvCartProductPoint);
            ivCartProductImage = itemView.findViewById(R.id.ivCartProductImage);
            tvCartRemove = itemView.findViewById(R.id.tvCartRemove);
            tvCartProdutVariation = itemView.findViewById(R.id.tvCartProdutVariation);
            btProductListCartQtyMinus = itemView.findViewById(R.id.btProductListCartQtyMinus);
            btProductListCartQtyPlus = itemView.findViewById(R.id.btProductListCartQtyPlus);

        }

    }

}
