package com.example.parentalapp.admin.questionbank;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import java.util.ArrayList;

import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_ID;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_TITLE;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_SUBJECT;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_LEVEL;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_PUBLISHER;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLAOD_LINK;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLOAD_STATUS;

public class QuestionBankActivity extends AppCompatActivity implements QuestionBankViewAdapter.QuestionBankClickListener {

    private int positionClicked;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<QuestionBankSource> sourceList;
    private QuestionBankDBHelper questionBankDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        //back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        questionBankDBHelper = new QuestionBankDBHelper(getApplicationContext());
        sourceList = questionBankDBHelper.getSource();

        recyclerView = findViewById(R.id.recyclerView_question_bank);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new QuestionBankViewAdapter(sourceList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyItemChanged(positionClicked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBankClick(int position) {
        //show source detail
        positionClicked = position;
        QuestionBankSource questionBankSource = sourceList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(SOURCE_ID, questionBankSource.getId());
        Intent i = new Intent(getApplicationContext(), QuestionBankDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}