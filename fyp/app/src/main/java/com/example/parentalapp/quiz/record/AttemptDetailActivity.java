package com.example.parentalapp.quiz.record;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.DatabaseOpenHelper;
import com.example.parentalapp.quiz.QuizConstant;
import com.example.parentalapp.quiz.QuizMainActivity;

import java.util.ArrayList;

// TODO: finish this class
public class AttemptDetailActivity extends AppCompatActivity implements AttemptViewAdapter.AttemptClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseOpenHelper quizDBHelper;
    private ArrayList<AttemptDetail> attemptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_detail);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        attemptList = new ArrayList<>();

        quizDBHelper = new DatabaseOpenHelper(this);
        Bundle extras = getIntent().getExtras();
        int attempt_id = -1;
        if(extras != null){
            attempt_id = extras.getInt("id");
        }
        Cursor c = quizDBHelper.getAttemptDetail(attempt_id); // test
        if(c.moveToFirst()){
            do {
                int id = c.getInt(c.getColumnIndex(QuizConstant.QuestionTable._ID));
                int attemptId = c.getInt(c.getColumnIndex(QuizConstant.QuestionTable.ATTEMPT_ID));
                String questionId = c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_QUESTION));
                String category = c.getString(c.getColumnIndex(QuizConstant.QuestionTable.CATEGORY));
                String attemptAns = c.getString(c.getColumnIndex(QuizConstant.QuestionTable.ATTEMPTED_ANSWER));
                String correctAns = c.getString(c.getColumnIndex(QuizConstant.QuestionTable.CORRECT_ANSWER));
                int score = c.getInt(c.getColumnIndex(QuizConstant.QuestionTable.SCORE));
                String explanation = c.getString(c.getColumnIndex("explanation"));
                attemptList.add(new AttemptDetail(id, attemptId, questionId, category, attemptAns, correctAns, score, explanation));
            } while(c.moveToNext());
            c.close();
        }


        recyclerView = findViewById(R.id.recyclerView_attempt_detail);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new AttemptViewAdapter(attemptList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, RecordMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttemptClick(int position) {
        // position: index to the list
        // recordList.get(position);
        Toast.makeText(this, "Was up" + position, Toast.LENGTH_SHORT).show();
    }
}