package com.ryan.unofficialhendrixapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ryan.unofficialhendrixapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebFragment extends BaseNavDrawerFragment {

    @InjectView(R.id.fragment_web_webview) WebView mWebView;
    @InjectView(R.id.fragment_web_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public static WebFragment newInstance(int pos) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putInt(NAV_DRAWER_KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.inject(this, rootView);
        setUpWebView(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return rootView;
    }

    private void setUpWebView(Bundle savedInstanceState) {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        if (savedInstanceState != null) {
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.loadUrl(getString(R.string.campus_web_url));
        }
    }

    private void refresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mWebView.reload();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void onBackPressed() {
        mWebView.goBack();
    }
}
