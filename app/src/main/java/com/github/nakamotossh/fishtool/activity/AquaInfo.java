package com.github.nakamotossh.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.fragments.AquaFragment;
import com.github.nakamotossh.fishtool.fragments.FaunaFragment;
import com.github.nakamotossh.fishtool.fragments.ParamFragment;

import static com.github.nakamotossh.fishtool.debug.WakeUp.riseAndShine;

public class AquaInfo extends AppCompatActivity {

    private static final String TAG = "AquaInfo";
    private ParamFragment paramFragment;
    private Fragment aquaFragment;
    private FaunaFragment faunaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquainfo);

        /* TODO: remove before release */
        riseAndShine(this);

        /* Pass ID to fragments */
        boolean hasExtra = getIntent().hasExtra("aquaId");

        /* There are extras? */
        if (hasExtra) {
            /* Get ID and Convert to URI */
            int intentAquaId = getIntent()
                    .getExtras()
                    .getInt("aquaId");
            Log.d(TAG, "onCreate: id: " + intentAquaId);

            Bundle bundle = new Bundle();
            bundle.putInt("aquaId", intentAquaId);

            paramFragment = new ParamFragment();
            paramFragment.setArguments(bundle);

            aquaFragment = new AquaFragment();
            aquaFragment.setArguments(bundle);

            faunaFragment = new FaunaFragment();
            faunaFragment.setArguments(bundle);
        }

        /* Start Fragment */
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_fragment, aquaFragment)
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
                                .replace(R.id.frame_fragment, aquaFragment)
                                .commit();
                        return true;
                    case R.id.navigation_param:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragment, paramFragment)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.navigation_fauna:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_fragment, faunaFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });

    }
}
