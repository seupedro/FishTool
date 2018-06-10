package com.github.nakamotossh.fishtool.fragments.parameters;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.nakamotossh.fishtool.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhFragment extends Fragment {

    private LineChart chart;

    public PhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ph, container, false);

        chart = rootView.findViewById(R.id.chart);

        initChart();

        return rootView;
    }

    private void initChart() {

        List<Entry> entries = new ArrayList<>();

        // x = horizontal/data
        // y = vertical/pH

        entries.add(new Entry(5, 6.1f));
        entries.add(new Entry(7, 6.3f));
        entries.add(new Entry(10, 8.4f));
        entries.add(new Entry(10, 7.9f));
        entries.add(new Entry(15, 6.4f));
        entries.add(new Entry(20, 6.8f));
        entries.add(new Entry(25, 7.0f));
        entries.add(new Entry(30, 7.7f));

        LineDataSet dataSet = new LineDataSet(entries, "parameters");
        dataSet.setColor(Color.BLUE, 100);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleColorHole(Color.GREEN);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setHighLightColor(Color.RED);

        /* Horizontal Axis */
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setLabelRotationAngle(45.0f);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.LTGRAY);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value + "/05");
            }
        });


        Description description = new Description();
        description.setText("pH values based on date");

        /* Vertical Axis Left */
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setGridColor(Color.LTGRAY);
        yAxisLeft.setAxisMinimum(5.5f);
        yAxisLeft.setAxisMaximum(9.6f);
        // Axis Right
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawLabels(false);

        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(7.2f);

        chart.setBorderColor(Color.LTGRAY);
        chart.setDrawBorders(true);
        chart.setData(lineData);
        chart.invalidate();
        chart.setDescription(description);
    }

}
