package com.github.nakamotossh.fishtool.fragments.parameters;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.github.nakamotossh.fishtool.activity.AddParam;
import com.github.nakamotossh.fishtool.adapters.ParamListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhFragment extends Fragment implements LoaderManager.LoaderCallbacks<CursorLoader> {

    private LineChart chart;
    private RecyclerView recyclerView;
    private ParamListAdapter adapter;

    public PhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ph, container, false);

        //Chart
        chart = rootView.findViewById(R.id.chart);
        initChart();

        //Adapter List
        adapter = new ParamListAdapter(getContext(), null);

        // Item List
//        recyclerView = recyclerView.findViewById(R.id.recycler_param);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.params_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_param:
                startActivity(new Intent(getActivity(), AddParam.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @NonNull
    @Override
    public Loader<CursorLoader> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<CursorLoader> loader, CursorLoader data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<CursorLoader> loader) {

    }
}
