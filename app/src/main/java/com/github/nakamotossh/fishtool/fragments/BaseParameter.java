package com.github.nakamotossh.fishtool.fragments;

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
import com.github.nakamotossh.fishtool.extras.ParamUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry._paramID;

public abstract class BaseParameter extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //TODO: Fix 0/NULL param being showing on list/chart
    //TODO: Fix many decimal floats on variations / or better / format that
    private final String TAG = this.getClass().getSimpleName();

    private int aquaId;
    private LineChart chart;
    private ParamListAdapter adapter;
    private String label;
    private String[] projection;
    private String paramColumn;
    private int paramCode;

    public int getAquaId() {
        return aquaId;
    }

    public void initBaseFragment(Bundle args, int paramCode) {
        if (args == null)
            throw new NullPointerException("This fragment must be called passing an Id over arguments");

        /* Get Aquarium Id/ Param Code */
        this.aquaId = args.getInt("aquaId");
        this.paramCode = paramCode;
        
        /* Setup layout based on Param */
        switch (paramCode){
            case ParamUtils.PH_PARAM:
                label = "pH";
                paramColumn = PH_COLUMN;
                projection = new String[]{
                        _paramID,
                        PH_COLUMN,
                        DATE_PARAM_COLUMN,
                };
                break;
            case ParamUtils.AMMONIA_PARAM:
                label = "Ammonia";
                paramColumn = NH3_COLUMN;
                projection = new String[]{
                        _paramID,
                        NH3_COLUMN,
                        DATE_PARAM_COLUMN,
                };
                break;
            case ParamUtils.TEMP_PARAM:
                label = "Temperature";
                paramColumn = TEMP_COLUMN;
                projection = new String[]{
                        _paramID,
                        TEMP_COLUMN,
                        DATE_PARAM_COLUMN,
                };
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        /* Inflate layout */
        View view = inflater.inflate(R.layout.fragment_ph, container, false);

        /* Init Chart */
        chart = view.findViewById(R.id.chart);
        chart.setNoDataText("No data available yet");
        chart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
        chart.setNoDataTextColor(R.color.colorPrimary);
        chart.setHardwareAccelerationEnabled(true);

        /* Params Adapter / List */
        adapter = new ParamListAdapter(getContext(), null, paramCode);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_param);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /* Starts Loader */
        int LOADER_ID = 0;
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    private void updateChart(Cursor cursor) {
        if (chart == null)
            throw new NullPointerException("Chart cannot be null");

        List<Entry> entries = new ArrayList<>();
        final int LIMIT_OF_ENTRIES = 3;
        if (cursor.moveToFirst()){
            int i = 0;
            /* Get Values */
            long datesInMilliseconds = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_PARAM_COLUMN));
            float paramValue = cursor.getFloat(cursor.getColumnIndexOrThrow(paramColumn));
            /* Set to chart */
            entries.add(new Entry(datesInMilliseconds, paramValue));
            i++;
            while (i < LIMIT_OF_ENTRIES) {
                if (cursor.moveToNext()){
                    /* Get Values */
                    long datesInMilliseconds1 = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_PARAM_COLUMN));
                    float paramValue1 = cursor.getFloat(cursor.getColumnIndexOrThrow(paramColumn));
                    /* Set to chart */
                    entries.add(new Entry(datesInMilliseconds1, paramValue1));
                } else {
                    break;
                }
                i++;
            }
        }

        /* Sort in Ascending */
        Collections.sort(entries, new EntryXComparator());

        /* Chart Line */
        LineDataSet dataSet = new LineDataSet(entries, label);
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

        /* Chart Itselt */
        Description description = new Description();
        description.setText("Value by Date");
        chart.setDescription(description);
        chart.setBorderColor(Color.LTGRAY);
        chart.setDrawBorders(true);
        chart.setData(lineData);
        chart.invalidate();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String selection = AQUA_FKEY + " = ?";
        String[] selectionArgs = { String.valueOf(aquaId) };
        String sortOrder = DATE_PARAM_COLUMN + " DESC";
        return new CursorLoader(Objects.requireNonNull(getContext()),
                PARAM_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
        /* Check if cursor is empty */
        if (cursor.getCount() > 0) {
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
                startActivity(new Intent(getActivity(), AddParam.class)
                        .putExtra("aquaId", aquaId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
