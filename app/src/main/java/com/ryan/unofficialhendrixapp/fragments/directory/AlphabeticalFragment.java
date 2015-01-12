package com.ryan.unofficialhendrixapp.fragments.directory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;

import icepick.Icepick;
import icepick.Icicle;

public class AlphabeticalFragment extends ListFragment {

    @Icicle int mPosition;

    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static AlphabeticalFragment newInstance() {
        AlphabeticalFragment fragment = new AlphabeticalFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.fragment_dir_dept_item, LETTERS));
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
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String letter = getListAdapter().getItem(position).toString();
        Intent intent = new Intent(getActivity(), DirectoryDetailActivity.class);
        intent.putExtra(DirectoryDetailActivity.LETTER_KEY, letter);
        getActivity().startActivity(intent);
    }
}
