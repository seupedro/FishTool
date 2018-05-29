package com.example.nakamoto.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.fragments.AquaFragment;
import com.example.nakamoto.fishtool.fragments.FaunaFragment;
import com.example.nakamoto.fishtool.fragments.ParamFragment;

import static com.example.nakamoto.fishtool.debug.WakeUp.riseAndShine;

public class AquaInfo extends AppCompatActivity {

    private static final String TAG = "AquaInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquainfo);

        /* TODO: remove before release */
        riseAndShine(this);

        /* Start Fragment */
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_fragment, new AquaFragment())
                .commit();

        /* TODO: Remove before release */
        setTitle(getIntent().hasExtra("name") ? getIntent().getStringExtra("name") : getTitle());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_aqua:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragment, new AquaFragment())
                                .commit();
                        return true;
                    case R.id.navigation_param:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragment, new ParamFragment())
                                .commit();
                        return true;
                    case R.id.navigation_fauna:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragment, new FaunaFragment())
                                .commit();
                        return true;
                }
                return false;
            }
        });

    }

}
