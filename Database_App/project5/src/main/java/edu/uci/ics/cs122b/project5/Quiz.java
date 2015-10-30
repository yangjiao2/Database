package edu.uci.ics.cs122b.project5;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.os.Handler;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;



public class Quiz extends Activity {

    private DatabaseConnector connect;
    private int correct = -1;
    private long startTime = 0;
    private TextView timer;
    private Handler timerHandler = new Handler();
    private SharedPreferences preference;

    private Button answer0;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button next;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int)(millis/1000);
            int minutes = seconds/60;
            if(minutes == 1){
                System.out.println("Time up!");
                return;
            }
            timer = (TextView)findViewById(R.id.timer);
            timer.setText(String.format("%d:%02d",minutes,seconds));
            timerHandler.postDelayed(this, 500);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        this.preference = getSharedPreferences("edu.uci.ics.cs122b.project5", Context.MODE_PRIVATE);
        connect = new DatabaseConnector(this);
        timer = (TextView)findViewById(R.id.timer);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);



        randomQuestion();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void randomQuestion(){
        Random r = new Random();
        //int question = r.nextInt(7-0);
       // if(question == 0){

            String q = "Who directed ";
            Movie movie = getRandomMovie();
            System.out.println("Random movie is " + movie.getTitle());
            TextView ques = (TextView)findViewById(R.id.questionArea);
            ques.setText(q + movie.getTitle());
            ArrayList<String> wrongDirectors = getRandomWrongDirectors(movie.getDirector());
            correct = r.nextInt(4);
            System.out.println(correct);
            if(correct == 0){
                answer0 = (Button)findViewById(R.id.answer0);
                answer0.setText(movie.getDirector());
            }
            else if(correct == 1){
                answer1 = (Button)findViewById(R.id.answer1);
                answer1.setText(movie.getDirector());
            }
            else if(correct == 2){
                answer2 = (Button)findViewById(R.id.answer2);
                answer2.setText(movie.getDirector());
            }
            else if(correct == 3){
                answer3 = (Button)findViewById(R.id.answer3);
                answer3.setText(movie.getDirector());
            }
            setIncorrectAnswers(correct,wrongDirectors);
                        //randomly fetch movie
            //get director
            //set random button to correct answer, save button id
            //set remaining to random other directors
            //on button click, check button id
                //if same as saved correct
                //else wrong
        answer0.setEnabled(true);
        answer1.setEnabled(true);
        answer2.setEnabled(true);
        answer3.setEnabled(true);

       // }

    }



    private Movie getRandomMovie(){
        Movie m = null;
        Cursor c = connect.getResults("SELECT * FROM movies ORDER BY RANDOM() LIMIT 1;");
        if(c.moveToFirst()){
             m = new Movie(c.getInt(0),c.getString(1),c.getInt(2),c.getString(3));

        }
        return m;
    }


    private ArrayList<String> getRandomWrongDirectors(String rightDirector){
        ArrayList<String> wrongDirectors = new ArrayList<String>();
        Cursor c = connect.getResults("SELECT director FROM movies WHERE director !='"+rightDirector+ "' ORDER BY RANDOM() LIMIT 3;");
        while(c.moveToNext()){
            wrongDirectors.add(c.getString(0));
        }
        return wrongDirectors;
    }


    private void setIncorrectAnswers(int c,ArrayList<String> list){
        if(c == 0){
            answer1 = (Button)findViewById(R.id.answer1);
            answer1.setText(list.get(0));
            answer2 = (Button)findViewById(R.id.answer2);
            answer2.setText(list.get(1));
            answer3 = (Button)findViewById(R.id.answer3);
            answer3.setText(list.get(2));
        }
        else if(c == 1){
            answer0 = (Button)findViewById(R.id.answer0);
            answer0.setText(list.get(0));
            answer2 = (Button)findViewById(R.id.answer2);
            answer2.setText(list.get(1));
            answer3 = (Button)findViewById(R.id.answer3);
            answer3.setText(list.get(2));
        }
        else if(c == 2){
            answer0 = (Button)findViewById(R.id.answer0);
            answer0.setText(list.get(0));
            answer1 = (Button)findViewById(R.id.answer1);
            answer1.setText(list.get(1));
            answer3 = (Button)findViewById(R.id.answer3);
            answer3.setText(list.get(2));
        }
        else if(c == 3){
            answer0 = (Button)findViewById(R.id.answer0);
            answer0.setText(list.get(0));
            answer1 = (Button)findViewById(R.id.answer1);
            answer1.setText(list.get(1));
            answer2 = (Button)findViewById(R.id.answer2);
            answer2.setText(list.get(2));
        }
    }

    public void checkAnswer(View v){
        answer0.setEnabled(false);
        answer1.setEnabled(false);
        answer2.setEnabled(false);
        answer3.setEnabled(false);
        switch(v.getId()){
            case R.id.answer0:
                if(correct == 0){
                    v.setBackgroundColor(Color.GREEN);
                    correctAnswer();
                }
                else{
                    v.setBackgroundColor(Color.RED);
                    incorrectAnswer();
                }
                break;
            case R.id.answer1:
                if(correct == 1){
                    v.setBackgroundColor(Color.GREEN);
                    correctAnswer();
                }
                else{
                    v.setBackgroundColor(Color.RED);
                    incorrectAnswer();
                }
                break;
            case R.id.answer2:
                if(correct == 2){
                    v.setBackgroundColor(Color.GREEN);
                    correctAnswer();
                }
                else{
                    v.setBackgroundColor(Color.RED);
                    incorrectAnswer();
                }
                break;
            case R.id.answer3:
                if(correct == 3){
                    v.setBackgroundColor(Color.GREEN);
                    correctAnswer();
                }
                else{
                    v.setBackgroundColor(Color.RED);
                    incorrectAnswer();
                }
                break;

        }
        next = (Button)findViewById(R.id.next);
        next.setEnabled(true);

    }

    public void nextQuestion(View v){
        v.setEnabled(false);
        answer0.setBackgroundResource(android.R.drawable.btn_default);
        answer1.setBackgroundResource(android.R.drawable.btn_default);
        answer2.setBackgroundResource(android.R.drawable.btn_default);
        answer3.setBackgroundResource(android.R.drawable.btn_default);

        randomQuestion();
    }


    private void correctAnswer(){

        System.out.println("Correct");
    }

    private void incorrectAnswer(){
        System.out.println("incorrect");
    }


}
