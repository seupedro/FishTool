package com.github.nakamotossh.fishtool.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.extras.ParamUtils;

import java.util.Calendar;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;
import static com.github.nakamotossh.fishtool.debug.WakeUp.riseAndShine;
import static com.github.nakamotossh.fishtool.extras.ParamUtils.formatNumber;

public class AddParam extends AppCompatActivity {

    //TODO: notify updates to chart/recyclerview/db
    //TODO: format float params to 7.2

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
        riseAndShine(this);

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
                if (checkAndSaveParams())
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

        //test
//        test();
    }

    private void setNewDate() {
        /* Listener Date Set */
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                dateInMilliseconds = calendar.getTimeInMillis();
                /* Set on Layout */
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

    private boolean checkAndSaveParams(){
        ContentValues values = new ContentValues();

        /* Params Values */
        String ph = phEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(ph))
            values.put(PH_COLUMN, formatNumber(ph, ParamUtils.PH_PARAM));

        String temperature = tempEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(temperature))
            values.put(TEMP_COLUMN, formatNumber(temperature, ParamUtils.TEMP_PARAM));

        String ammonia = ammoniaEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(ammonia))
            values.put(NH3_COLUMN, formatNumber(ammonia, ParamUtils.AMMONIA_PARAM));

        /* Aqua ID */
        values.put(AQUA_FKEY, aquaIdExtra);

        /* Time Values */
        values.put(DATE_PARAM_COLUMN, dateInMilliseconds);

        Uri uriInserted = getContentResolver().insert(PARAM_CONTENT_URI, values);
        Toast.makeText(this, String.valueOf(uriInserted), Toast.LENGTH_SHORT).show();
        return true;
    }



    private void test() {

        ContentValues values = new ContentValues();

        phEdit.setText("567.899");

        Editable text = phEdit.getText();
        Log.d(TAG, "test: text " + text);

        String string = phEdit.getText().toString();
        Log.d(TAG, "test: string " + string);

        if (!TextUtils.isEmpty(string)) {
            Log.d(TAG, "test: NOT null");

            String s = formatNumber(string, ParamUtils.TEMP_PARAM);

            values.put(TEMP_COLUMN, s);

            /* Aqua ID */
            values.put(AQUA_FKEY, aquaIdExtra);

            /* Time Values */
            values.put(DATE_PARAM_COLUMN, dateInMilliseconds);

            Uri insert = getContentResolver().insert(PARAM_CONTENT_URI, values);
            Log.d(TAG, "test: uri "+ insert);
        } else {
            Log.d(TAG, "test: NULL");
        }
    }
}
