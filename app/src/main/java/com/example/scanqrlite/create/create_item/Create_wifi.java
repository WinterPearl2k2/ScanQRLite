package com.example.scanqrlite.create.create_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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
    String SSID;
    String password;
    String security;
    String content = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_wifi, container, false);
        ORM(view);
        createWifi();
        return view;
    }

    public void createWifi() {
        rdbWPA.setChecked(true);
        SSID = edtSSID.getText().toString().trim();
        password = edtPass.getText().toString().trim();
        checkEmpty();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SSID = edtSSID.getText().toString().trim();
                password = edtPass.getText().toString().trim();
                security = checkSecurity(rdbWPA, rdbWEP, rdbNO);
                checkEmpty();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
                if (edtSSID.getText().toString().trim().length() > 0 && edtPass.getText().toString().trim().length() > 7) {
                    btnCreate.setImageResource(R.drawable.ic_btn_create_true);
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), ResultScan.class);
                            intent.putExtra("create_txt", content);
                            intent.putExtra("S", SSID);
                            intent.putExtra("P", password);
                            intent.putExtra("T", security);
                            intent.putExtra("create_title", "Wifi");
                            intent.putExtra("type", "QRcode");
                            startActivity(intent);
                        }
                    });
                } else {
                    btnCreate.setImageResource(R.drawable.ic_button);
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (SSID.trim().length() < 1)
                                edtSSID.setError("Account not be blank");
                            if (password.trim().length() < 8)
                                edtPass.setError("Password please enter more than 8 characters");
                        }
                    });
                }
            }
        };
        edtSSID.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);

    }

    private void checkEmpty() {
        if (password.length() < 8 || SSID.length() < 1) {
            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SSID.length() < 1)
                        edtSSID.setError("Account not be blank");
                    if (password.length() < 8)
                        edtPass.setError("Password please enter more than 8 characters");
                }
            });
        }
    }

    private String checkSecurity(RadioButton rdbWPA, RadioButton rdbWEP, RadioButton rdbNO) {
        String s = "";
        if(rdbWPA.isChecked())
            s = "WPA";
        else if(rdbWEP.isChecked())
            s =  "WEP";
        else if(rdbNO.isChecked())
            s = "nopass";
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