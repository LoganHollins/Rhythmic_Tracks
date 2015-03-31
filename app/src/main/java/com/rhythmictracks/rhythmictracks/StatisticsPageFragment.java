package com.rhythmictracks.rhythmictracks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StatisticsPageFragment extends Fragment{
    TextView stats;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatisticsPageFragment newInstance(int sectionNumber) {
        StatisticsPageFragment fragment = new StatisticsPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public StatisticsPageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_statistics_page, container, false);
        stats = (TextView) rootView.findViewById(R.id.runstats);
        readfromDB();

        return rootView;
    }



    public void readfromDB() {

        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<RunStats> rsList = new ArrayList<RunStats>();
        rsList = db.getAllRunStats();
        for(RunStats rs : rsList){
            stats.append("\n id: " + rs.get_id() +
                         "\n max: " + rs.getRunMaxSpeed() +
                         "\n average: " + rs.getRunAvgSpeed() +
                         "\n distance: " + rs.getRunDistance() +
                         "\n time: " + rs.getRunTime() / 1000);
        }

    }
}
