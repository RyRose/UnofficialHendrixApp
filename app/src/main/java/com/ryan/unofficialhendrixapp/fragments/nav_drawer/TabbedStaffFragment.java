package com.ryan.unofficialhendrixapp.fragments.nav_drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.astuetz.PagerSlidingTabStrip;
import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff_categories.ByDeptFragment;
import com.ryan.unofficialhendrixapp.fragments.staff_categories.ByNameFragment;
import com.ryan.unofficialhendrixapp.models.Staff;
import com.ryan.unofficialhendrixapp.services.StaffDatabaseService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class TabbedStaffFragment extends BaseNavDrawerFragment {

    private static final int BY_DEPT_POSITION = 0;
    private static final int BY_LETTER_POSITION = 1;

    @InjectView(R.id.staff_pager) ViewPager mViewPager;
    @InjectView(R.id.staff_tabs) PagerSlidingTabStrip tabs;
    @InjectView(R.id.staff_progress_bar) ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragments_navdrawer_tabbedstafffragment, container, false);
        ButterKnife.inject(this, rootView);
        setUpUI();
        return rootView;
    }

    private void setUpUI() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isFirstPull = prefs.getBoolean(StaffDatabaseService.INITIAL_STAFF_FILL_KEY, true);
        boolean isDoneWithFirstPull = prefs.getBoolean(StaffDatabaseService.DONE_STAFF_FILL_KEY, false);

        EventBus.getDefault().register(this);
        if(isFirstPull) {
            setUpStaffTable();
            startRefreshing();
        } else if (isDoneWithFirstPull) {
            setUpViewPagerAndTabs();
        } else {
            startRefreshing();
        }
    }

    private void setUpViewPagerAndTabs() {
        mViewPager.setAdapter(new StaffPagerAdapter());
        tabs.setVisibility(View.VISIBLE);
        tabs.setViewPager(mViewPager);
    }

    private void setUpStaffTable() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isFirstPull = prefs.getBoolean(StaffDatabaseService.INITIAL_STAFF_FILL_KEY, true);
        if ( isFirstPull )
            createStaffTable();
    }

    private void createStaffTable() {
        Intent intent = new Intent(getActivity(), StaffDatabaseService.class);
        getActivity().startService(intent);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(Staff staff) {
        EventBus.getDefault().unregister(this);
        stopRefreshing();
        setUpViewPagerAndTabs();
    }

    private void startRefreshing() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void stopRefreshing() {
        progressBar.setVisibility(View.GONE);
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
                    return ByNameFragment.newInstance();
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
