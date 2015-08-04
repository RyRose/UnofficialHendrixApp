package com.ryan.unofficialhendrixapp.fragments.nav_drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.pdfview.PDFView;
import com.ryan.unofficialhendrixapp.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapFragment extends BaseNavDrawerFragment {

    @InjectView(R.id.fragment_map_pdfview) PDFView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragments_navdrawer_mapfragment, container, false);
        ButterKnife.inject(this, rootView);
        mapView.fromAsset("campus_map.pdf").pages(0).defaultPage(0).enableSwipe(false).load();
        return rootView;
    }
}
