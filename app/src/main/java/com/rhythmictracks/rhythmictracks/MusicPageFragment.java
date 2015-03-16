package com.rhythmictracks.rhythmictracks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;


public class MusicPageFragment extends Fragment {

    //Variables initialized
    static String lowSong;
    private static Spinner lowSongSelect;
    private static Spinner medSongSelect;
    private static Spinner highSongSelect;
    public static final String LOGGING_TAG = "MusicPlaylists";
    public static Cursor playLists;
    private ContentResolver resolver;
    private static Uri playListsUri;
    public static String playlistIdKey;
    private static String playlistNameKey;
    public static long playlistID;


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MusicPageFragment newInstance(int sectionNumber) {
        MusicPageFragment fragment = new MusicPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MusicPageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_music_page, container, false);

        lowSongSelect = (Spinner) rootView.findViewById(R.id.lowSpeedSpinner);
        medSongSelect = (Spinner) rootView.findViewById(R.id.medSpeedSpinner);
        highSongSelect = (Spinner) rootView.findViewById(R.id.highSpeedSpinner);

        //Set cursor
        setPlaylistCursor();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                playLists,
                new String[] {"Name"},
                new int[] {android.R.id.text1}, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        lowSongSelect.setAdapter(adapter);
        medSongSelect.setAdapter(adapter);
        highSongSelect.setAdapter(adapter);

        // If no playlists are found
        if (playLists == null) {
            Log.e(LOGGING_TAG, "Found no playlists.");
        } else {
        // Log a list of the playlists.
        Log.i(LOGGING_TAG, "Playlists:");
        String playListName;
        for (boolean hasItem = playLists.moveToFirst(); hasItem; hasItem = playLists.moveToNext()) {
                playListName = playLists.getString(playLists.getColumnIndex(playlistNameKey));
                Log.i(LOGGING_TAG, playListName);
            }
        }



        return rootView;
    }

    /**
     * Returns selected item from the spinner as a string
     */
    public static void songSelect(){
        Object o = lowSongSelect.getSelectedItem();
        if(o != null) {
            lowSong = o.toString();
        }

    }

    /**
     * Returns cursor to users play lists
     */
    public Cursor setPlaylistCursor() {
        resolver = getActivity().getContentResolver();
        playListsUri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
        playlistIdKey = MediaStore.Audio.Playlists._ID;
        playlistNameKey = MediaStore.Audio.Playlists.NAME;
        String[] columns = {playlistIdKey, playlistNameKey};
        playLists = resolver.query(playListsUri, columns, null, null, null);

        return playLists;
    }

    public static long returnPlayListId() {

        playLists.moveToPosition(lowSongSelect.getSelectedItemPosition());
        Log.i(LOGGING_TAG, String.valueOf(playLists.getColumnName(0)));
        playlistID = playLists.getLong(playLists.getColumnIndex(playlistIdKey));
        Log.i(LOGGING_TAG, String.valueOf(playlistID));
        return playlistID;
    }


}
