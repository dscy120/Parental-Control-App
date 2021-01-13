package com.example.parentalapp.quiz.record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;

import java.util.ArrayList;

public class RecordViewAdapter extends RecyclerView.Adapter<RecordViewAdapter.RecordViewHolder> {

    private ArrayList<RecordItem> recordList;
    private RecordClickListener recordClickListener;

    public RecordViewAdapter(ArrayList<RecordItem> recordList, RecordClickListener recordClickListener){
        this.recordList = recordList;
        this.recordClickListener = recordClickListener;
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView attemptId, category, score;
        RecordClickListener recordClickListener;

        public RecordViewHolder(View itemView, RecordClickListener recordClickListener){
            super(itemView);
            attemptId = itemView.findViewById(R.id.textView_AttemptId);
            category = itemView.findViewById(R.id.textView_Subject);
            score = itemView.findViewById(R.id.textView_RecordScore);
            this.recordClickListener = recordClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // call the action after click
            recordClickListener.onRecordClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_record, parent, false);
        RecordViewHolder evh = new RecordViewHolder(v, recordClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordItem currentItem = recordList.get(position);

        holder.attemptId.setText(String.valueOf(currentItem.getId()));
        holder.category.setText(String.valueOf(currentItem.getCategory()));
        holder.score.setText(String.valueOf(currentItem.getScore()));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public interface RecordClickListener{
        void onRecordClick(int position);
    }

}
