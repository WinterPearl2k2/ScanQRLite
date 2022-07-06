package com.example.scanqrlite.create.create_item;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.scanqrlite.CloseKeyBoard;
import com.example.scanqrlite.DateTime;
import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;
import com.example.scanqrlite.scan.ResultScan;

public class Create_wifi extends Fragment {
    EditText edtSSID, edtPass;
    RadioGroup rdgSecurity;
    RadioButton rdbWPA, rdbWEP, rdbNO;
    Button btnCreate;
    LinearLayout layoutPass;
    String SSID;
    String password;
    String security;
    String content;
    ScrollView hihi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_wifi, container, false);
        ORM(view);
        hihi = view.findViewById(R.id.hihi9);
        view.setOnTouchListener(new CloseKeyBoard(getActivity()));
        create();
        return view;
    }

    boolean nothing = false;
    private void create() {
        rdbWPA.setChecked(true);
        SSID = edtSSID.getText().toString().trim();
        password = edtPass.getText().toString().trim();
        security = getSecurity();

        rdgSecurity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.btn_wep: security = "WEP";
                        nothing = false;
                        edtPass.setEnabled(true);
                        layoutPass.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btn_wpa: security = "WPA";
                        nothing = false;
                        edtPass.setEnabled(true);
                        layoutPass.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btn_nothing: security = "nopass";
                        nothing = true;
                        layoutPass.setVisibility(View.GONE);
                        edtPass.setEnabled(false);
                        hihi.setFocusable(true);
                        hihi.requestFocusFromTouch();
                        break;
                }
                clickNothing(nothing);
            }
        });



        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyError(checkIsEmpty());
            }
        });

        TextWatcher textWatcher = getTextWatcher();
        edtSSID.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);
    }

    private void clickNothing(boolean nothing) {
        if (nothing) {
            content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + ";H:false;";
            if(edtSSID.getText().toString().trim().length() < 1) {
                btnCreate.setTextColor(getResources().getColor(R.color.black20));
                btnCreate.setBackgroundResource(R.drawable.cus_btn_create);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtSSID.setError("Account not be blank");
                    }
                });
            } else {
                btnCreate.setTextColor(getResources().getColor(R.color.black));
                btnCreate.setBackgroundResource(R.drawable.cus_btn_create_alow);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendData();
                    }
                });
            }
        } else {
            content = "WIFI:T:" + security + ";S:" + SSID + ";P:" + password + ";H:false;";
            if (checkIsEmpty() != 0) {
                btnCreate.setTextColor(getResources().getColor(R.color.black20));
                btnCreate.setBackgroundResource(R.drawable.cus_btn_create);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyError(checkIsEmpty());
                    }
                });
            } else {
                btnCreate.setTextColor(getResources().getColor(R.color.black));
                btnCreate.setBackgroundResource(R.drawable.cus_btn_create_alow);
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendData();
                    }
                });
            }
        }
    }

    @NonNull
    private TextWatcher getTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SSID = edtSSID.getText().toString().trim();
                password = edtPass.getText().toString().trim();

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SSID = edtSSID.getText().toString().trim();
                password = edtPass.getText().toString().trim();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                clickNothing(nothing);
            }
        };
        return textWatcher;
    }

    private String getSecurity() {
        if(rdbWEP.isChecked())
            return "WEP";
        if (rdbWPA.isChecked())
            return "WPA";
        return "nopass";
    }

    private void notifyError(int checkIsEmpty) {
        if(checkIsEmpty == 1)
            edtSSID.setError("Account not be blank");
        if(checkIsEmpty == 2)
            edtPass.setError("Password please enter more than 8 characters");
        if(checkIsEmpty == 3) {
            edtSSID.setError("Account not be blank");
            edtPass.setError("Password please enter more than 1 characters");
        }
    }

    private int checkIsEmpty() {
        if(edtSSID.getText().toString().trim().length() < 1 && edtPass.getText().toString().trim().length() < 8)
            return 3;
        else if(edtSSID.getText().toString().trim().length() < 1)
            return 1;
        else if(edtPass.getText().toString().trim().length() < 8)
            return 2;
        return 0;
    }

    private void sendData() {
        Intent intent = new Intent(getActivity(), ResultScan.class);
        intent.putExtra("create_txt", content);
        intent.putExtra("S", SSID);
        intent.putExtra("P", nothing ? "" : password);
        intent.putExtra("T", security);
        intent.putExtra("create_title", "Wifi");
        intent.putExtra("type", "QRcode");
        intent.putExtra("type_barcode", "QRcode");
        DateTime dateTime = new DateTime();
        HistoryCreateItem createItem = new HistoryCreateItem("Wifi",SSID, dateTime.getDateTime(), content);
        createItem.setPassword(password);
        createItem.setSecurity(security);
        CreateDatabase.getInstance(getActivity()).createItemDAO().insertItem(createItem);
        startActivity(intent);
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