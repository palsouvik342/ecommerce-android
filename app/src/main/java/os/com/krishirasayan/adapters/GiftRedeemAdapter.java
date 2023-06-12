package os.com.krishirasayan.adapters;


import static android.content.Context.MODE_PRIVATE;
import static os.com.krishirasayan.consts.Config.SF_USERDATA_FILENAME;
import static os.com.krishirasayan.consts.Helper.w;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.GiftModel;


public class GiftRedeemAdapter extends RecyclerView.Adapter<GiftRedeemAdapter.GiftRedeemViewHolder> {

    Context context;
    boolean selectable = false;
    List<GiftModel> allGiftList;
    List<GiftModel> selectedGiftList;
    ICallback iCallback;
    public static final String KEY_NAME_GIFT = "gift_name";
    float TOTAL_POINT = 0;


    public GiftRedeemAdapter(Context context, List<GiftModel> allGiftList) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectedGiftList =new ArrayList<>();

    }

    public GiftRedeemAdapter(Context context, List<GiftModel> allGiftList, boolean selectable) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.selectedGiftList =new ArrayList<>();
    }

    public GiftRedeemAdapter(Context context, List<GiftModel> allGiftList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allGiftList = allGiftList;
        this.selectable = selectable;
        this.iCallback = iCallback;
        this.selectedGiftList =new ArrayList<>();
    }

    @NonNull
    @Override
    public GiftRedeemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_gift_item_redeem, parent, false);
        return new GiftRedeemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftRedeemViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (allGiftList != null) {

            GiftModel listGift = allGiftList.get(position);
            listGift.setQuantity(1);
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

            holder.recycler_checkbox.setOnCheckedChangeListener(null);
            holder.recycler_checkbox.setChecked(allGiftList.get(position).isSelected());
            holder.recycler_checkbox.setTag(allGiftList.get(position));

            SharedPreferences sharedPreferences = context.getSharedPreferences(SF_USERDATA_FILENAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_NAME_GIFT, String.valueOf(listGift.getPoints()));
            editor.apply();

            SharedPreferences prfs = context.getSharedPreferences(SF_USERDATA_FILENAME, MODE_PRIVATE);
            String Gift_poits = prfs.getString(KEY_NAME_GIFT, "");

            holder.btGiftListQtyMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = selectedGiftList.indexOf(listGift);
                    if(index >= 0) {
                        selectedGiftList.get(index).subQuantity(1);
                        holder.tvProductListCartQtyValue.setText(String.valueOf(selectedGiftList.get(index).getQuantity()));
                        iCallback.function(selectedGiftList);
                    }

                }
            });

            holder.btGiftListQtyPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = selectedGiftList.indexOf(listGift);
                    if(index >= 0) {
                        selectedGiftList.get(index).addQuantity(1);
                        holder.tvProductListCartQtyValue.setText(String.valueOf(selectedGiftList.get(index).getQuantity()));
                        iCallback.function(selectedGiftList);
                    }
                }
            });

            holder.recycler_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedGiftList.add(listGift);
                        allGiftList.get(position).setSelected(true);
                        //Toast.makeText(context, "Checked : " + allGiftList.get(position).getName() + Gift_poits, Toast.LENGTH_LONG).show();
                        TOTAL_POINT = TOTAL_POINT + Float.parseFloat(Gift_poits);
                        //w("Users",TOTAL_POINT);;
                    } else {
                        selectedGiftList.remove(listGift);
                        allGiftList.get(position).setSelected(false);
                        //Toast.makeText(context, "Unchecked : " + allGiftList.get(position).getName(), Toast.LENGTH_LONG).show();
                        TOTAL_POINT = TOTAL_POINT - Float.parseFloat(Gift_poits);
                        //w("Users",TOTAL_POINT);

                    }
                    iCallback.function(selectedGiftList);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return allGiftList.size();
    }


    public static final class GiftRedeemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGiftImage;
        TextView tvPrizeListTitle, tvPrizeListNeededPoints, tvProductListCartQtyValue;
        CheckBox recycler_checkbox;
        Button btGiftListQtyMinus, btGiftListQtyPlus;

        public GiftRedeemViewHolder(@NonNull View itemView) {
            super(itemView);


            //Gift list
            tvPrizeListTitle = itemView.findViewById(R.id.tvPrizeListTitle);
            tvPrizeListNeededPoints = itemView.findViewById(R.id.tvPrizeListNeededPoints);
            ivGiftImage = itemView.findViewById(R.id.ivGiftImage);
            recycler_checkbox = itemView.findViewById(R.id.chkRedeemGift);
            tvProductListCartQtyValue = itemView.findViewById(R.id.tvProductListCartQtyValue);
            btGiftListQtyMinus = itemView.findViewById(R.id.btGiftListQtyMinus);
            btGiftListQtyPlus = itemView.findViewById(R.id.btGiftListQtyPlus);


            /*//Reward Banner List
            ivPrizeOfferBanner = itemView.findViewById(R.id.ivPrizeOfferBanner);*/

        }

    }

}
