package com.example.khoahoang.simplemathbraintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


//. Generate a new problem, display rightOrWrong, update genAns, and update progress with every click of a choice
//TODO:
public class MainActivity extends AppCompatActivity {

    CountDownTimer clock;
    List<Integer> candAnswers;
    TextView problem, progress, rightOrWrong, result, c1, c2, c3, c4;
    Button playAgain, quit;
    int answer, probsDone, numWrong;
    int totalProbs = 5;
    boolean gameActive = true;

    public void processChoice(View view) {
        //Increment probsDone and set the progress TextView.
        if (gameActive) {
            int userAns = Integer.parseInt(((TextView) (view)).getText().toString());
            //System.out.println(userAns);
            if (userAns == answer) {
                rightOrWrong.setText("RIGHT");
            } else {
                rightOrWrong.setText("WRONG");
                numWrong++;
            }

            probsDone++;
            progress.setText(Integer.toString(probsDone) + "/" + Integer.toString(totalProbs));
            if (probsDone == totalProbs) {
                gameActive = false;
                rightOrWrong.setVisibility(View.INVISIBLE);
                result.setText("SCORE: " + Integer.toString((totalProbs-numWrong)) + "/" + Integer.toString(totalProbs));
                playAgain.setVisibility(View.VISIBLE); quit.setVisibility(View.VISIBLE);
                clock.cancel();
            } else {
                genNewProblem();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timer = (TextView)findViewById(R.id.timer);
        problem = (TextView)findViewById(R.id.problem);
        progress = (TextView)findViewById(R.id.progress);
        rightOrWrong = (TextView)findViewById(R.id.rightOrWrong);
        result = (TextView)findViewById(R.id.result);
        playAgain = (Button)findViewById(R.id.playAgain);
        quit = (Button)findViewById(R.id.quit);
        probsDone = 0;
        c1 = (TextView)findViewById(R.id.c1);
        c2 = (TextView)findViewById(R.id.c2);
        c3 = (TextView)findViewById(R.id.c3);
        c4 = (TextView)findViewById(R.id.c4);

        clock = new CountDownTimer(16000, 500) {

            @Override
            public void onTick(long l) {
                //. Update the timer
                timer.setText(Long.toString(l/1000) + "s");
                //You're going down to the proper number of seconds because of how l/1000 rounds down.
            }

            @Override
            public void onFinish() {
                //. Make everything unclickable (so you should add a gameActive boolean variable or something).
                //. Display result
                timer.setText("0s");
            }

        }.start();
        progress.setText(Integer.toString(probsDone) + "/" + Integer.toString(totalProbs));
        genNewProblem();
    }


    public void genNewProblem() {
        int o1 = (int)(Math.random()*10);
        int o2 = (int)(Math.random()*10);
        answer = o1+o2;
        int candAnsRadius = 3;

        candAnswers = genCandAnswers(answer, candAnsRadius);
        long seed = System.nanoTime();
        Collections.shuffle(candAnswers, new Random(seed));

        problem.setText(Integer.toString(o1) + " + " + Integer.toString(o2));
        c1.setText(Integer.toString(candAnswers.get(0)));
        c2.setText(Integer.toString(candAnswers.get(1)));
        c3.setText(Integer.toString(candAnswers.get(2)));
        c4.setText(Integer.toString(candAnswers.get(3)));
    }


    public List<Integer> genCandAnswers(int answer, int candRadius) {
        int cand;
        Random rand = new Random();
        List<Integer> candAnswers = new ArrayList<Integer>();

        candAnswers.add(answer);
        while (candAnswers.size() < 4) {
            while ((cand = rand.nextInt(((answer + candRadius) - (answer - candRadius)) + 1) + (answer - candRadius)) == answer ||
                    candAnswers.contains(cand)) {
            }
            candAnswers.add(cand);
        }

        return candAnswers;
    }
}
