package com.craftsmen.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Request_item_model;
import com.craftsmen.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2/23/2017.
 */

public class Requests_Adapter extends RecyclerView.Adapter<Requests_Adapter.myViewHolder> {
    Adapter_item_click_listener adapter_item_click_listener ;
    ArrayList<Request_item_model> data;
    LayoutInflater inflater ;
    Context context ;
    public Requests_Adapter(Context context, ArrayList<Request_item_model> data , Adapter_item_click_listener adapter_item_click_listener  ){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context  ;
        this.adapter_item_click_listener = adapter_item_click_listener ;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.request_item,parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        Request_item_model current = data.get(position) ;
        holder.name.setText(current.getUser_model().getU_name());
        holder.desc.setText(current.getDesc());
        holder.time.setText(current.getCreated_date());
        Glide.with(context).load(Config.img_url  +current.getUser_model().getU_Pic()) .fitCenter() . into(holder.picture) ;
        Glide.with(context).load(Config.img_url  +current.getRequesst_image()) .fitCenter() . into(holder.req_img) ;
        Log.e("img url ",Config.img_url  +current.getRequesst_image());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name , desc , time , loc;
        ImageView picture;
        ImageView req_img;
        public myViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.request_item_creator_name);
            time = (TextView) itemView.findViewById(R.id.request_item_time);
            loc = (TextView) itemView.findViewById(R.id.request_item_loc);
            desc = (TextView) itemView.findViewById(R.id.request_item_desc);
            picture = (ImageView) itemView.findViewById(R.id.request_item_creator_img);
            req_img = (ImageView) itemView.findViewById(R.id.req_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter_item_click_listener.onClick(getAdapterPosition());
                }
            });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        adapter_item_click_listener.onLongClick(getAdapterPosition());

                        return true;
                    }
                });
        }

    }
    public interface   Adapter_item_click_listener {
        void  onClick (int postion ) ;
        void onLongClick( int postiion) ;
    }
}
