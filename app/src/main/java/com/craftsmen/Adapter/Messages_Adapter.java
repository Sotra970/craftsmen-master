package com.craftsmen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.craftsmen.Activity.ProfileActivity;
import com.craftsmen.AppManger.Config;
import com.craftsmen.Models.Comements_model;
import com.craftsmen.Models.Message_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;

import java.util.ArrayList;


/**
 * Created by lenovo on 2/23/2017.
 */

public class Messages_Adapter extends RecyclerView.Adapter<Messages_Adapter.myViewHolder> {

    ArrayList<Message_model> data;
    LayoutInflater inflater ;
    Context context ;
    public Messages_Adapter(Context context, ArrayList<Message_model> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context ;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.messages,parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Messages_Adapter.myViewHolder holder, int position) {
        final Message_model current = data.get(position) ;
        holder.name.setText(current.getUser_model().getU_name());
        holder.message.setText(current.getMessage());
        holder.date.setText(current.getDate());


        Glide.with(context).load(Config.img_url  +current.getUser_model().getU_Pic()) .fitCenter() . into(holder.picture) ;

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(current.getUser_model());
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(current.getUser_model());
            }
        });


    }

    void open_profile(User_model userModel ){
        Intent intent = new Intent(context , ProfileActivity.class) ;
        intent.putExtra("extra" , userModel) ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        TextView name , message , date;
        ImageView picture ;
        public myViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mess_item_creator_name);
            date = (TextView) itemView.findViewById(R.id.mess_item_time);
            message = (TextView) itemView.findViewById(R.id.mess_item_desc);
            picture = (ImageView) itemView.findViewById(R.id.mess_item_creator_img);
        }
    }
}
