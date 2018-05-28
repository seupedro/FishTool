package com.example.nakamoto.fishtool.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_CONTENT_ITEM_TYPE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_CONTENT_LIST_TYPE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.CONTENT_AUTORITY;
import static com.example.nakamoto.fishtool.database.AquaContract.PATH_AQUA;
import static com.example.nakamoto.fishtool.database.AquaContract.PATH_PARAM;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_ITEM_TYPE;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PARAM_CONTENT_LIST_TYPE;
import static com.example.nakamoto.fishtool.database.AquaContract.ParamEntry.PARAM_TABLE;

public class AquaProvider extends ContentProvider {

    private static final String TAG = "AquaProvider";

    /* Start Helper */
    private AquaDbHelper dbHelper;

    /* Constants for Uri Matcher */
    private static final int AQUA_LIST = 10;
    private static final int AQUA_ID = 11;
    private static final int PARAM_LIST = 20;
    private static final int PARAM_ID = 22;

    /* Starts an empty UriMatcher */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /* Aqua Uri */
        URI_MATCHER.addURI(CONTENT_AUTORITY, PATH_AQUA, AQUA_LIST);
        URI_MATCHER.addURI(CONTENT_AUTORITY, PATH_AQUA + "/#", AQUA_ID);
        /* Param Uri */
        URI_MATCHER.addURI(CONTENT_AUTORITY, PATH_PARAM, PARAM_LIST);
        URI_MATCHER.addURI(CONTENT_AUTORITY, PATH_PARAM + "/#", PARAM_ID);
    }

    @Override
    public boolean onCreate() {
        /* Starts database */
        dbHelper = new AquaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        /* Get db and cursor */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;

        /* Match Uri */
        int match = URI_MATCHER.match(uri);

        switch (match){
            case AQUA_LIST:
                /* Query complete db */
                cursor = db.query(AQUA_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case AQUA_ID:
                /* Setup Query to specific id */
                selection = _ID + "=?";
                /* Catch ID */
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(AQUA_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PARAM_LIST:
                cursor = db.query(PARAM_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case PARAM_ID:
                /* Prevents SQL Injection */
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PARAM_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unkown URI " + uri);

        }
        /* Notify changes */
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final  int match = URI_MATCHER.match(uri);
        switch (match) {
            case AQUA_ID:
                return AQUA_CONTENT_ITEM_TYPE;

            case AQUA_LIST:
                return AQUA_CONTENT_LIST_TYPE;

            case PARAM_ID:
                return PARAM_CONTENT_ITEM_TYPE;

            case PARAM_LIST:
                return PARAM_CONTENT_LIST_TYPE;

            default:
                throw new IllegalArgumentException("Unkwon URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        /* Matches URI */
        int match = URI_MATCHER.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (match){
            case AQUA_ID:
                /* Check if some fields are valid */
                String aquaName = values.getAsString(NAME_COLUMN);
                if (aquaName == null || aquaName.isEmpty()) {
                    throw new IllegalArgumentException("Name must have a name");
                }

                int aquaStatus = values.getAsInteger(STATUS_COLUMN);
                if (aquaStatus > 0 || aquaStatus < 2) {
                    throw new IllegalArgumentException("Status must be valid. Not acceptable values greater then 2 or lower than 0.");
                }

                int aquaType = values.getAsInteger(TYPE_COLUMN);
                if (aquaType > 0 || aquaType < 2) {
                    throw new IllegalArgumentException("Type must be valid. Not acceptable values greater then 2 or lower than 0.");
                }

                /* Insert Data */
                long aquaInsertId = db.insert(AQUA_TABLE, null, values);

                if (aquaInsertId == -1){
                    Log.e(TAG, "insert: failed on Uri: " + uri );
                    return null;
                }

                /* Notify Changes */
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, aquaInsertId);

            case PARAM_ID:
               //TODO: regex to match only params, not letters. It's very important for charts.

                /* Insert Data */
                long paramInsertId = db.insert(PARAM_TABLE, null, values);

                if (paramInsertId == -1){
                    Log.e(TAG, "insert: failed with uri " + uri );
                }
                /* Notify Changes */
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, paramInsertId);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        /* Get Database */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        /* Match the uri */
        int match = URI_MATCHER.match(uri);
        /* Track the rows removed */
        int rowsRemoved;

        switch (match){
            case AQUA_LIST:
                rowsRemoved = db.delete(AQUA_TABLE, selection, selectionArgs);
                break;

            case AQUA_ID:
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsRemoved = db.delete(AQUA_TABLE, selection, selectionArgs);
                break;

            case PARAM_LIST:
                rowsRemoved = db.delete(PARAM_TABLE, selection, selectionArgs);
                break;

            case PARAM_ID:
                selection = _ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsRemoved = db.delete(PARAM_TABLE, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for the following uri: " + uri);
        }

        if (rowsRemoved != 0) {
            /* Notify Changes */
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsRemoved;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        /* Handle Uri */
        final int match = URI_MATCHER.match(uri);

        /* Starts Db */
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (match) {
            case AQUA_ID:

                /* Return if there are any value */
                if (values.size() == 0){
                    rowsUpdated = 0;
                    return rowsUpdated;
                }

                /* Check name Column */
                if (values.containsKey(NAME_COLUMN)){
                    String aquaName = values.getAsString(NAME_COLUMN);
                    if (aquaName == null || aquaName.isEmpty()){
                        throw new IllegalArgumentException("Name must be valid.");
                    }
                }
                /* Check if status is valid */
                if (values.containsKey(STATUS_COLUMN)){
                    int aquaStatus = values.getAsInteger(STATUS_COLUMN);
                    if (aquaStatus < 0 || aquaStatus > 2) {
                        throw new IllegalArgumentException("Status must be valid. Not acceptable values greater then 2 or lower than 0.");
                    }
                }
                /* Check type column */
                if (values.containsKey(TYPE_COLUMN)){
                    int aquaType = values.getAsInteger(TYPE_COLUMN);
                    if (aquaType < 0 || aquaType > 2) {
                        throw new IllegalArgumentException("Type must be valid. Not acceptable values greater then 2 or lower than 0.");
                    }
                }

                /* Update rows on Db */
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(AQUA_TABLE, values, selection, selectionArgs);

                /* Notify Changes */
                if (rowsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;

            case PARAM_ID:
                // TODO: regex to verify valid inputs on params.

                /* Return if there are any value */
                if (values.size() == 0){
                    rowsUpdated = 0;
                    return rowsUpdated;
                }

                /* Update rows on Db */
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated = db.update(PARAM_TABLE, values, selection, selectionArgs);

                /* Notify Changes */
                if (rowsUpdated != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsUpdated;

            default:
                throw new IllegalArgumentException("Uptade is not supported for the following uri: " + uri);
        }
    }
}
