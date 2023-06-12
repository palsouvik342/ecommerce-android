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
import os.com.krishirasayan.models.GiftModel;


public class MyGiftAdapter extends RecyclerView.Adapter<MyGiftAdapter.GiftViewHolder> {

    Context context;
    boolean selectable = false;
    List<GiftModel> allGiftList;
    ICallback iCallback;


    public MyGiftAdapter(Context context, List<GiftModel> allGiftList) {
        this.context = context;
        this.allGiftList = allGiftList;
    }

    public MyGiftAdapter(Context context, List<GiftModel> allGiftList, boolean selectable) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
    }

    public MyGiftAdapter(Context context, List<GiftModel> allGiftList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_my_gift_single_item, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {

        GiftModel listGift= allGiftList.get(position);
        holder.tvPrizeListTitle.setText(listGift.getName());
        holder.tvPrizeListNeededPoints.setText(String.valueOf(listGift.getPoints()));
        holder.tvPrizeQuantity.setText(String.valueOf(listGift.getQuantity()));

        /*Picasso.get().load(R.drawable.account_icon)
                .error( R.drawable.account_icon )
                .placeholder( R.drawable.account_icon )
                .into( holder.ivGiftImage );*/
    }

    @Override
    public int getItemCount() {
        return allGiftList.size();
    }


    public static final class GiftViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGiftImage;
        TextView tvPrizeQuantity, tvPrizeListTitle, tvPrizeListNeededPoints;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);


             //Gift list
            tvPrizeListTitle = itemView.findViewById(R.id.tvPrizeListTitle);
            tvPrizeListNeededPoints = itemView.findViewById(R.id.tvPrizeListNeededPoints);
            tvPrizeQuantity = itemView.findViewById(R.id.tvPrizeQuantity);
            ivGiftImage = itemView.findViewById(R.id.ivGiftImage);


        }

    }

}
