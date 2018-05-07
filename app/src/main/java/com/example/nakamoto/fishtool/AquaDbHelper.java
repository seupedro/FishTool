package com.example.nakamoto.fishtool;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.nakamoto.fishtool.AquaContract.AquaEntry.*;
import static com.example.nakamoto.fishtool.AquaContract.ParamEntry.*;

public class AquaDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "aqua.db";

    // Create Aqua Table
    public static final String SQL_CREATE_AQUA =
            "CREATE TABLE " + AQUA_TABLE + " (" +
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
                    AQUA_FKEY +  " INTEGER NOT NULL REFERENCES " + AQUA_TABLE + " (_id) " + ")";

    // SQL Delete Aqua
    public static final String SQL_DELETE_AQUA =
            "DROP TABLE IF EXISTS " + AQUA_TABLE;

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
