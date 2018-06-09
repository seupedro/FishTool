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

    //TODO: Deploy layout
    //TODO: Implement Charts

    public ParamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aquaparam, container, false);

        LineChart chart = rootView.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        // y = altura,  x = comprimento, linha
        // y  = ph      x = data

        entries.add(new Entry(5, 6.1f));
        entries.add(new Entry(7, 6.3f));
        entries.add(new Entry(10, 7.9f));
        entries.add(new Entry(15, 6.4f));
        entries.add(new Entry(20, 6.8f));
        entries.add(new Entry(25, 7.0f));
        entries.add(new Entry(30, 7.7f));

        LineDataSet dataSet = new LineDataSet(entries, "parameters");
        dataSet.setColor(Color.RED);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleColorHole(Color.BLACK);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setHighLightColor(Color.MAGENTA);
        dataSet.setFillColor(Color.MAGENTA);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        // Inflate the layout for this fragment
        return rootView;
    }

}
