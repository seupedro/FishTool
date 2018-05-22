package com.example.nakamoto.fishtool.activity;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.nakamoto.fishtool.AquaListCursorAdapter;
import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;
import com.idescout.sql.SqlScoutServer;

import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.AQUA_TABLE;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.NAME_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.STATUS_COLUMN;
import static com.example.nakamoto.fishtool.database.AquaContract.AquaEntry.TYPE_COLUMN;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivity";
    private AquaDbHelper helper;
    private static SQLiteDatabase db;
    TextView textView;
    AquaListCursorAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new AquaDbHelper(this);
        db = helper.getWritableDatabase();

        /* Debug Db */
        SqlScoutServer.create(this, getPackageName());


        /* List and Adapter */
        recyclerView = findViewById(R.id.recycler);
        adapter = new AquaListCursorAdapter(this, null);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getLoaderManager().initLoader(0, null, this);
        Log.d(TAG, "onCreate: done");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    /* Fill db with dummy data to test */
        int i = 0;
        while (i < 10){

            ContentValues values = new ContentValues();
            values.put(NAME_COLUMN, String.valueOf(i));
            values.put(STATUS_COLUMN, 1);
            values.put(TYPE_COLUMN, 1);

            db.insert(AQUA_TABLE, null, values);
            values.clear();
            i++;
        }


        Log.d(TAG, "onCreateLoader: ");
        return new CustomCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        adapter.swapCursor(c);
        adapter = new AquaListCursorAdapter(this, c);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}