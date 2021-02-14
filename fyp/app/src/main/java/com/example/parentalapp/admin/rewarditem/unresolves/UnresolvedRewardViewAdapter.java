package com.example.parentalapp.admin.rewarditem.unresolves;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;
import com.example.parentalapp.reward.RewardViewAdapter;

import java.util.ArrayList;

public class UnresolvedRewardViewAdapter extends RecyclerView.Adapter<UnresolvedRewardViewAdapter.UnresolvedRewardViewHolder> {
    private ArrayList<UnresolvedReward> unresolvedRewardList;
    private UnresolvedRewardClickListener unresolvedRewardClickListener;

    public UnresolvedRewardViewAdapter(ArrayList<UnresolvedReward> unresolvedRewardList, UnresolvedRewardClickListener unresolvedRewardClickListener) {
        this.unresolvedRewardList = unresolvedRewardList;
        this.unresolvedRewardClickListener = unresolvedRewardClickListener;
    }

    public static class UnresolvedRewardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, quantity, date;
        UnresolvedRewardViewAdapter.UnresolvedRewardClickListener unresolvedRewardClickListener;

        public UnresolvedRewardViewHolder(View itemView, UnresolvedRewardViewAdapter.UnresolvedRewardClickListener unresolvedRewardClickListener){
            super(itemView);
            name = itemView.findViewById(R.id.textView_unresolve_item_name);
            quantity = itemView.findViewById(R.id.textView_unresolve_reward_quantity);
            date = itemView.findViewById(R.id.textView_unresolve_item_date);

            this.unresolvedRewardClickListener = unresolvedRewardClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            unresolvedRewardClickListener.onUnresolvedClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public UnresolvedRewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_unresolved_reward, parent, false);
        UnresolvedRewardViewHolder evh = new UnresolvedRewardViewHolder(v, this.unresolvedRewardClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UnresolvedRewardViewHolder holder, int position) {
        UnresolvedReward unresolvedReward = unresolvedRewardList.get(position);

        holder.name.setText(unresolvedReward.getRewardName());
        holder.quantity.setText(String.valueOf(unresolvedReward.getQuantity()));
        holder.date.setText(unresolvedReward.getDate());
    }

    @Override
    public int getItemCount() {
        return unresolvedRewardList.size();
    }

    public interface UnresolvedRewardClickListener{
        void onUnresolvedClick(int position);
    }
}
