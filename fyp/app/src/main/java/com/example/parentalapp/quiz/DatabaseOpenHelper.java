package com.example.parentalapp.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.parentalapp.quiz.question.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static String TAG = "DatabaseOpenHelper";
    private static final String DATABASE_PATH = "/data/data/com.example.parentalapp/databases/";
    private static final String DATABASE_NAME = "quizModule.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private final Context context;

    @Override
    public void onCreate(SQLiteDatabase db) {
        //empty
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //empty
    }

    public DatabaseOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void createDB() throws IOException{
        boolean dbExist = checkDB();
        if(!dbExist){
            this.getReadableDatabase();
            this.close();
            try{
                copyDB();
            }catch (IOException e){
                e.printStackTrace();
                throw new Error("ErrorCopyingDatabase");
            }finally {
                this.close();
            }
        }
    }

    private boolean checkDB(){
        try{
            final String path = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(path);
            if(file.exists()){
                return true;
            }else{
                return false;
            }
        }catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }

    private void copyDB() throws IOException {
        try{
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean openDB() throws SQLException {
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        return db != null;
    }

    @Override
    public synchronized void close() {
        if(db != null){
            db.close();
        }
        super.close();
    }

    // Retrieve all question in table
    public List<Question> getQuestions(String subject, String difficulty){
        try{
            createDB();
        }catch (IOException e){
            e.printStackTrace();
        }
        List<Question> questionList = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizConstant.QuestionTable.TABLE_NAME + " WHERE category=\'" + subject + "\' AND difficulty=\'" + difficulty + "\'", null);

        if(c.moveToFirst()){
            do{
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuizConstant.QuestionTable._ID)));
                question.setQuestionCategory(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.CATEGORY)));
                question.setQuestionType(c.getInt(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_QUESTION_TYPE)));
                question.setQuestion(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_OPTION4)));
                question.setCorrectAns(c.getString(c.getColumnIndex(QuizConstant.QuestionTable.COLUMN_ANSWER)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    // getting record of each attempt (sum of score)
    public Cursor getAllRecord(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT " + QuizConstant.QuestionTable.ATTEMPT_ID + "," + QuizConstant.QuestionTable.CATEGORY + "," + "SUM(" + QuizConstant.QuestionTable.SCORE + ")" +
                " FROM " + QuizConstant.QuestionTable.TABLE_NAME_RECORD + " GROUP BY " + QuizConstant.QuestionTable.ATTEMPT_ID, null);
        return c;
    }

    // getting detail attempt by attempt id
    public Cursor getAttemptDetail(int id){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + QuizConstant.QuestionTable.TABLE_NAME_RECORD + " AS r " +
                        "INNER JOIN " + QuizConstant.QuestionTable.TABLE_NAME + " q " +
                "ON q." + QuizConstant.QuestionTable._ID + "=r." + QuizConstant.QuestionTable.QUESTION_ID +
                " WHERE r." + QuizConstant.QuestionTable.ATTEMPT_ID + "=" + id, null);
        return c;
    }

    // add new record to record db
    public void addRecordDB(Question question,int attemptId, CharSequence[] input,  int score){
        for(CharSequence i : input){
            ContentValues cv = new ContentValues();
            cv.put(QuizConstant.QuestionTable.ATTEMPT_ID, attemptId);
            cv.put(QuizConstant.QuestionTable.CATEGORY, question.getQuestionCategory());
            cv.put(QuizConstant.QuestionTable.QUESTION_ID, question.getId());
            cv.put(QuizConstant.QuestionTable.ATTEMPTED_ANSWER, i.toString());
            cv.put(QuizConstant.QuestionTable.CORRECT_ANSWER, question.getCorrectAns());
            cv.put(QuizConstant.QuestionTable.SCORE, score);
            db.insert(QuizConstant.QuestionTable.TABLE_NAME_RECORD, null, cv);
        }
    }

}
