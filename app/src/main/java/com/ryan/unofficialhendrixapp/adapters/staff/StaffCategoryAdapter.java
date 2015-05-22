package com.ryan.unofficialhendrixapp.adapters.staff;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffCategoryAdapter extends CursorAdapter {

    private int column_number;

    public StaffCategoryAdapter(Context context, int column) {
        super(context, null, false);
        column_number = column;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.fragment_staff_dept_item, parent, false);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.view.setText(cursor.getString(column_number));
    }

    class ViewHolder {
        @InjectView(R.id.department_view)
        public TextView view;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}