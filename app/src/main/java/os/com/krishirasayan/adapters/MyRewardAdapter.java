package os.com.krishirasayan.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.OrderModel;


public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.OrderViewHolder> {

    Context context;
    boolean selectable = false;
    List<OrderModel> allOrderList;
    ICallback iCallback;


    public MyRewardAdapter(Context context, List<OrderModel> allOrderList) {
        this.context = context;
        this.allOrderList = allOrderList;
    }

    public MyRewardAdapter(Context context, List<OrderModel> allOrderList, boolean selectable) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
    }

    public MyRewardAdapter(Context context, List<OrderModel> allNewsList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allOrderList = allOrderList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_myreward_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        OrderModel listOrder= allOrderList.get(position);

        holder.tvOrderIdLabel.setText(listOrder.getOrderNumber());
        holder.tvOrderDate.setText(listOrder.getOrderedTime());
        holder.tvOrderPointEarned.setText(listOrder.getTotalPoints());
        holder.tvOrderTotalPrice.setText(" ");
    }

    @Override
    public int getItemCount() {
        return allOrderList.size();
    }


    public static final class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderIdLabel, tvOrderDate, tvOrderPointEarned, tvOrderTotalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderIdLabel = itemView.findViewById(R.id.tvOrderIdLabel);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderPointEarned = itemView.findViewById(R.id.tvOrderPointEarned);
            tvOrderTotalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
        }

    }

}
