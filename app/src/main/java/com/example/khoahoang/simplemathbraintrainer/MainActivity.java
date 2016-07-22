package com.example.khoahoang.simplemathbraintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//TODO: Allow user to quit.
public class MainActivity extends AppCompatActivity {

    CountDownTimer clock;
    List<Integer> candAnswers;
    TextView problem, progress, rightOrWrong, result, gameState, c1, c2, c3, c4;
    RelativeLayout gameRelativeLayout;
    Button playAgain, quit;
    int answer, probsDone, numWrong;
    int startingMillis = 15000; //change back to 15000.
    int totalProbs = 5;
    boolean gameActive = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameRelativeLayout = (RelativeLayout)findViewById(R.id.gameRelativeLayout);
        problem = (TextView)findViewById(R.id.problem);
        progress = (TextView)findViewById(R.id.progress);
        rightOrWrong = (TextView)findViewById(R.id.rightOrWrong);
        result = (TextView)findViewById(R.id.result);
        gameState = (TextView)findViewById(R.id.gameState);
        playAgain = (Button)findViewById(R.id.playAgain);
        quit = (Button)findViewById(R.id.quit);
        c1 = (TextView)findViewById(R.id.c1);
        c2 = (TextView)findViewById(R.id.c2);
        c3 = (TextView)findViewById(R.id.c3);
        c4 = (TextView)findViewById(R.id.c4);
    }


    public void processStart(View view) {
        view.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(View.VISIBLE);
        processPlayAgain(playAgain);
    }


    public void processChoice(View view) {
        if (gameActive) {
            int userAns = Integer.parseInt(((TextView) (view)).getText().toString());
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
                gameState.setText("GAME OVER");
                displayResult();
                clock.cancel();
            } else {
                genNewProblem();
            }
        }
    }


    public void processPlayAgain(View view) {
        gameState.setText("");
        result.setText("");

        final TextView timer = (TextView)findViewById(R.id.timer);
        clock = new CountDownTimer(startingMillis+100, 1000) { //+100 to fix the delay

            @Override
            public void onTick(long l) {
                timer.setText(Long.toString(l/1000) + "s");
                //You're going down to the proper number of seconds because of how l/1000 rounds down.
            }

            @Override
            public void onFinish() {
                timer.setText("0s");
                gameActive = false;
                gameState.setText("TIME UP");
                displayResult();
            }
        }.start();

        probsDone = 0;
        numWrong = 0;
        gameActive = true;
        progress.setText(Integer.toString(probsDone) + "/" + Integer.toString(totalProbs));
        genNewProblem();
    }


    /**
     * Generates a new problem to be displayed
     */
    public void genNewProblem() {
        int o1 = (int)(Math.random()*10);
        int o2 = (int)(Math.random()*10);
        answer = o1+o2;
        int candAnsRadius = 3;

        //Instead of shuffling ArrayList elements like you do here, you could choose a random index value and run a for
        //loop where for each iteration, if the loop control variable equals the random index, then you add the correct
        //answer to candAnswers. Else, you generate a random answer.
        candAnswers = genCandAnswers(answer, candAnsRadius);
        long seed = System.nanoTime();
        Collections.shuffle(candAnswers, new Random(seed));

        problem.setText(Integer.toString(o1) + " + " + Integer.toString(o2));
        c1.setText(Integer.toString(candAnswers.get(0)));
        c2.setText(Integer.toString(candAnswers.get(1)));
        c3.setText(Integer.toString(candAnswers.get(2)));
        c4.setText(Integer.toString(candAnswers.get(3)));
    }


    /**
     * Generates a set of candidate answers for a problem
     * @param answer The answer to the problem
     * @param candRadius Defines the range in which candidate answers will vary
     * @return A list of candidate answers
     */
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


    /**
     * Displays the results of a session
     */
    public void displayResult() {
        rightOrWrong.setText("");
        result.setText("SCORE: " + Integer.toString((totalProbs-numWrong)) + "/" + Integer.toString(totalProbs));
        playAgain.setVisibility(View.VISIBLE); quit.setVisibility(View.VISIBLE);
    }
}