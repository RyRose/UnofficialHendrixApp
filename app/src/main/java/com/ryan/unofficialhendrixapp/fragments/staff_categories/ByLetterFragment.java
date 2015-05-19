package com.ryan.unofficialhendrixapp.fragments.staff_categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.activities.StaffDetailActivity;

public class ByLetterFragment extends BaseByCategoryFragment {


    private static final String[] LETTERS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
    "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static ByLetterFragment newInstance() {
        return new ByLetterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.fragment_staff_dept_item, LETTERS));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String letter = getListAdapter().getItem(position).toString();
        Intent intent = new Intent(getActivity(), StaffDetailActivity.class);
        intent.putExtra(StaffDetailActivity.LETTER_KEY, letter);
        getActivity().startActivity(intent);
    }
}
