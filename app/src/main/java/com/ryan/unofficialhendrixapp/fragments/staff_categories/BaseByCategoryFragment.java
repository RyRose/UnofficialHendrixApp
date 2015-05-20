package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import icepick.Icepick;
import icepick.Icicle;

public abstract class BaseByCategoryFragment extends ListFragment {

    @Icicle int mPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
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
