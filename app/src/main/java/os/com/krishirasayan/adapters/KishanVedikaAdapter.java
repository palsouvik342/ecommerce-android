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
import os.com.krishirasayan.activities.NewsDetailsActivity;
import os.com.krishirasayan.interfaces.ICallback;
import os.com.krishirasayan.models.NewsModel;


public class KishanVedikaAdapter extends RecyclerView.Adapter<KishanVedikaAdapter.AddressViewHolder> {

    Context context;
    boolean selectable = false;
    List<NewsModel> allNewsList;
    ICallback iCallback;


    public KishanVedikaAdapter(Context context, List<NewsModel> allNewsList) {
        this.context = context;
        this.allNewsList = allNewsList;
    }

    public KishanVedikaAdapter(Context context, List<NewsModel> allNewsList, boolean selectable) {
        this.context = context;
        this.allNewsList = allNewsList;
        this.selectable = selectable;
    }

    public KishanVedikaAdapter(Context context, List<NewsModel> allNewsList, boolean selectable, ICallback iCallback) {
        this.context = context;
        this.allNewsList = allNewsList;
        this.selectable = selectable;
        this.iCallback = iCallback;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.list_scheme_item, parent, false);
       return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        NewsModel listNews= allNewsList.get(position);

        if(!listNews.getTitle().isEmpty()) {
            holder.tvSchemeTitle.setText(listNews.getTitle());
        }
       // Picasso.get().load(listNews.getImageUrl()).into(holder.ivBlogListImage);
        if(!listNews.getImageUrl().isEmpty()) {
            Picasso.get().load( listNews.getImageUrl())
                    .error( R.drawable.account_icon )
                    .placeholder( R.drawable.account_icon )
                    .into( holder.ivSchemeImage );
        }
    }

    @Override
    public int getItemCount() {
        return allNewsList.size();
    }

    public static final class AddressViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSchemeImage;
        TextView tvSchemeTitle;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
             //news list
            tvSchemeTitle = itemView.findViewById(R.id.tvSchemeTitle);
            ivSchemeImage = itemView.findViewById(R.id.ivSchemeImage);



        }

    }

}
