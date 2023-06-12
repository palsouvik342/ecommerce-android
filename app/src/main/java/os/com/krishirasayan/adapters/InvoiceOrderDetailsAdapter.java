package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.NewsDetailsActivity;
import os.com.krishirasayan.activities.OrderDetailsActivity;
import os.com.krishirasayan.classes.UserData;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.NewsModel;
import os.com.krishirasayan.models.OrderModel;


public class InvoiceOrderDetailsAdapter extends RecyclerView.Adapter<InvoiceOrderDetailsAdapter.OrderViewHolder> {

    Context context;
    boolean selectable = false;
    List<OrderModel> allOrderList;
    ICallback iCallback;


    public InvoiceOrderDetailsAdapter(Context context, List<OrderModel> allOrderList) {
        this.context = context;
        this.allOrderList = allOrderList;
    }

    public InvoiceOrderDetailsAdapter(Context context, List<OrderModel> allOrderList, boolean selectable) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
    }

    public InvoiceOrderDetailsAdapter(Context context, List<OrderModel> allNewsList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_ordered_product_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        OrderModel listOrder= allOrderList.get(position);

        holder.ivOrderitemProductTitle.setText(listOrder.getOrderProductName());
        holder.ivOrderitemProductPrice.setText(listOrder.getOrderProductPrice());
        holder.ivOrderitemProductPoint.setText(listOrder.getOrderProductPoint());
        holder.tvOrderItemQtyValue.setText(listOrder.getOrderProductQty());
        holder.tvOrderItemVarValue.setText(listOrder.getOrderProductVariant());
        Picasso.get().load(listOrder.getImageUrl()).into(holder.ivOrderitemProductImage);


        if(UserData.getKeyUserRoleId(context).equals("3")){
            holder.ivOrderitemProductPoint.setVisibility(View.GONE);
            holder.ivOrderitemProductPrice.setVisibility(View.VISIBLE);
        } else if(UserData.getKeyUserRoleId(context).equals("4")){
            holder.ivOrderitemProductPrice.setVisibility(View.GONE);
            holder.ivOrderitemProductPoint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return allOrderList.size();
    }


    public static final class OrderViewHolder extends RecyclerView.ViewHolder {

        ImageView ivOrderitemProductImage;
        TextView ivOrderitemProductPoint, ivOrderitemProductTitle, ivOrderitemProductPrice, tvOrderItemQtyValue, tvOrderItemVarValue, tvOrderDistributorValue;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderitemProductTitle = itemView.findViewById(R.id.ivOrderitemProductTitle);
            ivOrderitemProductPrice = itemView.findViewById(R.id.ivOrderitemProductPrice);
            ivOrderitemProductPoint = itemView.findViewById(R.id.ivOrderitemProductPoint);
            tvOrderItemQtyValue = itemView.findViewById(R.id.tvOrderItemQtyValue);
            tvOrderItemVarValue = itemView.findViewById(R.id.tvOrderItemVarValue);
            ivOrderitemProductImage = itemView.findViewById(R.id.ivOrderitemProductImage);
        }

    }

}
