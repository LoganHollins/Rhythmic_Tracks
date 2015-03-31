package com.rhythmictracks.rhythmictracks;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import java.text.DecimalFormat;
import java.util.Set;
import java.util.Stack;


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
    public DecimalFormat dFormat;
    //LOCATION VARIABLES
    LocationManager locationManager;
    TextView topSpeed;
    TextView avgSpeed;
    TextView currentSpeed;
    TextView totalDistance;
    Location lastLoc;
    //Speed/distance variables
    float maxSpeed = -1; // initialized to negative so first speed always higher
    float totalDist = 0;
    double avg = 0.0;
    int amountSpeed = 0;


    private static final int FIVE_SECONDS = 1000 * 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise__page);
        timerValue = (TextView) findViewById(R.id.exercise_timer);
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        playButton = (Button) findViewById(R.id.play_button);
        //initialize all views that will be display while running
        topSpeed = (TextView) findViewById(R.id.topSpeed);
        avgSpeed = (TextView) findViewById(R.id.avgSpeed);
        currentSpeed = (TextView) findViewById(R.id.currentSpeed);
        totalDistance = (TextView) findViewById(R.id.totalDistance);
        createLocationListener();
    }

    public void savetoDB(View view){
        double finalavgspeed = 0;
        if(amountSpeed > 0) finalavgspeed = avg / amountSpeed;
        DatabaseHandler db = new DatabaseHandler(this);
        RunStats rs = new RunStats((double)maxSpeed, finalavgspeed, (double)totalDist, (double)updatedTime);

        db.addRunStats(rs);
        Intent intent = new Intent(this, RhythmicTracks.class);
        startActivity(intent);


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

    public void createLocationListener() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                calcLocation(SettingsPageFragment.showMS, location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }




    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {

        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > FIVE_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -FIVE_SECONDS;
        boolean isNewer = timeDelta > 0;

        // If it's been more than 5 seconds since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than 5 seconds, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void calcLocation(boolean metre, Location location) {
        float cSpeed = location.getSpeed();
        amountSpeed++;
        if (!metre) {
            //KM/H
            dFormat = new DecimalFormat("#0.00");
            cSpeed *= 3.6;
            avg += cSpeed;
            if (cSpeed > maxSpeed) {
                maxSpeed = cSpeed;
                topSpeed.setText(dFormat.format(maxSpeed) + " km/h");
            }
            currentSpeed.setText(dFormat.format(cSpeed) + " km/h");
            if (lastLoc != null) {
                float tempdist = lastLoc.distanceTo(location);
                totalDist += tempdist;
            }
            totalDistance.setText( dFormat.format(totalDist / 1000) + " km");
            lastLoc = location;
            avgSpeed.setText(dFormat.format(avg / amountSpeed) + " km/h");
        } else{
            //Metres a second
            dFormat = new DecimalFormat("#0.0");
            avg += cSpeed;
            if (cSpeed > maxSpeed) {
                maxSpeed = cSpeed;
                topSpeed.setText(dFormat.format(maxSpeed) + " m/s");
            }
            currentSpeed.setText(dFormat.format(cSpeed) + " m/s");
            if (lastLoc != null) {
                float tempdist = lastLoc.distanceTo(location);
                totalDist += tempdist;
            }
            totalDistance.setText(dFormat.format(totalDist) + " m");
            lastLoc = location;
            avgSpeed.setText(dFormat.format(avg / amountSpeed) + " m/s");
        }
    }
}



