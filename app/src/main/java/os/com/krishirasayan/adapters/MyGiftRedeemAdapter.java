package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Config.COLOR_BG_GRAY;
import static os.com.krishirasayan.consts.Config.COLOR_BG_GREEN;
import static os.com.krishirasayan.consts.Config.COLOR_BG_RED;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.GiftModel;
import os.com.krishirasayan.models.GiftRedeemModel;


public class MyGiftRedeemAdapter extends RecyclerView.Adapter<MyGiftRedeemAdapter.GiftViewHolder> {

    Context context;
    boolean selectable = false;
    List<GiftRedeemModel> allGiftList;
    ICallback iCallback;


    public MyGiftRedeemAdapter(Context context, List<GiftRedeemModel> allGiftList) {
        this.context = context;
        this.allGiftList = allGiftList;
    }

    public MyGiftRedeemAdapter(Context context, List<GiftRedeemModel> allGiftList, boolean selectable) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
    }

    public MyGiftRedeemAdapter(Context context, List<GiftRedeemModel> allGiftList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public GiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_my_gift_item, parent, false);
        return new GiftViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftViewHolder holder, int position) {

        GiftRedeemModel listGift= allGiftList.get(position);
        holder.tvRedeemRequestDate.setText(listGift.getRequestTime());
        //holder.tvRedeemApproveDate.setText(listGift.getUpdateTime());
        holder.tvRedeemStatus.setText(listGift.getRequestStatus());
        holder.tvTotalRedeemPoint.setText(String.valueOf(listGift.getTotalRedeemPoint()));
        holder.tvRedeemItemCount.setText(String.valueOf(listGift.getTotalQuantity()));
        switch (listGift.getRequestStatus()){
            case "APPROVED":
                holder.ivOrderedStatusYellow.setBackgroundColor(Color.parseColor(COLOR_BG_GREEN));
                holder.clApprovedDate.setVisibility(View.VISIBLE);
                holder.tvRedeemApproveDate.setText(listGift.getUpdateTime());
                break;
            case "PENDING":
                holder.ivOrderedStatusYellow.setBackgroundColor(Color.parseColor(COLOR_BG_GRAY));
                break;
            case "DECLINED":
                holder.ivOrderedStatusYellow.setBackgroundColor(Color.parseColor(COLOR_BG_RED));
                holder.clApprovedDate.setVisibility(View.VISIBLE);
                holder.tvApprovedLblGiftRedeem.setText("Declined Request : ");
                holder.tvRedeemApproveDate.setText(listGift.getUpdateTime());
                break;
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.rvRedeemGiftList.setLayoutManager(layoutManager);
        holder.rvRedeemGiftList.setAdapter(new MyGiftAdapter(context, listGift.getGiftList(),true));
        //iCallback.function(listGift.getGiftList());

    }

    @Override
    public int getItemCount() {
        return allGiftList.size();
    }


    public static final class GiftViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGiftImage,ivOrderedStatusYellow;
        TextView tvPrizeListTitle, tvPrizeListNeededPoints, tvRedeemRequestDate, tvRedeemApproveDate, tvRedeemStatus,tvTotalRedeemPoint,tvRedeemItemCount,tvApprovedLblGiftRedeem;
        RecyclerView rvRedeemGiftList;
        ConstraintLayout clApprovedDate;

        public GiftViewHolder(@NonNull View itemView) {
            super(itemView);
            /*//Redeem List*/
            tvRedeemRequestDate = itemView.findViewById(R.id.tvRedeemRequestDate);
            tvRedeemApproveDate = itemView.findViewById(R.id.tvRedeemApproveDate);
            tvRedeemStatus = itemView.findViewById(R.id.tvRedeemStatus);
            tvTotalRedeemPoint = itemView.findViewById(R.id.tvTotalRedeemPoint);
            tvRedeemItemCount = itemView.findViewById(R.id.tvRedeemItemCount);
            rvRedeemGiftList = itemView.findViewById(R.id.rvRedeemGiftList);
            ivOrderedStatusYellow = itemView.findViewById(R.id.ivOrderedStatusYellow);
            clApprovedDate = itemView.findViewById(R.id.clApprovedDate);
            tvApprovedLblGiftRedeem = itemView.findViewById(R.id.tvApprovedLblGiftRedeem);

        }

    }

}
