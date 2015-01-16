package com.ryan.unofficialhendrixapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ryan.unofficialhendrixapp.R;

/**
 * Created by ryan on 1/12/15.
 */
public abstract class BaseNavDrawerFragment extends Fragment {
    protected static final String NAV_DRAWER_KEY = "name_pos";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int name_pos = getArguments().getInt(NAV_DRAWER_KEY);
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[name_pos]);
    }
}
