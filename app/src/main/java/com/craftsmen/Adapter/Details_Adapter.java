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
import com.craftsmen.Models.Comements_model;
import com.craftsmen.Models.User_model;
import com.craftsmen.R;

import java.util.ArrayList;

/**
 * Created by lenovo on 2/24/2017.
 */

public class Details_Adapter extends RecyclerView.Adapter<Details_Adapter.myViewHolder> {
        Context context ; 
        ArrayList<Comements_model> data;
        LayoutInflater inflater ;
public Details_Adapter(Context context, ArrayList<Comements_model> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
            this.context = context ; 
        }
@Override
public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comments_ryc,parent,false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
        }

@Override
public void onBindViewHolder(Details_Adapter.myViewHolder holder, int position) {
        final Comements_model current = data.get(position) ;
        holder.name.setText(current.getUser_model().getU_name());
        holder.comment.setText(current.getComment());
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

@Override
public int getItemCount() {
        return data.size();
        }
class myViewHolder extends RecyclerView.ViewHolder{
    TextView name , comment , date;
    ImageView picture ;
    public myViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.comment_item_creator_name);
        date = (TextView) itemView.findViewById(R.id.comment_item_time);
        comment = (TextView) itemView.findViewById(R.id.comment_item_desc);
        picture = (ImageView) itemView.findViewById(R.id.comment_item_creator_img);
    }
}
    void open_profile(User_model userModel ){
        Intent intent = new Intent(context , ProfileActivity.class) ; 
        intent.putExtra("extra" , userModel) ;
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    
}

