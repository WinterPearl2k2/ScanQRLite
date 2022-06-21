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

public class Create extends Fragment {

    RadioButton btnText, btnURL, btnWifi;
    FrameLayout createLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create, container, false);
        ORM(view);
        SwitchView();
        return view;
    }

    private void SwitchView() {
        SharedPreferences preferences = getActivity().getSharedPreferences("actionbtn", Context.MODE_PRIVATE);
        String check = preferences.getString("actionbtn", "0");
        changeBtn(Integer.parseInt(check), preferences);
        AccessClass(Integer.parseInt(check));
        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnText.isChecked()) {
                    changeBtn(1, preferences);
                    AccessClass(1);
                } else {
                    btnText.setBackgroundResource(R.drawable.ic_create_text_while);
                }
            }
        });
        btnURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnURL.isChecked()) {
                    changeBtn(2, preferences);
                    AccessClass(2);
                } else {
                    btnURL.setBackgroundResource(R.drawable.ic_create_url_while);
                }
            }
        });
        btnWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnWifi.isChecked()) {
                    changeBtn(3, preferences);
                    AccessClass(3);
                } else {
                    btnWifi.setBackgroundResource(R.drawable.ic_create_wifi_while);
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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.create_Layout, fragment2);
        fragmentTransaction.commit();
    }

    private void changeBtn(int check, SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        switch (check) {
            case 1:
                btnText.setBackgroundResource(R.drawable.ic_create_text_yellow);
                btnText.setChecked(true);
                btnURL.setBackgroundResource(R.drawable.ic_create_url_while);
                btnWifi.setBackgroundResource(R.drawable.ic_create_wifi_while);
                editor.putString("actionbtn", "1");
                break;
            case 2:
                btnURL.setBackgroundResource(R.drawable.ic_create_url_yellow);
                btnText.setChecked(true);
                btnText.setBackgroundResource(R.drawable.ic_create_text_while);
                btnWifi.setBackgroundResource(R.drawable.ic_create_wifi_while);
                editor.putString("actionbtn", "2");
                break;
            case 3:
                btnWifi.setBackgroundResource(R.drawable.ic_create_wifi_yellow);
                btnText.setChecked(true);
                btnText.setBackgroundResource(R.drawable.ic_create_text_while);
                btnURL.setBackgroundResource(R.drawable.ic_create_url_while);
                editor.putString("actionbtn", "3");
                break;
            default:
                btnText.setBackgroundResource(R.drawable.ic_create_text_yellow);
                btnText.setChecked(true);
                btnURL.setBackgroundResource(R.drawable.ic_create_url_while);
                btnWifi.setBackgroundResource(R.drawable.ic_create_wifi_while);
                editor.putString("actionbtn", "1");
                break;
        }
        editor.commit();
    }

    private void ORM(View view) {
        btnText = view.findViewById(R.id.btn_text);
        btnURL = view.findViewById(R.id.btn_url);
        btnWifi = view.findViewById(R.id.btn_wifi);
        createLayout = view.findViewById(R.id.create_Layout);
    }
}