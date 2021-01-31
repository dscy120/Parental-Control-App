package com.example.parentalapp.quiz;

import android.provider.BaseColumns;

// A class to store constants
public final class QuizConstant {
    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_bank";
        public static final String CATEGORY = "category";
        public static final String DIFFICULTY = "difficulty";
        public static final String COLUMN_QUESTION_TYPE = "question_type";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_EXPLANATION = "explanation";
        public static final String COLUMN_SOURCE_ID = "source_id";

        // constant for record
        public static final String TABLE_NAME_RECORD = "record";
        public static final String ATTEMPT_ID = "attempt_id";
        public static final String QUESTION_ID = "question_id";
        public static final String ATTEMPTED_ANSWER = "attempted_answer";
        public static final String CORRECT_ANSWER = "correct_answer";
        public static final String SCORE = "score";
    }

    public static class QuizRecord implements BaseColumns{

    }
}
