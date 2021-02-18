package com.example.parentalapp.admin.questionbank;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.parentalapp.quiz.question.QuizDBHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadQuestionFile extends AsyncTask<String, String, String> {
    private QuestionBankDBHelper questionBankDBHelper;
    private Context context;
    private TaskDelegate delegate;
    private String parentFilePath = "/sdcard/";
    private boolean downloadSuccess = false;

    public DownloadQuestionFile(Context context, TaskDelegate delegate){
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // set things before downlaod
        questionBankDBHelper = new QuestionBankDBHelper(context);
    }

    @Override
    protected String doInBackground(String... url) {
        String savedFilePath = parentFilePath + url[1] + ".xls";
        try{
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpsURLConnection connection = null;
            try{
                URL downloadURL = new URL(url[0]);
                connection = (HttpsURLConnection) downloadURL.openConnection();
                connection.connect();

                // connection failed
                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return connection.getResponseMessage();

                int fileLength = connection.getContentLength();

                inputStream = connection.getInputStream();
                outputStream = new FileOutputStream(savedFilePath);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1){
                    if (isCancelled())
                        return null;
                    total += count;

                    outputStream.write(data, 0, count);
                }
                downloadSuccess = true;

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    if(outputStream != null)
                        outputStream.close();
                    if(inputStream != null)
                        inputStream.close();
                }catch (IOException ignored){}
                if (connection != null)
                    connection.disconnect();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return savedFilePath;
    }

    @Override
    protected void onPostExecute(String s) {
        if(downloadSuccess){
            int sourceId = questionBankDBHelper.insertQuestion(s);
            Toast.makeText(context, "Downlaod completed.", Toast.LENGTH_SHORT).show();
            delegate.taskCompleteNotify(String.valueOf(sourceId));
        }else{
            Toast.makeText(context, "Downlaod failed.", Toast.LENGTH_SHORT).show();
        }
        //questionBankDBHelper.insertQuestion("/sdcard/Download/question.xls");
    }

    public interface TaskDelegate{
        public void taskCompleteNotify(String result);
    }
}
