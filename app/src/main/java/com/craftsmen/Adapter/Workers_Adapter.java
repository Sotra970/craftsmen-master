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
import com.craftsmen.Models.User_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;

import java.util.ArrayList;


/**
 * Created by lenovo on 2/23/2017.
 */

public class Workers_Adapter extends RecyclerView.Adapter<Workers_Adapter.myViewHolder> {

    ArrayList<User_model> data;
    LayoutInflater inflater ;
    Context context ;
    public Workers_Adapter(Context context, ArrayList<User_model> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context ;
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.workers_item,parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Workers_Adapter.myViewHolder holder, int position) {
        final User_model current = data.get(position) ;
        holder.name.setText(current.getU_name());
        holder.job.setText(current.getU_service());


        Glide.with(context).load(Config.img_url  +current.getU_Pic()) .fitCenter() . into(holder.picture) ;

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(current);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(current);
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
        TextView name , job;
        ImageView picture ;
        public myViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            job = (TextView) itemView.findViewById(R.id.job);
            picture = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
