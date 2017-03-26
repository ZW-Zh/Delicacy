package com.android.zzw.delicacy.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zzw.delicacy.JavaBean.Food;
import com.android.zzw.delicacy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by zzw on 17/2/25.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private Context context;
    private List<Food>mFoodList;
    private LayoutInflater inflater;
    private MyItemClickListener mItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View foodview;
        ImageView foodimg;
        TextView foodname;
        public ViewHolder(View view){
            super(view);
            foodimg = (ImageView) view.findViewById(R.id.foodimg);
            foodname = (TextView) view.findViewById(R.id.name);
            foodview=view;
        }

    }
    public FoodAdapter(Context context, List<Food> foodList){
        this.context = context;
        mFoodList=foodList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fooditem,parent,false);
        final ViewHolder holder =new ViewHolder(view);
        holder.foodview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int postion = holder.getAdapterPosition();
                mItemClickListener.onItemClick(v,postion);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(final FoodAdapter.ViewHolder holder, int position) {
        final Food food=mFoodList.get(position);
        //Glide.with(context).load(mFoodList.get(position).getUrl()).into(holder.foodimg);
        Glide.with(context).load(mFoodList.get(position).getUrl()).asBitmap().thumbnail(0.1f).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
               holder.foodimg.setImageBitmap(resource);
            }
        });
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),"fonts/songti.ttf");
        holder.foodname.setTypeface(typeFace);
        holder.foodname.setText(food.getName());

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
    public interface MyItemClickListener {
        void onItemClick(View view,int postion);
    }
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }
}
