package com.example.parentalapp.quiz.question;

import android.content.Context;
import android.database.Cursor;

import com.example.parentalapp.database.DatabaseOpenHelper;
import com.example.parentalapp.quiz.QuizConstant;

import java.util.ArrayList;
import java.util.List;

public class QuizDBHelper extends DatabaseOpenHelper {
    public QuizDBHelper(Context context) {
        super(context);
    }

    public List<Question> getQuestions(String subject, String difficulty){
        List<Question> questionList = new ArrayList<>();
        String sql = "SELECT * FROM " + QuizConstant.QuestionTable.TABLE_NAME
                + " WHERE category=\'" + subject + "\' AND difficulty=\'" + difficulty + "\'";
        Cursor c = super.query(sql);

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


}
