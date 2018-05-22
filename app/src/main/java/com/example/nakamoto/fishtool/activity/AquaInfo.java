package com.example.nakamoto.fishtool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.fragments.AquaFragment;
import com.example.nakamoto.fishtool.fragments.ParamFragment;

import java.util.ArrayList;
import java.util.List;

public class AquaInfo extends AppCompatActivity {

    /* Todo: Get Values and Set and set on layout using a AsyncTask */

    private static final String TAG = "AquaInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab_aqua_info);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
}

class FragmentList {

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

    public FragmentList(Fragment fragment, String fragmentTitle) {
        this.fragmentTitle = fragmentTitle;
        this.fragment = fragment;
    }
}

class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<FragmentList> fragmentLists = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title){
        fragmentLists.add(new FragmentList(fragment, title));
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLists.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentLists.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentLists.get(position).getFragmentTitle();
    }
}
