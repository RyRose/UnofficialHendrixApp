package com.ryan.unofficialhendrixapp.adapters.staff;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff.ByDeptFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ByDeptAdapter extends CursorAdapter {

    public ByDeptAdapter(Context context, Cursor c) { super(context, c, false); }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.fragment_staff_dept_item, parent, false);
        view.setTag( new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.view.setText( cursor.getString( ByDeptFragment.COL_DEPARTMENT_NAME) );
    }

    class ViewHolder {
        @InjectView(R.id.department_view) public TextView view;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
