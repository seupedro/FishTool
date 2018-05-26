package com.example.nakamoto.fishtool.fragments;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AquaFragment extends Fragment {

    //TODO: Read from Db Using AsyncTaskLoader
    //TODO: fix collapising bar

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

    public AquaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new AquaDbHelper(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_aquainfo, container, false);

        /* Find view on layout */
        aquaLiters = rootView.findViewById(R.id.aqua_info_liters);
        aquaSize = rootView.findViewById(R.id.aqua_info_size);
        aquaLight = rootView.findViewById(R.id.aqua_info_light);
        aquaFilter = rootView.findViewById(R.id.aqua_info_filter);
        aquaCo2 = rootView.findViewById(R.id.aqua_info_co2);
        aquaDose = rootView.findViewById(R.id.aqua_info_dosage);
        aquaSubstrate = rootView.findViewById(R.id.aqua_info_substrate);
        aquaType = rootView.findViewById(R.id.aqua_info_status);
        aquaStatus = rootView.findViewById(R.id.aqua_info_status);
        //aquaImage = rootView.findViewById(R.id.aqua_info_image);

        // Inflate the layout for this fragment
        return rootView;
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
        getActivity().setTitle(c.getString(c.getColumnIndexOrThrow(NAME_COLUMN)));
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

