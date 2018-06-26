package com.github.nakamotossh.fishtool.fragments.parameters;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.nakamotossh.fishtool.extras.ParamUtils;
import com.github.nakamotossh.fishtool.fragments.BaseParameter;

public class TempFragment extends BaseParameter {

    public TempFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseFragment(getArguments(), ParamUtils.TEMP_PARAM);
    }
}
