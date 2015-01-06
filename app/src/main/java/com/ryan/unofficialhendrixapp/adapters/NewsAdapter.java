package com.ryan.unofficialhendrixapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.fragments.NewsFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NewsAdapter extends CursorAdapter {

    public NewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    // TODO: Implement different layout for first entry of the ListView
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        View view;
        int layoutId;

        // layoutId = ( cursor.getPosition() == 0) ? R.layout.fragment_news_list_item_recent : R.layout.fragment_news_list_item;
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
        String dateString = cursor.getString(NewsFragment.COL_NEWS_DATE);

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
