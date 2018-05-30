package com.github.nakamotossh.fishtool.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nakamotossh.fishtool.R;

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

        View rootView = inflater.inflate(R.layout.fragment_aquaparam, container, false);
        TextView textView = rootView.findViewById(R.id.text_param);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "asd", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
