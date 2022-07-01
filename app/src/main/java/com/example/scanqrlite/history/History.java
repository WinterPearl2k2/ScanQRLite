package com.example.scanqrlite.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.History_Create;
import com.example.scanqrlite.history.History_Menu.History_Scan;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class History extends Fragment {
    TabItem tabScan, tabCreate;
    TabLayout tabContainer;
    TextView title_history;
    AdView adsViewHistory;
    AdRequest adRequest;

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
        SharedPreferences preferences = getActivity().getSharedPreferences("action_tab", Context.MODE_PRIVATE);
        int check = preferences.getInt("action_tab", 0);

        accessLayout(check, preferences);

        tabContainer.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        accessLayout(0, preferences);
                        break;
                    case 1:
                        accessLayout(1, preferences);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void accessLayout(int num, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        Fragment fragment;
        switch (num) {
            case 0:
                tabContainer.selectTab(tabContainer.getTabAt(0));
                fragment = new History_Scan();
                break;
            case 1:
                tabContainer.selectTab(tabContainer.getTabAt(1));
                fragment = new History_Create();
                break;
            default:
                tabContainer.selectTab(tabContainer.getTabAt(0));
                fragment = new History_Scan();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.history_layout, fragment);
        fragmentTransaction.commit();
        editor.putInt("action_tab", num);
        editor.commit();
    }

    private void ORM(View view) {
        tabScan = view.findViewById(R.id.tab_scan);
        tabCreate = view.findViewById(R.id.tab_create);
        tabContainer = view.findViewById(R.id.tab_container);
        title_history = view.findViewById(R.id.title_history);
        adsViewHistory = view.findViewById(R.id.adsViewHistory);
    }
}