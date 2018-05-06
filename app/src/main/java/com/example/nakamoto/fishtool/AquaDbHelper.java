package com.example.nakamoto.fishtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AquaDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "aqua.db";

    // Aquarium Table
    public static final String TABLE_NAME_AQUA = "aquarium";
    public static final String _AquaID = "_id";
    public static final String NAME_AQUA = "name";
    public static final String DATE_AQUA = "date";
    public static final String AQUATYPE_AQUA = "type";
    public static final String STATUS_AQUA = "status";
    public static final String CO2_AQUA = "co2";
    public static final String DOSAGE_AQUA = "dosage";
    public static final String SUBSTRATE_AQUA = "substrate";
    public static final String NOTES_AQUA = "notes";

    // Parameter Table
    // TODO: insert all tables
    public static final String TABLE_NAME_PARAM = "parameters";
    public static final String _paramID = "_id";
    public static final String PH_PARAM = "ph";
    public static final String NH3_PARAM = "nh3";
    public static final String DATE_PARAM = "date";
    public static final String AQUA_FKEY = "aqua_fkey";

    // Create Aqua Table
    public static final String SQL_CREATE_AQUA =
            "CREATE TABLE " + TABLE_NAME_AQUA + " (" +
                    _AquaID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_AQUA + " TEXT NOT NULL, " +
                    DATE_AQUA + " TEXT NOT NULL, " +
                    AQUATYPE_AQUA + " INTEGER NOT NULL, " +
                    STATUS_AQUA + " INTEGER NOT NULL,  " +
                    CO2_AQUA + " TEXT, " +
                    DOSAGE_AQUA + " TEXT, " +
                    SUBSTRATE_AQUA + " TEXT, " +
                    NOTES_AQUA + " TEXT " + ")";

    // Create Param Table
    public static final String SQL_CREATE_PARAM =
            "CREATE TABLE " + TABLE_NAME_PARAM + " (" +
                    _paramID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PH_PARAM + " INTEGER, " +
                    NH3_PARAM + " INTEGER, " +
                    DATE_PARAM + " TEXT NOT NULL, " +
                    AQUA_FKEY +  " INTEGER REFERENCES " + TABLE_NAME_AQUA + " (_id) " + ")";

    // SQL Delete Aqua
    public static final String SQL_DELETE_AQUA =
            "DROP TABLE IF EXISTS " + TABLE_NAME_AQUA;

    // SQL Delete Param
    public static final String SQL_DELETE_PARAM =
            "DROP TABLE IF EXISTS " + TABLE_NAME_PARAM;


    public AquaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AQUA);
        db.execSQL(SQL_CREATE_PARAM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PARAM);
        db.execSQL(SQL_DELETE_AQUA);
        onCreate(db);
    }
}
