package com.example.scanqrlite.create.create_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scanqrlite.CloseKeyBoard;
import com.example.scanqrlite.R;
import com.example.scanqrlite.create.CheckCreate;
import com.example.scanqrlite.scan.ResultScan;

public class Create_Url extends Fragment {

    EditText edtInput;
    Button btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_url, container, false);
        ORM(view);
        view.setOnTouchListener(new CloseKeyBoard(getActivity()));

        createURL();
        return view;
    }

    private void createURL() {
        CheckCreate checkCreate = new CheckCreate(edtInput, btnCreate, getString(R.string.please_enter_your_url), getActivity(),getString(R.string.url));
        checkCreate.create();
    }

    private void ORM(View view) {
        edtInput = view.findViewById(R.id.text_input);
        btnCreate = view.findViewById(R.id.btn_create);
    }
}