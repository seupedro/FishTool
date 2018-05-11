package com.example.nakamoto.fishtool.AsyncTask;

import android.database.Cursor;
import android.os.AsyncTask;

public class AquaCursorTask extends AsyncTask<Cursor, Cursor, Cursor> {

    //TODO:Implement Cursor AsyncTask

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Cursor doInBackground(Cursor... cursors) {
        return null;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
    }
}
