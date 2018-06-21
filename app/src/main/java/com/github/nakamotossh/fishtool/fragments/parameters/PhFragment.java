package com.github.nakamotossh.fishtool.fragments.parameters;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.activity.AddParam;
import com.github.nakamotossh.fishtool.adapters.ParamListAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.text.format.DateFormat.getDateFormat;
import static com.github.nakamotossh.fishtool.adapters.ParamListAdapter.PH_PARAM;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;

public class PhFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //TODO: Bottom navigation overflow recyclerView
    //TODO: Display loading while reading from db
    //TODO: Ripple effect on item list
    //TODO: fix bug on recyclerview hour
    //TODO: format value above cicle on chart 5,000 to 5,0 on first parameters
    //TODO: fix bug on variation list

    private static final String TAG = "PhFragment";
    private final int LOADER_ID = 0;

    private LineChart chart;
    private RecyclerView recyclerView;
    private ParamListAdapter adapter;

    public PhFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ph, container, false);
        setHasOptionsMenu(true);

        Log.d(TAG, "onCreateView: ph id: " +
                (getArguments() != null ? getArguments().getInt("aquaId") : -1));

        /* Setup Chart */
        chart = rootView.findViewById(R.id.chart);
        chart.setNoDataText("No data available yet");
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
        chart.setNoDataTextColor(R.color.colorPrimary);

        /* Adapter List */
        adapter = new ParamListAdapter(getContext(), null, PH_PARAM);

        /* Item List */
        recyclerView = rootView.findViewById(R.id.recycler_param);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* Starts the loader */
        getLoaderManager().initLoader(LOADER_ID, null, this);
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

    private void updateChart(Cursor cursor) {
        Log.d(TAG, "updateChart: ");
        List<Entry> entries = new ArrayList<>();

        /* Get N last param values */
        final int LIMIT_OF_ENTRIES = 3;
        if (cursor.moveToLast()){
            int i = 0;
            /* Get Values */
            long datesInMilliseconds = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_PARAM_COLUMN));
            float paramValue = cursor.getFloat(cursor.getColumnIndexOrThrow(PH_COLUMN));
            /* Set to chart */
            entries.add(new Entry(datesInMilliseconds, paramValue));
            i++;
            for (; i < LIMIT_OF_ENTRIES; i++) {
               if (cursor.moveToPrevious()){
                   /* Get Values */
                   long datesInMilliseconds1 = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_PARAM_COLUMN));
                   float paramValue1 = cursor.getFloat(cursor.getColumnIndexOrThrow(PH_COLUMN));
                   /* Set to chart */
                   entries.add(new Entry(datesInMilliseconds1, paramValue1));
               } else {
                   break;
               }
            }
        }

        /* Sort in Ascending */
        Collections.sort(entries, new EntryXComparator());

        /* Chart Line */
        LineDataSet dataSet = new LineDataSet(entries, "parameters");
        dataSet.setColor(Color.BLUE, 100);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleColorHole(Color.GREEN);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setHighLightColor(Color.RED);

        /* Horizontal Axis */
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(45.0f);
        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.LTGRAY);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long date = (long) value;
                return DateUtils.formatDateTime(getContext(), date, DateUtils.FORMAT_NUMERIC_DATE);
            }
        });

        /* Vertical Axis Left */
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.setGridColor(Color.LTGRAY);
        //yAxisLeft.setAxisMinimum(5.5f);
        //yAxisLeft.setAxisMaximum(9.6f);
        /* Axis Right */
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawLabels(false);

        /* Chart Data */
        LineData lineData = new LineData(dataSet);
        lineData.setValueTextSize(7.2f);

        Description description = new Description();
        description.setText("pH values based on date");
        chart.setDescription(description);

        chart.setHardwareAccelerationEnabled(true);
        chart.setBorderColor(Color.LTGRAY);
        chart.setDrawBorders(true);
        chart.setData(lineData);
        chart.invalidate();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String sortOrder = DATE_PARAM_COLUMN + " DESC";
        return new CursorLoader(Objects.requireNonNull(getContext()),
                PARAM_CONTENT_URI,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        /* Check if cursor is empty */
        if (cursor.getCount() > 0){
            updateChart(cursor);
        }
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
