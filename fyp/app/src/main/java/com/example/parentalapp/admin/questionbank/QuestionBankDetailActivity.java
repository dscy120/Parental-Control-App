package com.example.parentalapp.admin.questionbank;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import java.io.File;

import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_ID;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_TITLE;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_SUBJECT;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_LEVEL;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_PUBLISHER;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLAOD_LINK;
import static com.example.parentalapp.admin.questionbank.QuestionBankDBHelper.SOURCE_DOWNLOAD_STATUS;

public class QuestionBankDetailActivity extends AppCompatActivity implements DownloadQuestionFile.TaskDelegate {

    private TextView title, subject, level, publisher, status;
    private Button buttonDownload, buttonDelete;
    private QuestionBankDBHelper questionBankDBHelper;
    private QuestionBankSource q;
    private DownloadQuestionFile.TaskDelegate taskDelegate = this;
    private int sourceId;
    private Dialog dialog;

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
                startActivity(new Intent(getApplicationContext(), QuestionBankActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                showDownloadDialog();
                //questionBankDBHelper.downloadQuestion(getIntent().getExtras().getString(SOURCE_DOWNLAOD_LINK));\
                new DownloadQuestionFile(getApplicationContext(), taskDelegate).execute(q.getDownloadLink(), String.valueOf(q.getId()));
                Toast.makeText(getApplicationContext(), "Download started", Toast.LENGTH_SHORT).show();
            }
        });

        // button to delete the question bank source
        buttonDelete = findViewById(R.id.button_delete_source);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionBankDBHelper.removeQuestions(sourceId)){
                    questionBankDBHelper.updateStatus(sourceId, 0);
                    Toast.makeText(getApplicationContext(), "Question source has been removed", Toast.LENGTH_SHORT).show();
                }
                updateText();
            }
        });
    }

    private void updateText(){
        sourceId = getIntent().getExtras().getInt(SOURCE_ID);
        q = questionBankDBHelper.getSourceDetail(sourceId);
        title.setText("Title: " + q.getTitle());
        subject.setText("Subject: " + q.getSubject());
        level.setText("Level: " + q.getLevel());
        publisher.setText("Publisher: " + q.getPublisher());

        if(q.getDownloadStatus() == 1){
            status.setText("Exercise Downloaded");
            buttonDownload.setEnabled(false);
            buttonDelete.setEnabled(true);
        }else{
            buttonDownload.setEnabled(true);
            buttonDelete.setEnabled(false);
        }
    }

    private void showDownloadDialog(){
        dialog = new AlertDialog.Builder(this)
                .setTitle("Downloading")
                .setMessage("Please wait for a moment")
                .show();
    }

    @Override
    public void taskCompleteNotify(String result) {
        dialog.dismiss();
        if(Integer.parseInt(result) >= 0){
            updateText();
        }else{
            dialog = new AlertDialog.Builder(this)
                    .setTitle("Download failed.")
                    .setMessage("Please check your network connection.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

    }
}