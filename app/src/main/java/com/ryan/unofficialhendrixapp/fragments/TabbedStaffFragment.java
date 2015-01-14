package com.ryan.unofficialhendrixapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff.ByDeptFragment;
import com.ryan.unofficialhendrixapp.fragments.staff.ByLetterFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TabbedStaffFragment extends BaseNavDrawerFragment {

    private static final int BY_DEPT_POSITION = 0;
    private static final int BY_LETTER_POSITION = 1;

    @InjectView(R.id.staff_pager) ViewPager mViewPager;
    @InjectView(R.id.staff_tabs) PagerSlidingTabStrip tabs;


    public static TabbedStaffFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(NAV_DRAWER_KEY, pos);
        TabbedStaffFragment fragment = new TabbedStaffFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_staff_tabbed, container, false);
        ButterKnife.inject(this, rootView);
        setUpViewPagerAndTabs();
        return rootView;
    }

    private void setUpViewPagerAndTabs() {
        mViewPager.setAdapter( new StaffPagerAdapter() );
        tabs.setViewPager(mViewPager);
    }

    private class StaffPagerAdapter extends FragmentPagerAdapter {

        public StaffPagerAdapter() {
            super(getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case BY_DEPT_POSITION:
                    return ByDeptFragment.newInstance();
                case BY_LETTER_POSITION:
                    return ByLetterFragment.newInstance();
                default:
                    throw new IllegalArgumentException("Fragment not found in viewpager");
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tab_names)[position];
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.tab_names).length;
        }

    }
}
