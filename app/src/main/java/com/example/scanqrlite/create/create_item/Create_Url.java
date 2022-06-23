package com.example.scanqrlite.create.create_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.create.CheckCreate;
import com.example.scanqrlite.scan.ResultScan;

public class Create_Url extends Fragment {

    EditText edtInput;
    ImageButton btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_url, container, false);
        ORM(view);
        createURL();
        return view;
    }

    private void createURL() {
        CheckCreate checkCreate = new CheckCreate(edtInput, btnCreate, "Please enter your url", getActivity(), "URL");
        checkCreate.create();
    }

    private void ORM(View view) {
        edtInput = view.findViewById(R.id.text_input);
        btnCreate = view.findViewById(R.id.btn_create);
    }
}