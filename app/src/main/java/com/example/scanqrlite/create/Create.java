package com.example.scanqrlite.create;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.scanqrlite.R;
import com.example.scanqrlite.create.create_item.Create_Text;
import com.example.scanqrlite.create.create_item.Create_Url;
import com.example.scanqrlite.create.create_item.Create_wifi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Create extends Fragment {

    RadioButton btnText, btnURL, btnWifi;
    FrameLayout createLayout;
    AdView adsViewCreate;
    AdRequest adRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ORM(view);
        SwitchView();
        ShowAds();
        return view;
    }

    @Override
    public void onStart() {
        if(adsViewCreate != null)
            adsViewCreate.pause();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adsViewCreate != null)
            adsViewCreate.resume();
    }

    @Override
    public void onDestroy() {
        if (adsViewCreate != null) {
            adsViewCreate.destroy();
        }
        super.onDestroy();
    }

    private void ShowAds() {
        adRequest = new AdRequest.Builder().build();
        adsViewCreate.loadAd(adRequest);
    }

    private void SwitchView() {
        SharedPreferences preferences = getActivity().getSharedPreferences("actionbtn", Context.MODE_PRIVATE);
        int check = preferences.getInt("actionbtn", 0);
        changeBtn(check, preferences);
        AccessClass(check);
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnText.isChecked()) {
                    changeBtn(1, preferences);
                    AccessClass(1);
                }
            }
        });
        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnURL.isChecked()) {
                    changeBtn(2, preferences);
                    AccessClass(2);
                }
            }
        });
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnWifi.isChecked()) {
                    changeBtn(3, preferences);
                    AccessClass(3);
                }
            }
        });
    }

    private void AccessClass(int num) {
        Fragment fragment2;
        switch (num) {
            case 1:  fragment2 = new Create_Text();
                break;
            case 2:  fragment2 = new Create_Url();
                break;
            case 3:  fragment2 = new Create_wifi();
                break;
            default: fragment2 = new Create_Text();
                break;
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.create_Layout, fragment2);
        fragmentTransaction.commit();
    }

    private void changeBtn(int check, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        switch (check) {
            case 1:
                btnText.setChecked(true);
                editor.putInt("actionbtn", 1);
                break;
            case 2:
                btnURL.setChecked(true);
                editor.putInt("actionbtn", 2);
                break;
            case 3:
                btnWifi.setChecked(true);
                editor.putInt("actionbtn", 3);
                break;
            default:
                btnText.setChecked(true);
                editor.putInt("actionbtn", 1);
                break;
        }
        editor.commit();
    }

    private void ORM(View view) {
        btnText = view.findViewById(R.id.btn_text);
        btnURL = view.findViewById(R.id.btn_url);
        btnWifi = view.findViewById(R.id.btn_wifi);
        createLayout = view.findViewById(R.id.create_Layout);
        adsViewCreate = view.findViewById(R.id.adsView_Create);
    }
}