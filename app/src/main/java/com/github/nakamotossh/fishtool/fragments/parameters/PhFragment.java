package com.github.nakamotossh.fishtool.fragments.parameters;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.text.format.DateFormat.getDateFormat;
import static com.github.nakamotossh.fishtool.adapters.ParamListAdapter.PH_PARAM;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;

public class PhFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PhFragment";
    private final int LOADER_ID = 0;

    private LineChart chart;
    private RecyclerView recyclerView;
    private ParamListAdapter adapter;

    public PhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ph, container, false);
        setHasOptionsMenu(true);

        Log.d(TAG, "onCreateView: ph id: " +
                (getArguments() != null ? getArguments().getInt("aquaId") : -1));

        //test();

        //Chart
        chart = rootView.findViewById(R.id.chart);

        //Adapter List
        adapter = new ParamListAdapter(getContext(), null, PH_PARAM);

        // Item List
        recyclerView = rootView.findViewById(R.id.recycler_param);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getLoaderManager().initLoader(LOADER_ID, null, this);

        return rootView;
    }

    private void test()  {

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        String date = d + "/" + m + "/" + y;
        Log.d(TAG, "test: date " + date);

        Date a = null;
        try {
            a = getDateFormat(getContext()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test: " + a);

        String date1 = "14/06/2018";
        Date b = null;
        try {
            b = getDateFormat(getContext()).parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "test: " + b);
    }

    private void initChart(Cursor cursor) {

        while (cursor.moveToNext()){

        }

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
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(),
                PARAM_CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        initChart(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onDetach() {
        setMenuVisibility(false);
        super.onDetach();
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
}
