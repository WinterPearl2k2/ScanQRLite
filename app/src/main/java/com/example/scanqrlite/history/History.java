package com.example.scanqrlite.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.adapter.HistoryAdapter;
import com.example.scanqrlite.history.History_Menu.History_Create;
import com.example.scanqrlite.history.History_Menu.History_Scan;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class History extends Fragment {
    TabItem tabScan, tabCreate;
    TabLayout tabContainer;
    TextView title_history;
    AdView adsViewHistory;
    AdRequest adRequest;
    ViewPager2 viewPager2;
    HistoryAdapter adapter;
    int check;
    SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ORM(view);

        switchLayout();
        showAds();
        return view;
    }

    private void showAds() {
        adRequest = new AdRequest.Builder().build();
        adsViewHistory.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if(adsViewHistory != null)
            adsViewHistory.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        if(adsViewHistory != null)
            adsViewHistory.resume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adsViewHistory != null)
            adsViewHistory.destroy();
    }

    private void switchLayout() {
        adapter = new HistoryAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabContainer, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: tab.setText("Scan");
                        break;
                    case 1: tab.setText("Create");
                        break;
                }
            }
        }).attach();
    }

    private void ORM(View view) {
        tabScan = view.findViewById(R.id.tab_scan);
        tabCreate = view.findViewById(R.id.tab_create);
        tabContainer = view.findViewById(R.id.tab_container);
        title_history = view.findViewById(R.id.title_history);
        adsViewHistory = view.findViewById(R.id.adsViewHistory);
        viewPager2 = view.findViewById(R.id.history_layout);
    }
}