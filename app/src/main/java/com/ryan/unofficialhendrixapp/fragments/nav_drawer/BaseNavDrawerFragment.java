package com.ryan.unofficialhendrixapp.fragments.nav_drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ryan.unofficialhendrixapp.R;

public class BaseNavDrawerFragment extends Fragment {
    private static final String NAV_DRAWER_KEY = "name_pos";

    public static BaseNavDrawerFragment newInstance( BaseNavDrawerFragment fragment, int position) {
        Bundle args = new Bundle();
        args.putInt( NAV_DRAWER_KEY, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int name_pos = getArguments().getInt(NAV_DRAWER_KEY);
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[name_pos]);
    }

    public boolean canGoBack() { return false; }
    public void onBackPressed() {}
}
