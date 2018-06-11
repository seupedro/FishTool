package com.github.nakamotossh.fishtool.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nakamotossh.fishtool.R;
import com.github.nakamotossh.fishtool.fragments.parameters.AmmoniaFragment;
import com.github.nakamotossh.fishtool.fragments.parameters.PhFragment;
import com.github.nakamotossh.fishtool.fragments.parameters.TempFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParamFragment extends Fragment {

    //TODO: Deploy layout
    //TODO: Implement Charts

    public ParamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_aquaparam, container, false);

        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager());
        adapter.addFragment("pH", new PhFragment());
        adapter.addFragment("Temperature", new TempFragment());
        adapter.addFragment("Ammonia", new AmmoniaFragment());

        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
    }

    private class FragmentAdapter extends FragmentStatePagerAdapter {

        private List<FragmentItem> fragmentList;

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            fragmentList = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentList.get(position).getFragmentName();
        }

        public void addFragment(String name, Fragment fragment){
            fragmentList.add(new FragmentItem(name, fragment));
        }

        public void setFragmentName(int position, String name){
            fragmentList.get(position).setFragmentName(name);
        }
    }

    private class FragmentItem{

        private String fragmentName;
        private Fragment fragment;

        public FragmentItem(String fragmentName, Fragment fragment) {
            this.fragmentName = fragmentName;
            this.fragment = fragment;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragmentName(String fragmentName) {
            this.fragmentName = fragmentName;
        }
    }
}

