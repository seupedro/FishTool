package com.github.nakamotossh.fishtool.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;

import java.util.Calendar;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;

public class AddParam extends AppCompatActivity {

    //TODO: notify updates to chart/recyclerview/db

    private static final String TAG = "AddParam";

    private EditText dateEdit;
    private EditText timeEdit;
    private EditText phEdit;
    private EditText tempEdit;
    private EditText ammoniaEdit;
    private long dateInMilliseconds;
    private Calendar calendar;
    private int aquaIdExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_param);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendar = Calendar.getInstance();

        if (getIntent().hasExtra("aquaId")) {
            aquaIdExtra = getIntent().getExtras().getInt("aquaId");
        }

        /* Find views on Layout */
        timeEdit = findViewById(R.id.time_param);
        phEdit = findViewById(R.id.ph_param);
        tempEdit = findViewById(R.id.temperature_param);
        ammoniaEdit = findViewById(R.id.ammonia_param);
        dateEdit = findViewById(R.id.date_param);

        /* Save and Finish */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveParams();
                finish();
            }
        });

        /* Get and Set Current Date/Time */
        String currentDate = DateFormat.getDateFormat(this).format(calendar.getTime());
        dateEdit.setText(currentDate);
        String currentTime = DateFormat.getTimeFormat(this).format(calendar.getTime());
        timeEdit.setText(currentTime);

        /* Get time in milliseconds */
        dateInMilliseconds = calendar.getTimeInMillis();

        /* Set new Date */
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewDate();
            }
        });

        /* Set Hour */
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewHour();
            }
        });

    }

    private void setNewDate() {
        /* Listener Date Set */
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                dateInMilliseconds = calendar.getTimeInMillis();
                /* Set on Layout */
                Log.d(TAG, "onDateSet: " + DateFormat.getDateFormat(AddParam.this).format(calendar.getTime()));
                Log.d(TAG, "onDateSet: " + DateFormat.getTimeFormat(AddParam.this).format(calendar.getTime()));
                dateEdit.setText(DateFormat.getDateFormat(AddParam.this).format(calendar.getTime()));
                timeEdit.setText(DateFormat.getTimeFormat(AddParam.this).format(calendar.getTime()));
            }
        };

        /* Show Dialog */
        DatePickerDialog datePickerDialog = new DatePickerDialog(AddParam.this,
                dateListener,
                /* Fields for init picker */
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void setNewHour() {
        /* Listener */
        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                dateInMilliseconds = calendar.getTimeInMillis();
                timeEdit.setText(DateFormat.getTimeFormat(AddParam.this).format(calendar.getTime()));

                Log.d(TAG, "onTimeSet: AddParam.this " + DateFormat.getTimeFormat(AddParam.this).format(calendar.getTime()));
                Log.d(TAG, "onTimeSet: getTimeFormat(getApplicationContext " + DateFormat.getTimeFormat(getApplicationContext()).format(calendar.getTime()));
                Log.d(TAG, "onTimeSet: getTimeFormat(getBaseContext " + DateFormat.getTimeFormat(getBaseContext()).format(calendar.getTime()));
                Log.d(TAG, "onTimeSet: FORMAT_SHOW_TIME " + DateUtils.formatDateTime(getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
                Log.d(TAG, "onTimeSet: FORMAT_ABBREV_TIME " + DateUtils.formatDateTime(getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_TIME));
                Log.d(TAG, "onTimeSet: FORMAT | BOTH " + DateUtils.formatDateTime(getApplicationContext(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_ABBREV_TIME));


            }
        };

        /* Dialog */
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddParam.this,
                timeListener,
                /* Fields for init picker */
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getApplicationContext()));
        timePickerDialog.show();
    }

    private void saveParams(){

        ContentValues values = new ContentValues();
        /* Time Values */
        values.put(DATE_PARAM_COLUMN, dateInMilliseconds);
        /* Params Values */
        values.put(PH_COLUMN, phEdit.getText().toString().trim());
        values.put(TEMP_COLUMN, tempEdit.getText().toString().trim());
        values.put(NH3_COLUMN, ammoniaEdit.getText().toString().trim());
        /* Aqua ID */
        values.put(AQUA_FKEY, aquaIdExtra);

        Uri uriInserted = getContentResolver().insert(PARAM_CONTENT_URI, values);
        Toast.makeText(this, String.valueOf(uriInserted), Toast.LENGTH_SHORT).show();
    }
}
