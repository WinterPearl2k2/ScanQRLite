package com.example.scanqrlite.adapter;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scanqrlite.history.History_Menu.History_Create;
import com.example.scanqrlite.history.History_Menu.History_Scan;

public class HistoryAdapter extends FragmentStateAdapter {

    public HistoryAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new History_Scan();
            case 1: return new History_Create();
            default: return new History_Scan();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
