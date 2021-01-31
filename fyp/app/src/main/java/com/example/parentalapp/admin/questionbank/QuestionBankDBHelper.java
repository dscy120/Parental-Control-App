package com.example.parentalapp.admin.questionbank;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.Nullable;

import com.example.parentalapp.database.DatabaseOpenHelper;
import com.example.parentalapp.quiz.QuizConstant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class QuestionBankDBHelper extends DatabaseOpenHelper {

    public static final String SOURCE_ID = "id";
    public static final String SOURCE_TITLE = "title";
    public static final String SOURCE_SUBJECT = "subject";
    public static final String SOURCE_LEVEL = "level";
    public static final String SOURCE_PUBLISHER = "publisher";
    public static final String SOURCE_DOWNLAOD_LINK = "download_link";
    public static final String SOURCE_DOWNLOAD_STATUS = "download_status";

    // old https://drive.google.com/file/d/1R-a2OVDeOKw2e49W9WjC-owsKsZwjpuL/view?usp=sharing
    // new https://drive.google.com/file/d/16BwVWtWM7P45WnfIPkOx5o5tZIbMrneT/view?usp=sharing
    String url = "https://drive.google.com/uc?id=1R-a2OVDeOKw2e49W9WjC-owsKsZwjpuL&export=download";
    Context context;
    Workbook workbook;

    public QuestionBankDBHelper(Context context) {
        super(context);
    }

    public ArrayList<QuestionBankSource> getSource(){
        ArrayList<QuestionBankSource> sourceList = new ArrayList<>();
        String sql = "SELECT * FROM question_source";
        Cursor c = super.query(sql);

        if (c.moveToFirst()){
            do{
                int id = c.getInt(c.getColumnIndex(SOURCE_ID));
                String subject = c.getString(c.getColumnIndex(SOURCE_SUBJECT));
                String level = c.getString(c.getColumnIndex(SOURCE_LEVEL));
                String title = c.getString(c.getColumnIndex(SOURCE_TITLE));
                String publisher = c.getString(c.getColumnIndex(SOURCE_PUBLISHER));
                String downloadLink = c.getString(c.getColumnIndex(SOURCE_DOWNLAOD_LINK));
                int downloadStatus = c.getInt(c.getColumnIndex(SOURCE_DOWNLOAD_STATUS));
                QuestionBankSource q = new QuestionBankSource(id, subject, level, title, publisher, downloadLink, downloadStatus);
                sourceList.add(q);
            }while(c.moveToNext());
        }
        c.close();

        return sourceList;
    }

    public String downloadQuestion(String url){
        //temp url
        url = this.url;

        String file_name = "question.xls";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                DownloadManager.Request.NETWORK_MOBILE) ;
        request.setTitle("download");
        request.setDescription("test downlaod");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "" + file_name);

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return file_name;
    }

    public void insertQuestion(String filePath){
        filePath = "/sdcard/Download/question.xls";

        File inputFile = new File(filePath);

        try{
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);

            workbook = Workbook.getWorkbook(inputFile);
            Sheet sheet = workbook.getSheet(0);

            for(int i = 1; i < sheet.getRows(); i++){
                Cell[] row = sheet.getRow(i);
                String category = row[0].getContents();
                String difficulty = row[1].getContents();
                int questionType = Integer.parseInt(row[2].getContents());
                String question = row[3].getContents();
                String option1 = row[4].getContents();
                String option2 = row[5].getContents();
                String option3 = row[5].getContents();
                String option4 = row[6].getContents();
                String correctAns = row[7].getContents();
                String explanation = row[8].getContents();
                addQuestion(category, difficulty, questionType, question, option1, option2,
                        option3, option4, correctAns, explanation);
            }


        }catch (IOException e) {
            e.printStackTrace();
        }catch (BiffException e){
            e.printStackTrace();
        }

    }

    public void addQuestion(String questionCategory, String difficulty, int questionType, String question,
                            @Nullable String option1, @Nullable String option2, @Nullable String option3,
                            @Nullable String option4, String correctAns, String explanation){
        ContentValues cv = new ContentValues();
        cv.put(QuizConstant.QuestionTable.CATEGORY, questionCategory);
        cv.put(QuizConstant.QuestionTable.DIFFICULTY, difficulty);
        cv.put(QuizConstant.QuestionTable.COLUMN_QUESTION_TYPE, questionType);
        cv.put(QuizConstant.QuestionTable.COLUMN_QUESTION, question);
        cv.put(QuizConstant.QuestionTable.COLUMN_OPTION1, option1);
        cv.put(QuizConstant.QuestionTable.COLUMN_OPTION2, option2);
        cv.put(QuizConstant.QuestionTable.COLUMN_OPTION3, option3);
        cv.put(QuizConstant.QuestionTable.COLUMN_OPTION4, option4);
        cv.put(QuizConstant.QuestionTable.COLUMN_ANSWER, correctAns);
        cv.put(QuizConstant.QuestionTable.COLUMN_EXPLANATION, explanation);
        execSQL(QuizConstant.QuestionTable.TABLE_NAME, cv);
    }
}
