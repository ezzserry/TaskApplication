package com.example.kryptonworx.taskapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by kryptonWorx on 21-Dec-15.
 */
public class DetailsViewAdapter extends RecyclerView.Adapter<DetailsViewAdapter.MyCustomViewHolder> {


    private ArrayList<PhotoItem> photoItemList;
    private Context mContext;
    String imgBaseUrl;
    PhotoItem photoItem;

    public DetailsViewAdapter(Context context, ArrayList<PhotoItem> photoItemList) {
        this.photoItemList = photoItemList;
        this.mContext = context;
        this.imgBaseUrl = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    }

    @Override
    public MyCustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.details_row, null);

        MyCustomViewHolder viewHolder = new MyCustomViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MyCustomViewHolder holder, int position) {
        photoItem = photoItemList.get(position);

        String imgUrl = String.format(imgBaseUrl, photoItem.farm, photoItem.server, photoItem.id, photoItem.secret);
        //Download image using picasso library
        Picasso.with(mContext).load(imgUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        //Setting text view title
        holder.textView.setText(photoItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return photoItemList.size();
    }

    public class MyCustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected RelativeLayout container;
        protected TextView textView;

        public MyCustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail_details);
            this.textView = (TextView) view.findViewById(R.id.title_tv_details);
            this.container = (RelativeLayout) view.findViewById(R.id.container);

        }
    }
}
