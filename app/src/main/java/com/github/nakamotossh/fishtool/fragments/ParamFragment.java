package com.github.nakamotossh.fishtool.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.nakamotossh.fishtool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParamFragment extends Fragment {

    private LineChart lineChart;

    //TODO: Deploy layout
    //TODO: Implement Charts

    public ParamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aquaparam, container, false);

        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(10, 1));
        entries.add(new Entry(11, 2));
        entries.add(new Entry(12, 3));

        LineDataSet dataSet = new LineDataSet(entries, "dummy");
        dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);

        lineChart = rootView.findViewById(R.id.chart);
        lineChart.setData(lineData);
        lineChart.invalidate();

        // Inflate the layout for this fragment
        return rootView;
    }

}
