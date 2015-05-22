package com.ryan.unofficialhendrixapp.adapters.staff;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.staff_categories.staff_details.StaffGridFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StaffGridAdapter extends CursorAdapter {

    public StaffGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.fragment_staff_grid_item, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        rootView.setTag(holder);
        return rootView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.textView.setText(cursor.getString(StaffGridFragment.COL_GRID_FULL_NAME));

        String picture = cursor.getString(StaffGridFragment.COL_GRID_PIC);
        Picasso.with(context).load(picture)
                .fit().centerCrop().into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError() {
                Picasso.with(context)
                        .load(R.drawable.placeholder_person)
                        .fit()
                        .centerCrop()
                        .into(holder.imageView);
            }
        });
    }

    class ViewHolder {
        @InjectView(R.id.fragment_directory_grid_image) ImageView imageView;
        @InjectView(R.id.fragment_directory_grid_text) TextView textView;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
