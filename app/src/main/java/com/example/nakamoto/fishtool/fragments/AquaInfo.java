package com.example.nakamoto.fishtool.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nakamoto.fishtool.R;
import com.example.nakamoto.fishtool.database.AquaDbHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AquaInfo extends Fragment {

    private AquaDbHelper dbHelper;
    private TextView aquaLiters;
    private TextView aquaSize;
    private TextView aquaLight;
    private TextView aquaFilter;
    private TextView aquaCo2;
    private TextView aquaDose;
    private TextView aquaSubstrate;
    private TextView aquaType;
    private TextView aquaStatus;
    private TextView aquaDate;
    private TextView aquaNote;
    private ImageView aquaImage;

    public AquaInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View rootView = inflater.inflate(R.layout.fragment_aqua_info, container, false);
        
        /* Find view on layout */
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        aquaLiters = rootView.findViewById(R.id.aqua_info_liters);
        aquaSize = rootView.findViewById(R.id.aqua_info_size);
        aquaLight = rootView.findViewById(R.id.aqua_info_light);
        aquaFilter = rootView.findViewById(R.id.aqua_info_filter);
        aquaCo2 = rootView.findViewById(R.id.aqua_info_co2);
        aquaDose = rootView.findViewById(R.id.aqua_info_dosage);
        aquaSubstrate = rootView.findViewById(R.id.aqua_info_substrate);
        aquaType = rootView.findViewById(R.id.aqua_info_status);
        aquaStatus = rootView.findViewById(R.id.aqua_info_status);
        aquaImage = rootView.findViewById(R.id.aqua_info_image);

        // Inflate the layout for this fragment
        return rootView;
    }

}
