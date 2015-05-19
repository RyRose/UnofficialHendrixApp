package com.ryan.unofficialhendrixapp.fragments.nav_drawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
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
import com.ryan.unofficialhendrixapp.services.NewsRefreshService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import icepick.Icepick;
import icepick.Icicle;

public class NewsFragment extends BaseNavDrawerFragment implements LoaderManager.LoaderCallbacks {
    public final String LOG_TAG = getClass().getSimpleName();

    public static final String[] NEWS_COLUMNS = {
            NewsColumn._ID,
            NewsColumn.COLUMN_TITLE,
            NewsColumn.COLUMN_DESCRIPTION,
            NewsColumn.COLUMN_DATE,
            NewsColumn.COLUMN_LINK
    };

    public static final int COL_NEWS_ID = 0;
    public static final int COL_NEWS_TITLE = 1;
    public static final int COL_NEWS_DESCRIPTION = 2;
    public static final int COL_NEWS_DATE = 3;
    public static final int COL_NEWS_LINK = 4;

    private NewsReceiver mReceiver;


    @InjectView(R.id.fragment_news_listView) ListView mListView;
    @InjectView(R.id.fragment_news_swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Icicle int mPosition = 0;
    NewsAdapter mNewsAdapter;

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
        mReceiver = new NewsReceiver(new Handler(), this);
        mNewsAdapter = new NewsAdapter(getActivity(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, rootView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        boolean isFirstNewsPull = prefs.getBoolean(NewsRefreshService.INITIAL_REFRESH_KEY, true);
        if (isFirstNewsPull)
            refresh();

        getLoaderManager().initLoader(0, null, this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mListView.setAdapter(mNewsAdapter);
        mListView.setSelection(mPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        mListView.setAdapter(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPosition = mListView.getFirstVisiblePosition();
        Icepick.saveInstanceState(this, outState);
    }

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
        inflater.inflate(R.menu.menu_fragment_news, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        Intent intent = new Intent(getActivity(), NewsRefreshService.class);
        intent.putExtra(NewsRefreshService.RECEIVER_KEY, mReceiver);
        getActivity().startService(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader( getActivity(), NewsColumn.CONTENT_URI, NEWS_COLUMNS, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mNewsAdapter.changeCursor((Cursor) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mNewsAdapter.changeCursor(null);
    }

    private class NewsReceiver extends ResultReceiver {
        private LoaderManager.LoaderCallbacks mCallback;

        public NewsReceiver(Handler handler, LoaderManager.LoaderCallbacks callback) {
            super(handler);
            mCallback = callback;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (isAdded()) {
                getLoaderManager().restartLoader(0, null, mCallback);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
