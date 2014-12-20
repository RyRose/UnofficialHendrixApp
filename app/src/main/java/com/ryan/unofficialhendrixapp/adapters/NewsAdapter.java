package com.ryan.unofficialhendrixapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.models.Entry;

/**
 * Created by ryan on 12/19/14.
 */
public class NewsAdapter extends ArrayAdapter<Entry> {

    Context mContext;
    int mResource;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public NewsAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
    }

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
