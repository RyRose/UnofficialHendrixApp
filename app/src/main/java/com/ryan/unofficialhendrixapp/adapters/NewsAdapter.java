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

/**
 * Created by ryan on 12/29/14.
 */
public class NewsAdapter extends CursorAdapter {

    /**
     * Recommended constructor.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter; may
     *                be any combination of {@link #FLAG_AUTO_REQUERY} and
     *                {@link #FLAG_REGISTER_CONTENT_OBSERVER}.
     */
    public NewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     *
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created view.
     */

    // TODO: Implement different layout for first entry of ListView
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder;
        View view;
        int layoutId;
        int position = cursor.getPosition();

        // layoutId = (position == 0) ? R.layout.list_item_news_recent : R.layout.list_item_news;
        layoutId = R.layout.list_item_news;
        view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new ViewHolder(view);

        view.setTag(holder);
        return view;
    }

    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String titleString = cursor.getString(NewsFragment.COL_NEWS_TITLE);
        String dateString = cursor.getString(NewsFragment.COL_NEWS_DATE);

        holder.titleView.setText(titleString);
        holder.dateView.setText(dateString);
    }

    public static class ViewHolder {
        @InjectView(R.id.titleView) TextView titleView;
        @InjectView(R.id.dateView) TextView dateView;

        public ViewHolder( View view) {
            ButterKnife.inject(this, view);
        }
    }
}
