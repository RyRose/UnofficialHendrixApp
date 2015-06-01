package com.ryan.unofficialhendrixapp.fragments.nav_drawer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ryan.unofficialhendrixapp.R;
import com.ryan.unofficialhendrixapp.adapters.NewsAdapter;
import com.ryan.unofficialhendrixapp.data.HendrixContract.NewsColumn;
import com.ryan.unofficialhendrixapp.models.NewsEvent;
import com.ryan.unofficialhendrixapp.receivers.NewsReceiver;
import com.ryan.unofficialhendrixapp.services.NewsRefreshService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import icepick.Icepick;
import icepick.Icicle;

public class NewsFragment extends BaseNavDrawerFragment implements LoaderManager.LoaderCallbacks {
    private final String LOG_TAG = getClass().getSimpleName();

    public static final String[] NEWS_COLUMNS = {
            NewsColumn._ID,
            NewsColumn.COLUMN_TITLE,
            NewsColumn.COLUMN_DESCRIPTION,
            NewsColumn.COLUMN_DATE,
            NewsColumn.COLUMN_LINK
    };

    public static final int COL_NEWS_TITLE = 1;
    public static final int COL_NEWS_DESCRIPTION = 2;
    public static final int COL_NEWS_DATE = 3;
    public static final int COL_NEWS_LINK = 4;

    @InjectView(R.id.fragment_news_listView)
    ListView mListView;

    @InjectView(R.id.fragment_news_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Icicle
    int mPosition;

    private NewsAdapter mNewsAdapter;

    public static NewsFragment newInstance(int pos) {
        Bundle bundle = new Bundle();
        bundle.putInt(NAV_DRAWER_KEY, pos);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        mNewsAdapter = new NewsAdapter(getActivity(), null);
        setUpNewsAlarm();
    }

    private void setUpNewsAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), NewsReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), NewsReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_HALF_DAY,
                AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, rootView);
        mSwipeRefreshLayout.setOnRefreshListener( this::refresh );
        getLoaderManager().initLoader(0, null, this);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setAdapter(mNewsAdapter);
        mListView.setSelection(mPosition);
        EventBus.getDefault().register(this);

        if (isFirstNewsPull())
            new Handler().postDelayed( this::refresh , 500l);
    }

    private boolean isFirstNewsPull() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getBoolean(NewsRefreshService.INITIAL_REFRESH_KEY, true);
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        Intent intent = new Intent(getActivity(), NewsRefreshService.class);
        getActivity().startService(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mSwipeRefreshLayout.setRefreshing(false);
        mPosition = mListView.getFirstVisiblePosition();
        mListView.setAdapter(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.fragment_news_listView)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c = mNewsAdapter.getCursor();
        String link = c.getString(NewsFragment.COL_NEWS_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(NewsEvent event) {
        getLoaderManager().restartLoader(0, null, this);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader( getActivity(), NewsColumn.CONTENT_URI, NEWS_COLUMNS, null, null, NewsColumn.COLUMN_DATE + " DESC");
    }

    @Override public void onLoadFinished(Loader loader, Object data) {
        mNewsAdapter.changeCursor((Cursor) data);
    }

    @Override public void onLoaderReset(Loader loader) {
        mNewsAdapter.changeCursor(null);
    }
}
