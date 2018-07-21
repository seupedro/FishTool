package com.github.nakamotossh.fishtool.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.*;
import static com.github.nakamotossh.fishtool.database.AquaContract.ParamEntry.*;

public class AquaDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 23;
    public static final String DB_NAME = "aqua.db";

    // Create Aqua Table
    public static final String SQL_CREATE_AQUA =
            "CREATE TABLE " + AQUA_TABLE + " (" +
                    _aquaID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NAME_COLUMN + " TEXT NOT NULL, " +
                    LITERS_COLUMN + " INTEGER, " +
                    DATE_AQUA_COLUMN + " INTEGER NOT NULL, " +
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
                    PH_COLUMN + " REAL, " +
                    NH3_COLUMN + " REAL, " +
//                    NO3_COLUMN + " REAL, " +
//                    SALT_COLUMN + " REAL, " +
//                    MAGNESIUM_COLUMN + " REAL, " +
//                    CALCIUM_COLUMN + " REAL, " +
//                    ORP_COLUMN + " REAL, " +
//                    TDS_COLUMN + " REAL, " +
//                    PHOSPHATE_COLUMN + " REAL, " +
                    TEMP_COLUMN + " REAL, " +
//                    ALKALINITY_COLUMN + " REAL, " +
                    DATE_PARAM_COLUMN + " INTEGER NOT NULL, " +
                    AQUA_FKEY +  " INTEGER NOT NULL REFERENCES " + AQUA_TABLE + " (_id) " + " ON DELETE CASCADE " + ")";

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

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
