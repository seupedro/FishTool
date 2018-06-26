package com.github.nakamotossh.fishtool.fragments.parameters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.nakamotossh.fishtool.extras.ParamUtils;
import com.github.nakamotossh.fishtool.fragments.BaseParameter;

public class SalinityFragment extends BaseParameter {

    private static final String TAG = "SalinityFragment";

    public SalinityFragment(){
        //Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseFragment(getArguments(), ParamUtils.PH_PARAM);
        Log.d(TAG, "onCreate: " + getAquaId());
    }
}
