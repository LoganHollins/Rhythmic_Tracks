package com.rhythmictracks.rhythmictracks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
    public static final String LOGGING_TAG = "Start Music Player:";
    public static MediaPlayer player;

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

            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int hours = secs / 3600;
            secs = secs % 60;
            mins = mins % 60;
            int milliseconds = (int) (updatedTime % 1000);
            if (hours >= 1 && SettingsPageFragment.showmil) {
                timerValue.setText("Timer: " + hours + ":" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
            } else if (hours >= 1 && !SettingsPageFragment.showmil) {
                timerValue.setText("Timer: " + hours + ":" + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs));
            } else if (hours < 1 && SettingsPageFragment.showmil) {
                timerValue.setText("Timer: " + String.format("%02d", mins) + ":"
                        + String.format("%02d", secs) + ":"
                        + String.format("%03d", milliseconds));
            } else if (hours < 1 && !SettingsPageFragment.showmil) {
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

    public void startPlaylist(View view) {

        if(player == null) {
            playTrackFromPlaylist(MusicPageFragment.returnPlayListId());
            playButton.setText("Pause");
        }
         else if(player.isPlaying()){
            player.pause();
            playButton.setText("Play");
        } else {
            player.start();
            playButton.setText("Pause");
        }
    }



    public void playAudio(final String path) {
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        if (path == null) {
            Log.e(LOGGING_TAG, "Called playAudio with null data stream.");
            return;
        }
        try {
            player.setDataSource(path);
            player.prepare();
            player.start();
            Log.i(LOGGING_TAG, "Success in starting MediaPlayer");
        } catch (Exception e) {
            Log.e(LOGGING_TAG, "Failed to start MediaPlayer: " + e.getMessage());
        }
    }

    public void playTrackFromPlaylist(final long playListID) {
        final ContentResolver resolver = this.getContentResolver();
        final Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playListID);
        final String dataKey = MediaStore.Audio.Media.DATA;
        Cursor tracks = resolver.query(uri, new String[]{dataKey}, null, null, null);
        if (tracks != null) {
            tracks.moveToFirst();
            final int dataIndex = tracks.getColumnIndex(dataKey);
            final String dataPath = tracks.getString(dataIndex);
            playAudio(dataPath);
            tracks.close();
        }
    }
}

