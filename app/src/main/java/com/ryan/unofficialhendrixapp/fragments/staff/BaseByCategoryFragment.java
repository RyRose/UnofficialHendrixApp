package com.ryan.unofficialhendrixapp.fragments.staff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by ryan on 1/12/15.
 */
public abstract class BaseByCategoryFragment extends ListFragment {

    @Icicle int mPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSelection(mPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPosition = getListView().getFirstVisiblePosition();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.restoreInstanceState(this, outState);
    }
}