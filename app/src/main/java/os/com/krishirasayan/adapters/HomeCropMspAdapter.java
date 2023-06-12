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
import os.com.krishirasayan.activities.OrderDetailsActivity;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.CropMspModel;
import os.com.krishirasayan.models.OrderModel;


public class HomeCropMspAdapter extends RecyclerView.Adapter<HomeCropMspAdapter.OrderViewHolder> {

    Context context;
    boolean selectable = false;
    List<CropMspModel> CropMspList;
    ICallback iCallback;


    public HomeCropMspAdapter(Context context, List<CropMspModel> CropMspList) {
        this.context = context;
        this.CropMspList = CropMspList;
    }

    public HomeCropMspAdapter(Context context, List<CropMspModel> CropMspList, boolean selectable) {
        this.context = context;
        this.CropMspList = CropMspList;
        this.selectable = selectable;
    }

    public HomeCropMspAdapter(Context context, List<CropMspModel> CropMspList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.CropMspList = CropMspList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_crop_msp_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        CropMspModel listOrder= CropMspList.get(position);

        holder.tvCropMspName.setText(listOrder.getName());
        //holder.tvCropMspRate.setText("₹ " + listOrder.getMspRate() + " / per " + listOrder.getMspUnit());
        //holder.tvCropMspYear.setText("Year: " + listOrder.getMspYear());
        //Picasso.get().load(listOrder.getImageUrl()).into(holder.ivCropMspIcon);
        if(!listOrder.getImageUrl().isEmpty()) {
            Picasso.get().load( listOrder.getImageUrl())
                    .error( R.drawable.account_icon )
                    .placeholder( R.drawable.account_icon )
                    .into( holder.ivCropMspIcon );
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(context, listOrder.getId(), Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return CropMspList.size();
    }


    public static final class OrderViewHolder extends RecyclerView.ViewHolder {

        //ImageView ivBlogListImage;
        TextView tvCropMspName;
        ImageView ivCropMspIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCropMspIcon = itemView.findViewById(R.id.ivCropMspIcon);
            tvCropMspName = itemView.findViewById(R.id.tvCropMspName);
            //tvCropMspRate = itemView.findViewById(R.id.tvCropMspRate);
            //tvCropMspYear = itemView.findViewById(R.id.tvCropMspYear);
        }

    }

}
