package com.example.kryptonworx.taskapplication;

import android.content.Context;
import android.content.Intent;
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
 * Created by kryptonWorx on 20-Dec-15.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<PhotoItem> photoItemList;
    private Context mContext;
    String imgBaseUrl;
    PhotoItem photoItem;

    public MyRecyclerViewAdapter(Context context, ArrayList<PhotoItem> photoItemList) {
        this.photoItemList = photoItemList;
        this.mContext = context;
        this.imgBaseUrl = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

    }


    @Override
    public MyRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewAdapter.CustomViewHolder holder, int position) {
        photoItem = photoItemList.get(position);

        String imgUrl = String.format(imgBaseUrl, photoItem.farm, photoItem.server, photoItem.id, photoItem.secret);
        //Download image using picasso library
        Picasso.with(mContext).load(imgUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        //Setting text view title
        holder.textView.setText(photoItem.getTitle());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ownerid = photoItem.getOwner();
                Intent intent = new Intent(mContext, PhotoDetails.class);
                intent.putExtra("owner_id", ownerid);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return photoItemList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;
        protected RelativeLayout container;
        protected TextView textView;

        public CustomViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
            this.textView = (TextView) view.findViewById(R.id.title_tv);
            this.container = (RelativeLayout) view.findViewById(R.id.container);

        }
    }
}
