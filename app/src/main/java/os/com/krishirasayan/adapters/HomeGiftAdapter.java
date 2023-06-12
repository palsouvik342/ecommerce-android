package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.w;

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


public class HomeGiftAdapter extends RecyclerView.Adapter<HomeGiftAdapter.GiftViewHolder> {

    Context context;
    boolean selectable = false;
    List<GiftModel> allGiftList;
    ICallback iCallback;


    public HomeGiftAdapter(Context context, List<GiftModel> allGiftList) {
        this.context = context;
        this.allGiftList = allGiftList;
    }

    public HomeGiftAdapter(Context context, List<GiftModel> allGiftList, boolean selectable) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
    }

    public HomeGiftAdapter(Context context, List<GiftModel> allGiftList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_home_gift_item_reward, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {

        GiftModel listGift= allGiftList.get(position);
        holder.tvPrizeListTitle.setText(listGift.getName());
        holder.tvPrizeListNeededPoints.setText(String.valueOf(listGift.getPoints()));
       //Picasso.get().load(listGift.getImageUrl()).into(holder.ivGiftImage);
        if(!listGift.getImageUrl().isEmpty()) {
            w("Users",listGift.getImageUrl() );
        Picasso.get().load( listGift.getImageUrl())
                .error( R.drawable.account_icon )
                .placeholder( R.drawable.account_icon )
                .into( holder.ivGiftImage );
        }
    }

    @Override
    public int getItemCount() {
        return allGiftList.size();
    }


    public static final class GiftViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGiftImage;
        TextView tvPrizeListTitle, tvPrizeListNeededPoints;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);


             //Gift list
            tvPrizeListTitle = itemView.findViewById(R.id.tvPrizeListTitle);
            tvPrizeListNeededPoints = itemView.findViewById(R.id.tvPrizeListNeededPoints);
            ivGiftImage = itemView.findViewById(R.id.ivGiftImage);


            /*//Reward Banner List
            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);*/

        }

    }

}
