package com.example.parentalapp.quiz.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parentalapp.database.DatabaseOpenHelper;
import com.example.parentalapp.quiz.QuizConstant;
import com.example.parentalapp.quiz.question.Question;

public class RecordDBHelper extends DatabaseOpenHelper {
    public RecordDBHelper(Context context) {
        super(context);
    }

    // getting record of each attempt (sum of score)
    public Cursor getAllRecord(){
        String sql = "SELECT " + QuizConstant.QuestionTable.ATTEMPT_ID + "," + QuizConstant.QuestionTable.CATEGORY + "," + "SUM(" + QuizConstant.QuestionTable.SCORE + ")" +
                " FROM " + QuizConstant.QuestionTable.TABLE_NAME_RECORD + " GROUP BY " + QuizConstant.QuestionTable.ATTEMPT_ID;
        return super.query(sql);
    }

    // getting detail attempt by attempt id
    public Cursor getAttemptDetail(int id){
        String sql = "SELECT * FROM " + QuizConstant.QuestionTable.TABLE_NAME_RECORD + " AS r " +
                "INNER JOIN " + QuizConstant.QuestionTable.TABLE_NAME + " q " +
                "ON q." + QuizConstant.QuestionTable._ID + "=r." + QuizConstant.QuestionTable.QUESTION_ID +
                " WHERE r." + QuizConstant.QuestionTable.ATTEMPT_ID + "=" + id;
        return super.query(sql);
    }

    // add new record to record db
    public void addRecordDB(Question question, int attemptId, CharSequence[] input, int score){
        SQLiteDatabase db = this.getWritableDatabase();
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
