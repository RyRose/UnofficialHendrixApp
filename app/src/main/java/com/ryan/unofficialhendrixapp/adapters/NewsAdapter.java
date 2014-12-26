package com.ryan.unofficialhendrixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.models.NewsEntry;

/**
 * Created by ryan on 12/19/14.
 */
public class NewsAdapter extends ArrayAdapter<NewsEntry> {

    Context mContext;
    int mResource;

    /**
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 populating the list view
     *
     */
    public NewsAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }

    /**
     *
     * Used when populating the list view for each individual element of the list view
     *
     * @param position Location of the view in Array Adapter
     * @return The view for each element of the listViewNews
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item_news, parent, false);
        TextView titleView = (TextView) rowView.findViewById(R.id.newsView);
        TextView dateView = (TextView) rowView.findViewById(R.id.dateView);

        dateView.setText(getItem(position).getDate());
        titleView.setText(getItem(position).getTitle());
        return rowView;
    }
}
