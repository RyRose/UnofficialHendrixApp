package com.ryan.unofficialhendrixapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.directory.AlphabeticalFragment;
import com.ryan.unofficialhendrixapp.fragments.directory.DepartmentFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TabbedDirectoryFragment extends Fragment {

    @InjectView(R.id.staffPager) ViewPager mViewPager;


    public static TabbedDirectoryFragment newInstance(int pos, Context context) {
        Bundle bundle = new Bundle();
        bundle.putInt(context.getResources().getString(R.string.fragment_pos_key), pos);
        TabbedDirectoryFragment fragment = new TabbedDirectoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dir_tabbed, container, false);
        ButterKnife.inject(this, rootView);
        mViewPager.setAdapter(new StaffPagerAdapter());
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int name_pos = getArguments().getInt(getResources().getString(R.string.fragment_pos_key));
        getActivity().setTitle(getResources().getStringArray(R.array.drawer_names)[name_pos]);
    }


    private class StaffPagerAdapter extends FragmentPagerAdapter {

        public StaffPagerAdapter() {
            super(getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DepartmentFragment.newInstance();
                case 1:
                    return AlphabeticalFragment.newInstance();
                default:
                    throw new IllegalArgumentException("Fragment not found in viewpager");
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "By Dept";
                case 1:
                    return "By Name";
                default:
                    throw new IllegalArgumentException("Page cannot be found in ViewPager");
            }
        }
    }
}
