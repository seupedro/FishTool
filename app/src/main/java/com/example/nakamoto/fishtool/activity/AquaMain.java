package com.example.nakamoto.fishtool.activity;

import android.animation.LayoutTransition;
import android.app.LoaderManager;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.adapters.AquaListCursorAdapter;
import com.example.nakamoto.fishtool.database.AquaDbHelper;
import com.example.nakamoto.fishtool.loader.CustomCursorLoader;

import static com.example.nakamoto.fishtool.debug.WakeUp.riseAndShine;

public class AquaMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID = 0;
    private AquaDbHelper dbHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private AquaListCursorAdapter cursorAdapter;
    private CardView cardView;

    private static final String TAG = "AquaMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquamain);

        /**
         * Debug purpose
         * */
        // TODO: Remove this before release
        riseAndShine(this);
        Log.d(TAG, "onCreate: started");
        startActivity(new Intent(this, AquaInfo.class).putExtra("id", 12));
        /**
         */

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
        cursorAdapter = new AquaListCursorAdapter(this, null);
        dbHelper = new AquaDbHelper(this);

        /* Setup Recycler */
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(cursorAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /* Start Loader */
        getLoaderManager().initLoader(LOADER_ID, null, this);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        db = dbHelper.getWritableDatabase();

        /* Create dummy data to test */
//        int i = 0;
//        while (i < 5){
//
//            ContentValues values = new ContentValues();
//            values.put(NAME_COLUMN, String.valueOf(i));
//            values.put(STATUS_COLUMN, 1);
//            values.put(TYPE_COLUMN, 1);
//
//            db.insert(AQUA_TABLE, null, values);
//            values.clear();
//            i++;
//        }
        return new CustomCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
        cursorAdapter = new AquaListCursorAdapter(this, cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
