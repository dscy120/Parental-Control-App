package com.example.parentalapp.admin.questionbank;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalapp.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionBankViewAdapter extends RecyclerView.Adapter<QuestionBankViewAdapter.QuestionBankViewHolder> {
    private ArrayList<QuestionBankSource> sourceList;
    private QuestionBankClickListener questionBankClickListener;

    public QuestionBankViewAdapter(ArrayList<QuestionBankSource> sourceList, QuestionBankClickListener questionBankClickListener) {
        this.sourceList = sourceList;
        this.questionBankClickListener = questionBankClickListener;
    }

    public static class QuestionBankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, subject, level;
        QuestionBankClickListener questionBankClickListener;

        public QuestionBankViewHolder(View itemView, QuestionBankClickListener questionBankClickListener){
            super(itemView);
            subject = itemView.findViewById(R.id.textView_exercise_subject);
            level = itemView.findViewById(R.id.textView_exercise_level);
            title = itemView.findViewById(R.id.textView_exercise_title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            questionBankClickListener.onBankClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public QuestionBankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_question_bank_source, parent, false);
        QuestionBankViewHolder evh = new QuestionBankViewHolder(v, this.questionBankClickListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionBankViewHolder holder, int position) {
        QuestionBankSource questionBankSource = sourceList.get(position);

        holder.subject.setText(questionBankSource.getSubject());
        holder.level.setText(questionBankSource.getSubject());
        holder.title.setText(questionBankSource.getTitle());
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    public interface QuestionBankClickListener{
        void onBankClick(int position);
    }

}
