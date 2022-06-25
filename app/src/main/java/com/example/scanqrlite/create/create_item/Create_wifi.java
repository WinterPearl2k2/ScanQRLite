package com.example.scanqrlite.create.create_item;

import static android.view.View.GONE;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;
import com.example.scanqrlite.scan.ResultScan;

public class Create_wifi extends Fragment {
    EditText edtSSID, edtPass;
    RadioGroup rdgSecurity;
    RadioButton rdbWPA, rdbWEP, rdbNO;
    ImageButton btnCreate;
    LinearLayout layoutPass;
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

    boolean check = true;
    public void createWifi() {
        rdbWPA.setChecked(true);
        SSID = edtSSID.getText().toString().trim();
        checkNothing();
        password = edtPass.getText().toString().trim();
        checkEmpty(check);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SSID = edtSSID.getText().toString().trim();
                checkNothing();
                password = edtPass.getText().toString().trim();
                checkEmpty(check);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
                if (edtSSID.getText().toString().trim().length() > 0 && check == true ? edtPass.getText().toString().trim().length() > 7 : edtPass.getText().toString().trim().length() == 0) {
                    btnCreate.setImageResource(R.drawable.ic_btn_create_true);
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            security = checkSecurity(rdbWPA, rdbWEP, rdbNO);
                            sendData();
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

    private void checkNothing() {
        rdgSecurity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_nothing:
                        layoutPass.setVisibility(GONE);
                        check = false;
                        edtPass.setText("");
                        break;
                    case R.id.btn_wep:
                    case R.id.btn_wpa:
                        layoutPass.setVisibility(View.VISIBLE);
                        check = true;
                        break;
                }
            }
        });
    }

    private void sendData() {
        Intent intent = new Intent(getActivity(), ResultScan.class);
        intent.putExtra("create_txt", content);
        intent.putExtra("S", SSID);
        intent.putExtra("P", password);
        intent.putExtra("T", security);
        intent.putExtra("create_title", "Wifi");
        intent.putExtra("type", "QRcode");
        HistoryCreateItem createItem = new HistoryCreateItem("Wifi",SSID, "10/11/2002", content);
        CreateDatabase.getInstance(getActivity()).createItemDAO().insertItem(createItem);
        startActivity(intent);
    }

    private void checkEmpty(boolean check) {
        if(check == true) {
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
        } else {
            if (SSID.length() < 1) {
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (SSID.length() < 1)
                            edtSSID.setError("Account not be blank");
                    }
                });
            }
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
        layoutPass = view.findViewById(R.id.layout_password);
    }
}