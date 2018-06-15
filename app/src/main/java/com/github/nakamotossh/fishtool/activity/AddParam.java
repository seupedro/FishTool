package com.github.nakamotossh.fishtool.activity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;

import java.util.Calendar;
import java.util.Date;

import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.DATE_PARAM_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_URI;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.TEMP_COLUMN;

public class AddParam extends AppCompatActivity {

    private static final String TAG = "AddParam";

    private EditText dateEdit;
    private EditText timeEdit;
    private EditText phEdit;
    private EditText tempEdit;
    private EditText ammoniaEdit;
    private long paramTimeInMilli;

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

        /* Get and Set Current Date/Time */
        String currentDate = DateFormat.getDateFormat(this).format(new Date());
        dateEdit.setText(currentDate);
        String currentTime = DateFormat.getTimeFormat(this).format(new Date());
        timeEdit.setText(currentTime);
        /* Get time in milliseconds */
        Calendar c = Calendar.getInstance();
        paramTimeInMilli = c.getTimeInMillis();
        Log.d(TAG, "onCreate: " + paramTimeInMilli);
    }

    private void saveParams(){

        ContentValues values = new ContentValues();
        /* Time Values */
        values.put(DATE_PARAM_COLUMN, paramTimeInMilli);
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
