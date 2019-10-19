package org.frangoro.tictac;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String INITIAL_RESET_TIMER = "35";
    private static final String INITIAL_ALARM_TIMER = "30";

    private Chronometer chronometer;
    private Button startPauseButton;
    private EditText resetTimer;
    private EditText alarmTimer;
    private boolean running;
    private boolean timeOver;
    private long pauseOffset;

    MediaPlayer alarmSound;
    MediaPlayer resetSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmSound = MediaPlayer.create(MainActivity.this, R.raw.bubble);
        resetSound = MediaPlayer.create(MainActivity.this, R.raw.win95);

        chronometer = findViewById(R.id.chronometer);
        startPauseButton = findViewById(R.id.startPauseButton);
        resetTimer = findViewById(R.id.resetTimer);
        alarmTimer = findViewById(R.id.alarmTimer);
        resetTimer.setText(INITIAL_RESET_TIMER);
        alarmTimer.setText(INITIAL_ALARM_TIMER);

        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                resetChronometer(v);
            }
        });

        alarmTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                resetChronometer(v);
            }
        });

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (alarmTimer.getText() != null && !alarmTimer.getText().toString().isEmpty()
                        && (SystemClock.elapsedRealtime() - chronometer.getBase())
                        >= Long.parseLong(alarmTimer.getText().toString()) * 1000) {
                    if (!timeOver) {
                        Toast.makeText(MainActivity.this, "Time over!", Toast.LENGTH_LONG).show();
                        alarmSound.start();
                        timeOver = true;
                    }
                    if (resetTimer.getText() != null && !resetTimer.getText().toString().isEmpty()
                            && (SystemClock.elapsedRealtime() - chronometer.getBase())
                            >= Long.parseLong(resetTimer.getText().toString()) * 1000) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        resetSound.start();
                        Toast.makeText(MainActivity.this, "Again!", Toast.LENGTH_LONG).show();
                        timeOver = false;
                    }
                }
            }
        });

    }

    public void startPauseChronometer(View view) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            startPauseButton.setText(R.string.pause);
        } else {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            startPauseButton.setText(R.string.start);
        }

    }

    public void resetChronometer(View view) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
}
