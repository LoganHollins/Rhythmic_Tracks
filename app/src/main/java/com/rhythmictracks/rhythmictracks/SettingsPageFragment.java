package com.rhythmictracks.rhythmictracks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class SettingsPageFragment extends Fragment implements View.OnClickListener{
    public static boolean showmil = false;
    public static boolean showMS = false;
    CheckBox toggleMil;
    Button saveChange;
    CheckBox toggleMS;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SettingsPageFragment newInstance(int sectionNumber) {
        SettingsPageFragment fragment = new SettingsPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    public void onClick(View v){
        showmil = showMilchange();
        showMS = showMSchange();
        Toast.makeText(getActivity(), "Changes Saved", Toast.LENGTH_SHORT).show();
    }

    public boolean showMilchange(){
       return toggleMil.isChecked();
    }
    public boolean showMSchange() { return toggleMS.isChecked(); }



    public SettingsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings_page, container, false);
        toggleMil = (CheckBox) rootView.findViewById(R.id.ToggleMilSec);
        toggleMS = (CheckBox) rootView.findViewById(R.id.ToggleMS);
        saveChange = (Button) rootView.findViewById(R.id.savebutton);
        saveChange.setOnClickListener(this);
        return rootView;
    }
}