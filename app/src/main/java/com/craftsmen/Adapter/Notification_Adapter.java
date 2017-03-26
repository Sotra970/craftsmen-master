package com.craftsmen.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.craftsmen.Models.Notification_model;
import com.craftsmen.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2/23/2017.
 */

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.myViewHolder> {

    ArrayList<Notification_model> data;
    LayoutInflater inflater ;
    public Notification_Adapter(Context context, ArrayList<Notification_model> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
    }
    @Override
    public Notification_Adapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.noti_item,parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Notification_Adapter.myViewHolder holder, int position) {
        Notification_model current = data.get(position) ;
        holder.name.setText(current.getName());
        holder.date.setText(current.getDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name  , date;
        public myViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.noti_msg);
            date = (TextView) itemView.findViewById(R.id.noti_date);
        }
    }
}

