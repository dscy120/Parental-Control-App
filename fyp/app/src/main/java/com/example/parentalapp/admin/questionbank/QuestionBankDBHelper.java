package com.example.parentalapp.admin.questionbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

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

    public static final String TABLE_NAME_SOURCE = "question_source";
    public static final String SOURCE_ID = "id";
    public static final String SOURCE_TITLE = "title";
    public static final String SOURCE_SUBJECT = "subject";
    public static final String SOURCE_LEVEL = "level";
    public static final String SOURCE_PUBLISHER = "publisher";
    public static final String SOURCE_DOWNLAOD_LINK = "download_link";
    public static final String SOURCE_DOWNLOAD_STATUS = "download_status";

    // Original link format: https://drive.google.com/file/d/1R-a2OVDeOKw2e49W9WjC-owsKsZwjpuL/view?usp=sharing
    // Download link format: https://drive.google.com/uc?id=1R-a2OVDeOKw2e49W9WjC-owsKsZwjpuL&export=download
    Workbook workbook;
    Context context;

    public QuestionBankDBHelper(Context context) {
        super(context);
        this.context = context;
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

    public QuestionBankSource getSourceDetail(int sourceId){
        QuestionBankSource q = null;
        String sql = "SELECT * FROM question_source WHERE id = " + sourceId;
        Cursor c = super.query(sql);
        if (c.moveToFirst()){
            int id = c.getInt(c.getColumnIndex(SOURCE_ID));
            String subject = c.getString(c.getColumnIndex(SOURCE_SUBJECT));
            String level = c.getString(c.getColumnIndex(SOURCE_LEVEL));
            String title = c.getString(c.getColumnIndex(SOURCE_TITLE));
            String publisher = c.getString(c.getColumnIndex(SOURCE_PUBLISHER));
            String downloadLink = c.getString(c.getColumnIndex(SOURCE_DOWNLAOD_LINK));
            int downloadStatus = c.getInt(c.getColumnIndex(SOURCE_DOWNLOAD_STATUS));
            q = new QuestionBankSource(id, subject, level, title, publisher, downloadLink, downloadStatus);
        }
        c.close();
        return q;
    }

    public int insertQuestion(String filePath){

        File inputFile = new File(filePath);

        int source = 0;

        try{
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setGCDisabled(true);

            workbook = Workbook.getWorkbook(inputFile);
            Sheet sheet = workbook.getSheet(0);

            source = Integer.parseInt(sheet.getRow(1)[10].getContents());

            for(int i = 1; i < sheet.getRows(); i++){
                Cell[] row = sheet.getRow(i);
                if (row != null){
                    String category = row[0].getContents();
                    String difficulty = row[1].getContents();
                    int questionType = Integer.parseInt(row[2].getContents());
                    String question = row[3].getContents();
                    String option1 = row[4].getContents();
                    String option2 = row[5].getContents();
                    String option3 = row[6].getContents();
                    String option4 = row[7].getContents();
                    String correctAns = row[8].getContents();
                    String explanation = row[9].getContents();
                    int sourceId = Integer.parseInt(row[10].getContents());
                    addQuestion(category, difficulty, questionType, question, option1, option2,
                            option3, option4, correctAns, explanation, sourceId);
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }catch (BiffException e){
            e.printStackTrace();
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return source;

    }

    public void addQuestion(String questionCategory, String difficulty, int questionType, String question,
                            @Nullable String option1, @Nullable String option2, @Nullable String option3,
                            @Nullable String option4, String correctAns, String explanation, int sourceId){
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
        cv.put(QuizConstant.QuestionTable.COLUMN_SOURCE_ID, sourceId);
        if (insertSQL(QuizConstant.QuestionTable.TABLE_NAME, cv)){
            updateStatus(sourceId, 1);
        }else{
            Toast.makeText(context, "Unable to add question", Toast.LENGTH_SHORT).show();
        }
    }

    public int updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(SOURCE_DOWNLOAD_STATUS, status);
        return updateSQL(TABLE_NAME_SOURCE, cv, id);
    }
}
