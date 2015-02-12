package com.rhythmictracks.rhythmictracks;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


    public class Exercise_Page extends ActionBarActivity {
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    Button playButton;
    long updatedTime = 0L;
    MediaPlayer lowMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise__page);
        timerValue = (TextView) findViewById(R.id.exercise_timer);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        playButton = (Button) findViewById(R.id.play_button);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise__page, menu);
        return true;
    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000 );
            int mins = secs / 60;
            int hours = secs / 3600;
            secs = secs % 60;
            mins = mins % 60;
            int milliseconds = (int) (updatedTime % 1000);
            if(hours >= 1 && SettingsPageFragment.showmil) {
                timerValue.setText("Timer: " + hours + ":" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
            }
            else if (hours >= 1 && !SettingsPageFragment.showmil){
                timerValue.setText("Timer: "  + hours + ":" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
            }
            else if (hours < 1 && SettingsPageFragment.showmil){
                timerValue.setText("Timer: " + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
            }
            else if (hours < 1 && !SettingsPageFragment.showmil){
                timerValue.setText("Timer: " + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
            }
            customHandler.postDelayed(this, 0);
        }

    };

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
    public MediaPlayer findLowSong(){
        if(MusicPageFragment.lowSong.equals("Barbie")){
            return  MediaPlayer.create(this, R.raw.barbie);
        } else if(MusicPageFragment.lowSong.equals("Daft Punk")){
            return MediaPlayer.create(this, R.raw.daftpunk);
        } else if(MusicPageFragment.lowSong.equals("Kanye")){
            return MediaPlayer.create(this, R.raw.stronger);
        }
        return MediaPlayer.create(this, R.raw.stronger);
    }
@Override
public void onPause() {
    super.onPause();
    lowMusic.stop(); // Stops music when back button is pressed.  Can no longer layer songs ontop of each other
}
    public void playKanye(View view)
    {
        if(lowMusic == null) {
            lowMusic = findLowSong();
        }
        if(lowMusic.isPlaying()){
            lowMusic.pause();
            playButton.setText("Play");
        } else {
            lowMusic.start();
            playButton.setText("Pause");
        }
    }
}

