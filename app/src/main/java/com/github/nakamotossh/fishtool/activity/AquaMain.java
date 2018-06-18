package com.github.nakamotossh.fishtool.activity;

import android.animation.LayoutTransition;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.adapters.AquaListCursorAdapter;
import com.github.nakamotossh.fishtool.database.AquaDbHelper;

import java.util.Calendar;

import static com.github.nakamotossh.fishtool.database.AquaContract.AquaEntry.AQUA_CONTENT_URI;
import static com.github.nakamotossh.fishtool.debug.WakeUp.riseAndShine;

public class AquaMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final int AQUA_LOADER = 0;
    private final int PARAM_LOADER = 1;
    private AquaDbHelper dbHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private AquaListCursorAdapter adapter;

    private static final String TAG = "AquaMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquamain);

        // TODO: Remove this before release
        riseAndShine(this);

        ConstraintLayout constraintLayout = findViewById(R.id.constraint_parent);
        constraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        /* Set ActionBar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAquaIntent = new Intent(AquaMain.this, AquaNew.class);
                startActivity(newAquaIntent);
            }
        });
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(AquaMain.this, AquaInfo.class);
                startActivity(intent);
                return false;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /* Initialize */
        adapter = new AquaListCursorAdapter(this, null);
        dbHelper = new AquaDbHelper(this);

        /* Setup Recycler */
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* Start Loader */
        getLoaderManager().initLoader(AQUA_LOADER, null, this);

        date();
    }

    private void date()  {

        String dateOfBirth = "26/02/1974";

        Calendar calendar = Calendar.getInstance();
        String date = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);

        String date1 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_ALL);

        String date2 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_MONTH);

        String date3 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_RELATIVE);

        String date4 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_TIME);

        String date5 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_ABBREV_WEEKDAY);

        String date6 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_NO_MIDNIGHT);

        String date7 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_NO_MONTH_DAY);

        String date8 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_NO_NOON);

        String date9 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_NO_YEAR);

        String date10 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_NUMERIC_DATE);

        String date11 = DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);

        Log.d(TAG, "date: " + date);
        Log.d(TAG, "date1: " + date1);
        Log.d(TAG, "date2: " + date2);
        Log.d(TAG, "date3: " + date3);
        Log.d(TAG, "date4: " + date4);
        Log.d(TAG, "date5: " + date5);
        Log.d(TAG, "date6: " + date6);
        Log.d(TAG, "date7: " + date7);
        Log.d(TAG, "date8: " + date8);
        Log.d(TAG, "date9: " + date9);
        Log.d(TAG, "date10: " + date10);
        Log.d(TAG, "date11: " + date11);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                AQUA_CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
