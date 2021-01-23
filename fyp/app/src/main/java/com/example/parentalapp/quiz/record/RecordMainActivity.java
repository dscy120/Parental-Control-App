package com.example.parentalapp.quiz.record;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.parentalapp.R;
import com.example.parentalapp.database.DatabaseOpenHelper;
import com.example.parentalapp.quiz.QuizMainActivity;

import java.util.ArrayList;

public class RecordMainActivity extends AppCompatActivity implements RecordViewAdapter.RecordClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecordDBHelper recordDBHelper;
    private ArrayList<RecordItem> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_main);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        recordList = new ArrayList<>();

        recordDBHelper = new RecordDBHelper(this);
        Cursor c = recordDBHelper.getAllRecord();
        if(c.moveToFirst()){
            do {
                int id = c.getInt(0);
                String category = c.getString(1);
                int score = c.getInt(2);
                recordList.add(new RecordItem(id, category, score));
            } while(c.moveToNext());
            c.close();
        }


        recyclerView = findViewById(R.id.recyclerView_recordList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RecordViewAdapter(recordList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, QuizMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // action when a record is clicked
    // show details of the clicked attempt
    @Override
    public void onRecordClick(int position) {
        // position: index to the list
        Intent i = new Intent(this, AttemptDetailActivity.class);
        i.putExtra("id", recordList.get(position).getId());
        startActivity(i);
    }
}