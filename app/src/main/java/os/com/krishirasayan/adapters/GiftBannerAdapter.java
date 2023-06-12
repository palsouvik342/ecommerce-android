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


public class GiftBannerAdapter extends RecyclerView.Adapter<GiftBannerAdapter.GiftViewHolder> {

    Context context;
    boolean selectable = false;
    List<GiftModel> allGiftList;
    ICallback iCallback;


    public GiftBannerAdapter(Context context, List<GiftModel> allGiftList) {
        this.context = context;
        this.allGiftList = allGiftList;
    }

    public GiftBannerAdapter(Context context, List<GiftModel> allGiftList, boolean selectable) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
    }

    public GiftBannerAdapter(Context context, List<GiftModel> allGiftList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_reward_banner_item, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {

        GiftModel listGift= allGiftList.get(position);
        //Picasso.get().load(listGift.getBannerImageUrl()).into(holder.ivPrizeOfferBanner);
        if(!listGift.getBannerImageUrl().isEmpty()) {
            Picasso.get().load( listGift.getBannerImageUrl())
                    .error( R.drawable.account_icon )
                    .placeholder( R.drawable.account_icon )
                    .into( holder.ivPrizeOfferBanner );
        }
    }

    @Override
    public int getItemCount() {
        return allGiftList.size();
    }


    public static final class GiftViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPrizeOfferBanner;
        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);

        }

    }

}
