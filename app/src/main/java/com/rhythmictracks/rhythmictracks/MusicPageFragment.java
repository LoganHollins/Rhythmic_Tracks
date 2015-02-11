package com.rhythmictracks.rhythmictracks;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;

public class MusicPageFragment extends Fragment {
    public static String lowSong;
    static Spinner lowSongSelect;
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
    public static void songSelect(){
        lowSong = lowSongSelect.getSelectedItem().toString();
    }

    public MusicPageFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music_page, container, false);
        lowSongSelect = (Spinner) rootView.findViewById(R.id.lowSpeedSpinner);
        return rootView;
    }
}
