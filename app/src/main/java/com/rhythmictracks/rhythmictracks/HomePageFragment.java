package com.rhythmictracks.rhythmictracks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HomePageFragment extends Fragment implements View.OnClickListener{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    Button button;
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HomePageFragment newInstance(int sectionNumber) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HomePageFragment() {
    }
 View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        Button upButton = (Button) rootView.findViewById(R.id.start_button);
        upButton.setOnClickListener(this);

        return rootView;

    }


    public void onClick(View arg0) {
        Intent intent = new Intent(getActivity(), Exercise_Page.class);
        startActivity(intent);
    }
}
