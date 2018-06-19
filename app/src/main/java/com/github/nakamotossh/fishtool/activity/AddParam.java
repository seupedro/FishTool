package com.github.nakamotossh.fishtool.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_param);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                saveParams();
                finish();
            }
        });

        /* Find views on Layout */
        timeEdit = findViewById(R.id.time_param);
        phEdit = findViewById(R.id.ph_param);
        tempEdit = findViewById(R.id.temperature_param);
        ammoniaEdit = findViewById(R.id.ammonia_param);
        dateEdit = findViewById(R.id.date_param);

        /* Set Date */
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(AddParam.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                final int MATCH_MONTH = 1;
                                String dateSet = String.valueOf(dayOfMonth + "/" + (month + MATCH_MONTH) +  "/" +year);
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date date = dateFormat.parse(dateSet);
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    dateInMilliseconds = calendar.getTimeInMillis();
                                    /* Set on Layout */
                                    dateEdit.setText(DateFormat.getDateFormat(AddParam.this).format(calendar.getTime()));
                                    timeEdit.setText(DateFormat.getTimeFormat(AddParam.this).format(calendar.getTime()));
                                    calendar.clear();
                                    //Todo: Call timePicker here
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        /* Date for init picker */
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        /* Set Hour */
        timeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new TimePickerDialog(AddParam.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.HOUR, hourOfDay);
                        calendar1.set(Calendar.MINUTE, minute);
                        timeEdit.setText(DateFormat.getTimeFormat(AddParam.this).format(calendar1.getTime()));
                    }
                },
                 calendar.get(Calendar.HOUR),
                 calendar.get(Calendar.MINUTE),
                 DateFormat.is24HourFormat(AddParam.this))
                .show();
            }
        });

        /* Get and Set Current Date/Time */
        String currentDate = DateFormat.getDateFormat(this).format(new Date());
        dateEdit.setText(currentDate);
        String currentTime = DateFormat.getTimeFormat(this).format(new Date());
        timeEdit.setText(currentTime);
        /* Get time in milliseconds */
        Calendar c = Calendar.getInstance();
        dateInMilliseconds = c.getTimeInMillis();
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
        values.put(AQUA_FKEY, 1);

        Uri uriInserted = getContentResolver().insert(PARAM_CONTENT_URI, values);
        Toast.makeText(this, String.valueOf(uriInserted), Toast.LENGTH_SHORT).show();
    }
}
