package com.mapure.amap.contest.ui.fagment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.widget.CustomDigitalClock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountDownFragment extends Fragment {

    private CustomDigitalClock timeClock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count_down, null);

        timeClock = (CustomDigitalClock) view.findViewById(R.id.time);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String txtDate = "11-29-2013";
        Date date = null;

        try {
            date = dateFormat.parse(txtDate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        timeClock.setEndTime(date.getTime());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
