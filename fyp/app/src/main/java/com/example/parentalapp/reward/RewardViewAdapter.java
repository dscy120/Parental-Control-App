package com.example.parentalapp.reward;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.SelectConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class RewardViewAdapter extends RecyclerView.Adapter<RewardViewAdapter.RewardViewHolder> {
    private ArrayList<RewardItem> rewardItemList;
    private RewardClickListener rewardClickListener;

    public RewardViewAdapter(ArrayList<RewardItem> rewardItemList, RewardClickListener rewardClickListener){
        this.rewardItemList = rewardItemList;
        this.rewardClickListener = rewardClickListener;
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, point;
        RewardViewAdapter.RewardClickListener rewardClickListener;

        public RewardViewHolder(View itemView, RewardViewAdapter.RewardClickListener rewardClickListener){
            super(itemView);
            name = itemView.findViewById(R.id.textView_rewardItem_name);
            point = itemView.findViewById(R.id.textView_require_points);

            this.rewardClickListener = rewardClickListener;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // call the action after click
            rewardClickListener.onRewardClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RewardViewAdapter.RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reward_item, parent, false);
        RewardViewAdapter.RewardViewHolder evh = new RewardViewAdapter.RewardViewHolder(v, this.rewardClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        RewardItem rewardItem = rewardItemList.get(position);

        holder.name.setText(rewardItem.getName());
        holder.point.setText(String.valueOf(rewardItem.getPoint()));

    }

    @Override
    public int getItemCount() {
        return rewardItemList.size();
    }

    public interface RewardClickListener{
        void onRewardClick(int position);
    }


}
