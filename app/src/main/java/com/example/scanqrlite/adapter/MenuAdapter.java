package com.example.scanqrlite.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.scanqrlite.create.Create;
import com.example.scanqrlite.history.History;
import com.example.scanqrlite.scan.Scan;
import com.example.scanqrlite.setting.Setting;

public class MenuAdapter extends FragmentStateAdapter {
    public MenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Scan();
            case 1:
                return new Create();
            case 2:
                return new History();
            case 3:
                return new Setting();
            default:
                return new Scan();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
