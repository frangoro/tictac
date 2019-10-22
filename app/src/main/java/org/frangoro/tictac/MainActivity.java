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

    private static final String INITIAL_RESET_TIMER = "0";
    private static final String INITIAL_ALARM_TIMER = "3";

    private Chronometer chronometer;
    private Button startPauseButton;
    private EditText resetTimer;
    private EditText alarmTimer;
    private boolean running;
    private long pauseOffset;
    private Long alarmTimerSeconds;
    private Long resetTimerSeconds;
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

        // Initialize
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

        // Reset program
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Long elapsedSeconds = Long.valueOf(Math.round((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000));
                alarmTimerSeconds = alarmTimer.getText() != null && !alarmTimer.getText().toString().isEmpty() ? (Long.parseLong(alarmTimer.getText().toString())) : 0;
                resetTimerSeconds = alarmTimer.getText() != null && !resetTimer.getText().toString().isEmpty() ? (Long.parseLong(resetTimer.getText().toString())) : 0;
                if (elapsedSeconds != 0 && alarmTimerSeconds != 0 && elapsedSeconds % alarmTimerSeconds == 0) {
                    Toast.makeText(MainActivity.this, "Time over!", Toast.LENGTH_LONG).show();
                    alarmSound.start();
                }
                if (elapsedSeconds != 0 && resetTimerSeconds != 0 && elapsedSeconds % resetTimerSeconds == 0) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(MainActivity.this, "Again!", Toast.LENGTH_LONG).show();
                    resetSound.start();
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
