package com.example.parentalapp.quiz.question;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.question.questionFragment.FragmentListener;
import com.example.parentalapp.quiz.question.questionFragment.QuestionFragment;
import com.example.parentalapp.quiz.record.RecordDBHelper;

import java.util.Collections;
import java.util.List;

public class QuestionDisplayActivity extends AppCompatActivity implements FragmentListener {

    ConstraintLayout quizLayout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    QuestionFragment questionFragment;
    QuizDBHelper dbHelper;
    RecordDBHelper recordDBHelper;

    private TextView textView_questionNumber, textView_question;


    private int attemptId;
    private String difficulty;
    private String subject;
    private List<Question> questionList;
    private int questionCounter = 0;
    private int questionTotal;
    private Question currentQuestion;

    private int totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_display);

        quizLayout = findViewById(R.id.quizDisplayLayout);

        textView_questionNumber = findViewById(R.id.textView_questionNumber);
        textView_question = findViewById(R.id.textView_question);

        // Connect to database and get all question
        dbHelper = new QuizDBHelper(getApplicationContext());
        recordDBHelper = new RecordDBHelper(getApplicationContext());
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            subject = extras.getString("subject");
            difficulty = extras.getString("difficulty");
        }
        questionList = dbHelper.getQuestions(subject, difficulty);
        Collections.shuffle(questionList);
        questionTotal = Math.min(5, questionList.size());
        totalScore = 0;

        // set attempt id
        SharedPreferences sharedPreferences = getSharedPreferences("quizPreference", MODE_PRIVATE);
        attemptId = sharedPreferences.getInt("attemptId", 1);
        sharedPreferences.edit().putInt("attemptId", attemptId + 1).apply();

        // Show the first Question
        showNextQuestion();

    }

    // When the Fragment returns the answer
    @Override
    public void onInputSent(CharSequence[] input) {
        checkAnswer(input);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(questionFragment).commit();
        showNextQuestion();
    }

    // Display the next question
    private void showNextQuestion(){

        if (questionCounter < questionTotal){
            currentQuestion = questionList.get(questionCounter);

            setQuestionFormat();

            textView_question.setText(currentQuestion.getQuestion());

            questionCounter++;
            textView_questionNumber.setText("Question: " + questionCounter);

        }else{
            showResult();
        }
    }

    // Set display for different question type
    private void setQuestionFormat(){
        int questionType = currentQuestion.getQuestionType();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        questionFragment = new QuestionFragment();
        Bundle b = new Bundle();

        switch (questionType){
            // Multiple Choice
            case 1:
                b.putInt("question_type", questionType);
                b.putString("option1", currentQuestion.getOption1());
                b.putString("option2", currentQuestion.getOption2());
                b.putString("option3", currentQuestion.getOption3());
                b.putString("option4", currentQuestion.getOption4());
                questionFragment.setArguments(b);
                break;

            // Fill in the blank
            case 2:
                b.putInt("question_type", questionType);
                questionFragment.setArguments(b);
                break;

            default:

        }
        fragmentTransaction.add(R.id.fragment_container, questionFragment, null);
        fragmentTransaction.commit();
    }

    // Check if answer is correct
    // Increment total score if correct
    private void checkAnswer(CharSequence[] input){

        boolean correct = true;
        // TODO: Check multiple anwer
        for (CharSequence ans : input){
            if (ans.toString().compareTo(currentQuestion.getCorrectAns()) != 0){
                correct = false;
                break;
            }
        }
        if (correct){
            // Correct answer
            totalScore++;
        }
        recordAttempt(input, correct);
    }

    // write attempt into record database
    private void recordAttempt(CharSequence[] input, boolean correct){
        int score;
        if(correct){
            score = 1;
        }else{
            score = 0;
        }
        recordDBHelper.addRecordDB(currentQuestion, attemptId, input, score);
    }

    // Show final score
    private void showResult(){
        Intent i = new Intent(getApplicationContext(), QuizResultActivity.class);
        i.putExtra("Score", totalScore);
        i.putExtra("TotalQuestion", questionTotal);
        i.putExtra("Difficulty", difficulty);
        dbHelper.close();
        startActivity(i);
    }

}