package com.github.nakamotossh.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquainfo);
        riseAndShine(this);

        /* Check if activity was called directly */
        if (getIntent().getExtras() == null)
            throw new RuntimeException(this.getLocalClassName() + " must be only called from an aquarium");

        /* Get Extras: ID */
        int intentAquaId = getIntent().getExtras().getInt("aquaId");

        /* Check Runtime Invalid Id */
        if (intentAquaId == 0)
            throw new IllegalArgumentException(getLocalClassName() + " has an invalid/null id. " +
                    "Id is always greater than 0.");

        /* Title in action bar */
        setTitle(getIntent().getStringExtra("aquaName"));

        /* Pass ID to fragments */
        Bundle bundle = new Bundle();

        aquaFragment = new AquaFragment();
        aquaFragment.setArguments(bundle);

        paramFragment = new ParamFragment();
        paramFragment.setArguments(bundle);

        faunaFragment = new FaunaFragment();
        faunaFragment.setArguments(bundle);

        /* Start First Fragment */
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (savedInstanceState == null)
            fragmentTransaction.replace(R.id.frame_fragment, aquaFragment).commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_aqua:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, aquaFragment).commit();
                        return true;
                    case R.id.navigation_param:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, paramFragment)/*.commit();*/
                                .addToBackStack(null).commit();
                        return true;
                    case R.id.navigation_fauna:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, faunaFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("navigationBottomPosition", navigation.getSelectedItemId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigation.setSelectedItemId(savedInstanceState.getInt("navigationBottomPosition"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
