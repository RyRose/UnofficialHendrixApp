package com.ryan.unofficialhendrixapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.nav_drawer.NewsFragment;
import com.ryan.unofficialhendrixapp.helpers.DateUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsAdapter extends CursorAdapter {

    public NewsAdapter(Context context, Cursor c) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        View view;
        int layoutId;

        layoutId = R.layout.fragment_news_list_item;
        view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new ViewHolder(view);

        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String titleString = cursor.getString(NewsFragment.COL_NEWS_TITLE);
        String dateString = DateUtils.getPresentableDate(DateUtils.convertToDate(cursor.getLong(NewsFragment.COL_NEWS_DATE)));

        holder.titleView.setText(titleString);
        holder.dateView.setText(dateString);
    }

    public static class ViewHolder {
        @InjectView(R.id.titleView) public TextView titleView;
        @InjectView(R.id.dateView)  public TextView dateView;

        public ViewHolder( View view) {
            ButterKnife.inject(this, view);
        }
    }
}
