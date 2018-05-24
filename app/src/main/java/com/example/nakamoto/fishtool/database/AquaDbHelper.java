package com.example.nakamoto.fishtool.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.*;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.*;

public class AquaDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 11;
    public static final String DB_NAME = "aqua.db";

    // Create Aqua Table
    public static final String SQL_CREATE_AQUA =
            "CREATE TABLE " + AQUA_TABLE + " (" +
                    _aquaID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    LITERS_COLUMN + " INTEGER, " +
                    DATE_AQUA_COLUMN + " TEXT, " +
                    TYPE_COLUMN + " INTEGER NOT NULL, " +
                    STATUS_COLUMN + " INTEGER NOT NULL,  " +
                    LIGHT_COLUMN + " TEXT, " +
                    CO2_COLUMN + " TEXT, " +
                    DOSAGE_COLUMN + " TEXT, " +
                    SUBSTRATE_COLUMN + " TEXT, " +
                    NOTES_COLUMN + " TEXT, " +
                    SIZE_COLUMN + " TEXT, " +
                    FILTER_COLUMN + " TEXT, " +
                    IMAGE_URI_COLUMN + " TEXT " + ")";

    // Create Param Table
    public static final String SQL_CREATE_PARAM =
            "CREATE TABLE " + PARAM_TABLE + " (" +
                    _paramID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PH_COLUMN + " INTEGER, " +
                    NH3_COLUMN + " INTEGER, " +
                    DATE_PARAM_COLUMN + " TEXT, " +
                    AQUA_FKEY +  " INTEGER NOT NULL REFERENCES " + AQUA_TABLE + " (_id) " + ")";

    // SQL Delete Aqua
    public static final String SQL_DELETE_AQUA =
            "DROP TABLE IF EXISTS " + AQUA_TABLE;

    // SQL Delete Param
    public static final String SQL_DELETE_PARAM =
            "DROP TABLE IF EXISTS " + PARAM_TABLE;


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
