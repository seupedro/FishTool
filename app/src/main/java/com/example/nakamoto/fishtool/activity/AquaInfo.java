package com.example.nakamoto.fishtool.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.CO2_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.DOSAGE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.FILTER_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.IMAGE_URI_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.LITERS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.SIZE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.SUBSTRATE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.AQUA_FKEY;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.NH3_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PARAM_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PH_COLUMN;

public class AquaInfo extends AppCompatActivity {

    /* Todo: Get Values and Set and set on layout using a AsyncTask */

    private static final String TAG = "AquaInfo";

    private AquaDbHelper dbHelper;
    private TextView aquaLiters;
    private TextView aquaSize;
    private TextView aquaLight;
    private TextView aquaFilter;
    private TextView aquaCo2;
    private TextView aquaDose;
    private TextView aquaSubstrate;
    private TextView aquaType;
    private TextView aquaStatus;
    private TextView aquaDate;
    private TextView aquaNote;
    private ImageView aquaImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Find view on layout */
        FloatingActionButton fab = findViewById(R.id.fab);
        aquaLiters = findViewById(R.id.aqua_info_liters);
        aquaSize = findViewById(R.id.aqua_info_size);
        aquaLight = findViewById(R.id.aqua_info_light);
        aquaFilter = findViewById(R.id.aqua_info_filter);
        aquaCo2 = findViewById(R.id.aqua_info_co2);
        aquaDose = findViewById(R.id.aqua_info_dosage);
        aquaSubstrate = findViewById(R.id.aqua_info_substrate);
        aquaType = findViewById(R.id.aqua_info_status);
        aquaStatus = findViewById(R.id.aqua_info_status);
        aquaImage = findViewById(R.id.aqua_info_image);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

//        displayValues(readData());
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

    private Cursor readData(){
        /* Get database */
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        /* Choose where retrive data */
        Cursor cursor = db.query(
                AQUA_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return cursor;
    }

    private void displayValues(Cursor c){
        c.moveToFirst();
        this.setTitle(c.getString(c.getColumnIndexOrThrow(NAME_COLUMN)));
        aquaImage.setImageURI(Uri.parse(c.getString(c.getColumnIndexOrThrow(IMAGE_URI_COLUMN))));
        aquaLiters.setText(c.getString(c.getColumnIndexOrThrow(LITERS_COLUMN)));
        aquaSize.setText(c.getString(c.getColumnIndexOrThrow(SIZE_COLUMN)));
        aquaLight.setText(c.getString(c.getColumnIndexOrThrow(LITERS_COLUMN)));
        aquaFilter.setText(c.getString(c.getColumnIndexOrThrow(FILTER_COLUMN)));
        aquaCo2.setText(c.getString(c.getColumnIndexOrThrow(CO2_COLUMN)));
        aquaDose.setText(c.getString(c.getColumnIndexOrThrow(DOSAGE_COLUMN)));
        aquaSubstrate.setText(c.getString(c.getColumnIndexOrThrow(SUBSTRATE_COLUMN)));
        aquaType.setText(c.getString(c.getColumnIndexOrThrow(TYPE_COLUMN)));
        aquaStatus.setText(c.getString(c.getColumnIndexOrThrow(STATUS_COLUMN)));
    }
}
