package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import os.com.krishirasayan.R;
import os.com.krishirasayan.activities.AddressAddActivity;
import os.com.krishirasayan.activities.AddressListActivity;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.AddressModel;

import java.util.List;


public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    Context context;
    boolean selectable = false;
    List<AddressModel> allAddressList;
    ICallback iCallback;


    public AddressAdapter(Context context, List<AddressModel> allAddressList) {
        this.context = context;
        this.allAddressList = allAddressList;
    }

    public AddressAdapter(Context context, List<AddressModel> allAddressList, boolean selectable) {
        this.context = context;
        this.allAddressList = allAddressList;
        this.selectable = selectable;
    }

    public AddressAdapter(Context context, List<AddressModel> allAddressList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allAddressList = allAddressList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_address_item_menu, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        AddressModel listAddress = allAddressList.get(position);
        holder.tvContact.setText(listAddress.getContactName());
        holder.tvAddress.setText(listAddress.getLine1());
        holder.tvLocation.setText(listAddress.getLocation());

        if(this.selectable) {
            holder.btAddressListEditButton.setVisibility(View.GONE);
            holder.btAddressListDeleteButton.setVisibility(View.GONE);
            holder.clAddressContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iCallback.function(listAddress);
                }
            });
        }
        else {
            holder.btAddressListEditButton.setVisibility(View.VISIBLE);
            holder.btAddressListEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, listAddress.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddressAddActivity.class);
                    intent.putExtra("address_id", listAddress.getId());
                    open(context, intent, true);
                }
            });

            holder.btAddressListDeleteButton.setVisibility(View.GONE);
            /*holder.btAddressListDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, listAddress.getId(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, AddressAddActivity.class);
                    intent.putExtra("address_delete_id", listAddress.getId());
                    open(context, intent, true);
                }
            });*/
        }

    }

    @Override
    public int getItemCount() {
        return allAddressList.size();
    }


    public static final class AddressViewHolder extends RecyclerView.ViewHolder {

        TextView tvContact, tvAddress, tvLocation;
        Button btAddressListEditButton, btAddressListDeleteButton;
        ConstraintLayout clAddressContainer;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContact = itemView.findViewById(R.id.tvContact);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btAddressListEditButton = itemView.findViewById(R.id.btAddressListEditButton);
            btAddressListDeleteButton = itemView.findViewById(R.id.btAddressListDeleteButton);
            clAddressContainer = itemView.findViewById(R.id.clAddressContainer);


        }

    }

}
