package com.example.nakamoto.fishtool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;

import java.util.Calendar;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;

public class AquaNew extends AppCompatActivity {

    private static final String TAG = "NEWAQUA";

    private ImageView aquaImage;
    private EditText aquaName;
    private static EditText aquaDate;
    private Spinner aquaType;
    private Spinner aquaStatus;
    private EditText aquaLiters;
    private EditText aquaLight;
    private EditText aquaCo2;
    private EditText aquaDose;
    private EditText aquaSubstrate;
    private FloatingActionButton fab;
    private AquaDbHelper dbHelper;

    private boolean onChanged = false;
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            onChanged = true;
            return false;
        }
    };

    private void readFromDb() {
        /* Get database */
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /* Define data retrive */
        Cursor cursor = db.query(
                AQUA_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        aquaName.setText(cursor.getString(cursor.getColumnIndex(NAME_COLUMN)));
        aquaType.setSelection(cursor.getInt(cursor.getColumnIndex(TYPE_COLUMN)));
        aquaStatus.setSelection(cursor.getInt(cursor.getColumnIndex(STATUS_COLUMN)), true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_new);

        /* Find views on layout */
        aquaImage = findViewById(R.id.aqua_photo);
        aquaName = findViewById(R.id.aqua_name);
        aquaType = findViewById(R.id.aqua_type);
        aquaStatus = findViewById(R.id.aqua_status);
        aquaLiters = findViewById(R.id.aqua_liters);
        aquaLight = findViewById(R.id.aqua_light);
        aquaCo2 = findViewById(R.id.aqua_co2);
        aquaSubstrate = findViewById(R.id.aqua_substrate);
        aquaDose = findViewById(R.id.aqua_dose);
        fab = findViewById(R.id.fab);
        aquaDate = findViewById(R.id.aqua_date);

        /* Track changes */
        aquaImage.setOnTouchListener(touchListener);
        aquaName.setOnTouchListener(touchListener);
        aquaType.setOnTouchListener(touchListener);
        aquaStatus.setOnTouchListener(touchListener);
        aquaLiters.setOnTouchListener(touchListener);
        aquaLight.setOnTouchListener(touchListener);
        aquaCo2.setOnTouchListener(touchListener);
        aquaSubstrate.setOnTouchListener(touchListener);
        aquaDose.setOnTouchListener(touchListener);
        aquaDate.setOnTouchListener(touchListener);

        /* Open DB Connection */
        dbHelper = new AquaDbHelper(this);

        /* Set listener on fab */

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Catch Image from gallery
                readFromDb();
            }
        });

        aquaDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch: ");
                    showDatePickerDialog(v);
                    //Todo: Fix dual datepicker dialog
                    //Todo: Check perfomClick Lint
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_aqua, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveValues();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (onChanged){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to discard this aquarium?");
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AquaNew.super.onBackPressed();
                }
            });
            builder.create().show();
        }
        super.onBackPressed();
    }

    private void saveValues() {
        /* Get Database */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
         /* Insert data */
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, aquaName.getText().toString().trim());
        values.put(STATUS_COLUMN, aquaStatus.getSelectedItemId());
        values.put(TYPE_COLUMN, aquaType.getSelectedItemId());
        /* Insert into Db */
        db.insert(AQUA_TABLE, null, values);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        Log.d(TAG, "showDatePickerDialog: ");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            Log.d(TAG, "onCreateDialog: ");
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            aquaDate.setText(day + "/" + month + "/" + year);
            Log.d(TAG, "onDateSet: ");
        }
    }
}
