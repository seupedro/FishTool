package com.example.nakamoto.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.fragments.AquaFragment;
import com.example.nakamoto.fishtool.fragments.ParamFragment;

import static com.example.nakamoto.fishtool.debug.WakeUp.riseAndShine;

public class AquaInfo extends AppCompatActivity {

    /* Todo: Get Values and Set and set on layout using a AsyncTask/Loader */
    // TODO: Remove this before release

    private static final String TAG = "AquaInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquainfo);

        /**
         * Debug purpose
         * */
        riseAndShine(this);
        //startActivity(new Intent(this, AquaMain.class));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new ParamFragment()).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new AquaFragment()).commit();
                        return true;
                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.linear, new ParamFragment()).commit();
                        return true;
                }
                return false;
            }
        });
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        getSupportFragmentManager().beginTransaction().add(R.id.linear, new ParamFragment()).commit();


    }
}

