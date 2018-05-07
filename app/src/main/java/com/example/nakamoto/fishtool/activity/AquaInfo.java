package com.example.nakamoto.fishtool.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;

import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PARAM_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;

public class AquaInfo extends AppCompatActivity {

    private static final String TAG = "AquaInfo";

    private AquaDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                Snackbar.make(view, "Done", Snackbar.LENGTH_LONG)
                        .setDuration(100)
                        .show();
            }
        });

        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                readData();
                Snackbar.make(view, "Done", Snackbar.LENGTH_LONG)
                        .setDuration(1000)
                        .show();
                return false;
            }
        });

        dbHelper = new AquaDbHelper(this);
    }

    private void insertData(){
        /* Get Db object */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        /* Prepare data values */
        ContentValues values = new ContentValues();
        values.put(PH_COLUMN, 7);
        values.put(NH3_COLUMN, 0);
        values.put(AQUA_FKEY, 1);
        /* Insert into db */
        db.insert(PARAM_TABLE, null, values);
    }

    private void readData(){
        /* Get database */
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /* Choose where retrive data */
        Cursor cursor = db.query(
                PARAM_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        /* Display data on log */
        Log.d(TAG, "readData: " +
                "pH: " + cursor.getString(cursor.getColumnIndex(PH_COLUMN)) +
                " NH3: " + cursor.getString(cursor.getColumnIndex(NH3_COLUMN)) +
                " AquaFKey is " + cursor.getString(cursor.getColumnIndex(AQUA_FKEY))
        );
    }
}
