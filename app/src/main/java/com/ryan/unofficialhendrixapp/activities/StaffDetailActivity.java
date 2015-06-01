package com.ryan.unofficialhendrixapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff_categories.staff_details.StaffDetailFragment;
import com.ryan.unofficialhendrixapp.fragments.staff_categories.staff_details.StaffGridFragment;

// TODO: Setup dual pane b/w department/letter and StaffDetail fragment for tablets. Put this into onPersonSelected().
public class StaffDetailActivity extends BaseActivity implements StaffGridFragment.OnPersonSelectedListener{
    private final String LOG_TAG = getClass().getSimpleName();

    public static final String DEPT_KEY = "dept_key";
    public static final String NAME_KEY = "ltr_key";
    public static final String ID_KEY = "ID_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_detail);

        if ( savedInstanceState == null )
            setUpFirstFragment();
    }

    private void setUpFirstFragment() {
        Fragment fragment;

        if (getIntent().hasExtra(DEPT_KEY)) {
            fragment = getDepartmentFragment();
        } else if (getIntent().hasExtra(NAME_KEY)) {
            fragment = getLetterFragment();
        } else
            fragment = getStaffDetailFragment();

        startFragment(fragment);
    }

    private Fragment getDepartmentFragment(){
        String dept = getIntent().getStringExtra(DEPT_KEY);
        return StaffGridFragment.newInstance(dept, null);
    }

    private Fragment getLetterFragment(){
        String letter = getIntent().getStringExtra(NAME_KEY);
        return StaffGridFragment.newInstance(null, letter);
    }

    private Fragment getStaffDetailFragment(){
        int id = getIntent().getIntExtra(ID_KEY, 0);
        return StaffDetailFragment.newInstance(id);
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_directory_detail_container, fragment).commit();
    }

    @Override
    public void onPersonSelected(long id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_directory_detail_container, StaffDetailFragment.newInstance( (int) id) )
                .addToBackStack(null)
                .commit();
    }
}
