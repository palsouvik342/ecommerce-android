package os.com.krishirasayan.adapters;


import static os.com.krishirasayan.consts.Helper.open;
import static os.com.krishirasayan.consts.Helper.w;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import os.com.krishirasayan.R;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.AddressModel;
import os.com.krishirasayan.models.PaymentMethodModel;


public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder> {

    Context context;
    boolean selectable = false;
    List<PaymentMethodModel> list;
    ICallback iCallback;


    public PaymentMethodAdapter(Context context, List<PaymentMethodModel> list) {
        this.context = context;
        this.list = list;
    }

    public PaymentMethodAdapter(Context context, List<PaymentMethodModel> list, boolean selectable) {
        this.context = context;
        this.list = list;
        this.selectable = selectable;
    }

    public PaymentMethodAdapter(Context context, List<PaymentMethodModel> list, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.list = list;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public PaymentMethodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_payment_method_item, parent, false);
        return new PaymentMethodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentMethodViewHolder holder, int position) {

        PaymentMethodModel paymentMethodModel = list.get(position);
        holder.tvMethodName.setText(paymentMethodModel.getName());

        if(this.selectable) {
            holder.clAddressContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iCallback.function(paymentMethodModel);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static final class PaymentMethodViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout clAddressContainer;
        TextView tvMethodName;

        public PaymentMethodViewHolder(@NonNull View itemView) {
            super(itemView);

            clAddressContainer = itemView.findViewById(R.id.clAddressContainer);
            tvMethodName = itemView.findViewById(R.id.tvMethodName);


        }

    }

}
