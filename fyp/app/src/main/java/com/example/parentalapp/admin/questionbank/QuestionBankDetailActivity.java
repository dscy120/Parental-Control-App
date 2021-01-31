package com.example.parentalapp.admin.questionbank;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_ID;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_TITLE;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_SUBJECT;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_LEVEL;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_PUBLISHER;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLAOD_LINK;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLOAD_STATUS;

public class QuestionBankDetailActivity extends AppCompatActivity implements DownloadQuestionFile.TaskDelegate {

    TextView title, subject, level, publisher, status;
    Button buttonDownload;
    QuestionBankDBHelper questionBankDBHelper;
    QuestionBankSource q;
    DownloadQuestionFile.TaskDelegate taskDelegate = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank_detail);

        //back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        questionBankDBHelper = new QuestionBankDBHelper(getApplicationContext());


        setDetails();

        updateText();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, QuestionBankActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setDetails(){
        title = findViewById(R.id.textView_question_bank_detail_title);

        subject = findViewById(R.id.textView_question_bank_detail_subject);

        level = findViewById(R.id.textView_question_bank_detail_level);

        publisher = findViewById(R.id.textView_question_bank_detail_publisher);

        status = findViewById(R.id.textView_question_bank_detail_status);

        buttonDownload = findViewById(R.id.button_question_bank_detail_download);
        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //questionBankDBHelper.downloadQuestion(getIntent().getExtras().getString(SOURCE_DOWNLAOD_LINK));\
                new DownloadQuestionFile(getApplicationContext(), taskDelegate).execute(q.getDownloadLink(), String.valueOf(q.getId()));
                Toast.makeText(getApplicationContext(), "Download started", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateText(){
        q = questionBankDBHelper.getSourceDetail(getIntent().getExtras().getInt(SOURCE_ID));
        title.setText("Title: " + q.getTitle());
        subject.setText("Subject: " + q.getSubject());
        level.setText("Level: " + q.getLevel());
        publisher.setText("Publisher: " + q.getPublisher());

        if(q.getDownloadStatus() == 1){
            status.setText("Exercise Downloaded");
            buttonDownload.setEnabled(false);
        }
    }

    @Override
    public void taskCompleteNotify(String result) {
        updateText();
    }
}