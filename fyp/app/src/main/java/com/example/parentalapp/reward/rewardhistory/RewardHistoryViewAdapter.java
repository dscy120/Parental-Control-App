package com.example.parentalapp.reward.rewardhistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;

import java.util.ArrayList;

public class RewardHistoryViewAdapter extends RecyclerView.Adapter<RewardHistoryViewAdapter.RewardHistoryViewHolder> {
    private ArrayList<RewardRecordItem> recordItemArrayList;
    private RewardHistoryClickListener rewardHistoryClickListener;

    public RewardHistoryViewAdapter(ArrayList<RewardRecordItem> recordItemArrayList, RewardHistoryClickListener rewardHistoryClickListener) {
        this.recordItemArrayList = recordItemArrayList;
        this.rewardHistoryClickListener = rewardHistoryClickListener;
    }

    public static class RewardHistoryViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener{
        public TextView itemName, date;
        RewardHistoryViewAdapter.RewardHistoryClickListener rewardHistoryClickListener;

        public RewardHistoryViewHolder(@NonNull View itemView, RewardHistoryViewAdapter.RewardHistoryClickListener rewardHistoryClickListener) {
            super(itemView);
            // textviews
            itemName = itemView.findViewById(R.id.textView_reward_history_itemName);
            date = itemView.findViewById(R.id.textView_reward_history_date);

            this.rewardHistoryClickListener = rewardHistoryClickListener;
        }

        @Override
        public void onClick(View v) {
            rewardHistoryClickListener.onRewardHistoryClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RewardHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_reward_history, parent, false);
        RewardHistoryViewAdapter.RewardHistoryViewHolder evh = new RewardHistoryViewHolder(v, this.rewardHistoryClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RewardHistoryViewHolder holder, int position) {
        RewardRecordItem rewardRecordItem = recordItemArrayList.get(position);

        holder.itemName.setText(rewardRecordItem.getItemName());
        holder.date.setText(rewardRecordItem.getDate());
    }

    @Override
    public int getItemCount() {
        return recordItemArrayList.size();
    }

    public interface RewardHistoryClickListener{
        void onRewardHistoryClick(int position);
    }
}
