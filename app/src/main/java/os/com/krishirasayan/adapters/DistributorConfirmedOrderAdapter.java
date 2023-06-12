package os.com.krishirasayan.adapters;

import static os.com.krishirasayan.consts.Helper.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.OrderDetailsActivity;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.OrderModel;

public class DistributorConfirmedOrderAdapter extends RecyclerView.Adapter<DistributorConfirmedOrderAdapter.OrderViewHolder>{
    Context context;
    boolean selectable = false;
    List<OrderModel> allOrderList;
    ICallback iCallback;


    public DistributorConfirmedOrderAdapter(Context context, List<OrderModel> allOrderList) {
        this.context = context;
        this.allOrderList = allOrderList;
    }

    public DistributorConfirmedOrderAdapter(Context context, List<OrderModel> allOrderList, boolean selectable) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
    }

    public DistributorConfirmedOrderAdapter(Context context, List<OrderModel> allNewsList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public DistributorConfirmedOrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_order_item_main, parent, false);
        return new DistributorConfirmedOrderAdapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorConfirmedOrderAdapter.OrderViewHolder holder, int position) {

        OrderModel listOrder= allOrderList.get(position);

        holder.tvOrderedProductTitle.setText(listOrder.getOrderNumber());
        holder.tvOrderedProdutConfirmedDate.setText(listOrder.getOrderedTime());
        holder.tvOrderedProdutCurrentStatus.setText(listOrder.getStatus());
        holder.tvOrderedProdutCurrentStatusDate.setText(listOrder.getOrderedUpdateTime());
        holder.tvOrderedProdutPriceValue.setText(listOrder.getTotalPrice());

        holder.btOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(context, listOrder.getId(), Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order_id", listOrder.getId());
                intent.putExtra("confirmed_order", "true");
                open(context, intent, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allOrderList.size();
    }


    public static final class OrderViewHolder extends RecyclerView.ViewHolder {

        //ImageView ivBlogListImage;
        Button btOrderDetails;
        TextView tvOrderedProductTitle, tvOrderedProdutConfirmedDate, tvOrderedProdutCurrentStatus, tvOrderedProdutCurrentStatusDate, tvOrderedProdutPriceValue;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderedProductTitle = itemView.findViewById(R.id.tvOrderedProductTitle);
            tvOrderedProdutConfirmedDate = itemView.findViewById(R.id.tvOrderedProdutConfirmedDate);
            tvOrderedProdutCurrentStatusDate = itemView.findViewById(R.id.tvOrderedProdutCurrentStatusDate);
            tvOrderedProdutCurrentStatus = itemView.findViewById(R.id.tvOrderedProdutCurrentStatus);
            btOrderDetails = itemView.findViewById(R.id.btOrderDetails);
            tvOrderedProdutPriceValue = itemView.findViewById(R.id.tvOrderedProdutPriceValue);
            //ivBlogListImage = itemView.findViewById(R.id.ivBlogListImage);
        }

    }

}
