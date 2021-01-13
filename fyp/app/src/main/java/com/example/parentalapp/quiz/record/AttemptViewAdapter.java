package com.example.parentalapp.quiz.record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;

import java.util.ArrayList;

public class AttemptViewAdapter extends RecyclerView.Adapter<AttemptViewAdapter.AttemptViewHolder>{
    private ArrayList<AttemptDetail> AttemptList;
    private AttemptViewAdapter.AttemptClickListener AttemptClickListener;

    public AttemptViewAdapter(ArrayList<AttemptDetail> AttemptList, AttemptViewAdapter.AttemptClickListener AttemptClickListener){
        this.AttemptList = AttemptList;
        this.AttemptClickListener = AttemptClickListener;
    }

    public static class AttemptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView question, attemptAnswer, correctAnswer, explanation;
        AttemptViewAdapter.AttemptClickListener AttemptClickListener;

        public AttemptViewHolder(View itemView, AttemptViewAdapter.AttemptClickListener AttemptClickListener){
            super(itemView);
            question = itemView.findViewById(R.id.textView_detail_question);
            attemptAnswer = itemView.findViewById(R.id.textView_detail_attemptAns);
            correctAnswer = itemView.findViewById(R.id.textView_detail_correctAns);
            explanation = itemView.findViewById(R.id.textView_explanation);
            this.AttemptClickListener = AttemptClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // call the action after click
            AttemptClickListener.onAttemptClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public AttemptViewAdapter.AttemptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_attempt_detail, parent, false);
        AttemptViewAdapter.AttemptViewHolder evh = new AttemptViewAdapter.AttemptViewHolder(v, AttemptClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttemptViewAdapter.AttemptViewHolder holder, int position) {
        AttemptDetail currentItem = AttemptList.get(position);

        holder.question.setText(String.valueOf(currentItem.getQuestionId()));
        holder.attemptAnswer.setText(String.valueOf(currentItem.getAttemptAnswer()));
        holder.correctAnswer.setText(String.valueOf(currentItem.getCorrectAnswer()));
        holder.explanation.setText(String.valueOf(currentItem.getExplanation()));
    }

    @Override
    public int getItemCount() {
        return AttemptList.size();
    }

    public interface AttemptClickListener{
        void onAttemptClick(int position);
    }
}
