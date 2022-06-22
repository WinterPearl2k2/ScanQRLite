package com.example.scanqrlite.create.create_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.scan.ResultScan;

public class Create_wifi extends Fragment {
    EditText edtSSID, edtPass;
    RadioGroup rdgSecurity;
    RadioButton rdbWPA, rdbWEP, rdbNO;
    ImageButton btnCreate;
    String SSID = "";
    String password = "";
    String security = "";
    String content = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_wifi, container, false);
        ORM(view);
        crateWifi();
        return view;
    }

    private void crateWifi() {
        rdbWPA.setChecked(true);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SSID = edtSSID.getText().toString().trim();
                password = edtPass.getText().toString().trim();
                security = checkSecurity();

                content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
                if(password.length() < 8) {
                    if(password.length() < 8)
                        edtPass.setError("Please enter your link");
                } else {
                    Intent intent = new Intent(getActivity(), ResultScan.class);
                    intent.putExtra("create_txt", content);
                    intent.putExtra("S", SSID);
                    intent.putExtra("P", password);
                    intent.putExtra("T", security);
                    intent.putExtra("create_title", "Wifi");
                    intent.putExtra("type", "QRcode");
                    startActivity(intent);
                }
            }
        });
    }

    String s = "";
    private String checkSecurity() {
        rdgSecurity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_wep:
                        s =  "WEP";
                        break;
                    case R.id.btn_wpa:
                        s = "WPA";
                        break;
                    case R.id.btn_nothing:
                        s = "nopass";
                        break;
                }
            }
        });
        return s;
    }

    private void ORM(View view) {
        edtSSID = view.findViewById(R.id.text_input);
        edtPass = view.findViewById(R.id.text_password);
        rdgSecurity = view.findViewById(R.id.container_btn_create);
        rdbWEP = view.findViewById(R.id.btn_wep);
        rdbWPA = view.findViewById(R.id.btn_wpa);
        rdbNO = view.findViewById(R.id.btn_nothing);
        btnCreate = view.findViewById(R.id.btn_create);
    }
}