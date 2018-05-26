package com.example.nakamoto.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.fragments.AquaFragment;
import com.example.nakamoto.fishtool.fragments.ParamFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.nakamoto.fishtool.debug.WakeUp.riseAndShine;

public class AquaInfo extends AppCompatActivity {

    /* Todo: Get Values and Set and set on layout using a AsyncTask/Loader */

    private static final String TAG = "AquaInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Debug purpose
         * */
        // TODO: Remove this before release
        riseAndShine(this);
        Log.d(TAG, "onCreate: started");
        setTitle("Neon");
        //startActivity(new Intent(this, AquaMain.class));
        /**
         */

        setContentView(R.layout.activity_aqua_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = findViewById(R.id.fab_aqua_info);

        AquaInfoViewPagerAdapter adapter = new AquaInfoViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AquaFragment(), "Aquarium");
        adapter.addFragment(new ParamFragment(), "Parameters");

        ViewPager viewPager = findViewById(R.id.viewpager_aqua_info);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.aqua_info_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                final int AQUA_TAB = 0;
                final int PARAM_TAB = 1;
                switch (tab.getPosition()){
                    case AQUA_TAB:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AquaInfo.this, "aqua", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case PARAM_TAB:
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(AquaInfo.this, "param", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private static class AquaInfoFragmentList {

        private String fragmentTitle;
        private Fragment fragment;

        public String getFragmentTitle() {
            return fragmentTitle;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragmentTitle(String fragmentTitle) {
            this.fragmentTitle = fragmentTitle;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public AquaInfoFragmentList(Fragment fragment, String fragmentTitle) {
            this.fragmentTitle = fragmentTitle;
            this.fragment = fragment;
        }
    }

    private static class AquaInfoViewPagerAdapter extends FragmentPagerAdapter {

        private final List<AquaInfoFragmentList> aquaInfoFragmentLists = new ArrayList<>();

        public AquaInfoViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            aquaInfoFragmentLists.add(new AquaInfoFragmentList(fragment, title));
        }

        @Override
        public Fragment getItem(int position) {
            return aquaInfoFragmentLists.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return aquaInfoFragmentLists.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return aquaInfoFragmentLists.get(position).getFragmentTitle();
        }
    }
}

