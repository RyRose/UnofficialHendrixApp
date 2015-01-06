package com.ryan.unofficialhendrixapp.fragments.directory;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ryan.unofficialhendrixapp.activities.DirectoryDetailActivity;

public class PersonDetailFragment extends Fragment{

    public static PersonDetailFragment newInstance (String name) {
        Bundle bundle = new Bundle();
        bundle.putString(DirectoryDetailActivity.NAME_KEY, name);
        PersonDetailFragment fragment = new PersonDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
