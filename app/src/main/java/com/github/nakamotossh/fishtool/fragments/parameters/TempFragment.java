package com.github.nakamotossh.fishtool.fragments.parameters;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nakamotossh.fishtool.R;

public class TempFragment extends Fragment {

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_temp, container, false);

        return rootView;
    }


}
