package com.example.scanqrlite.create.create_item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scanqrlite.R;
import com.example.scanqrlite.create.CheckCreate;
import com.example.scanqrlite.scan.ResultScan;


public class Create_Text extends Fragment {
    EditText edtInput;
    ImageButton btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_text, container, false);
        ORM(view);
        createText();
        return view;
    }

    private void createText() {
        CheckCreate checkCreate = new CheckCreate(edtInput, btnCreate, "Please enter your content", getActivity(), "Text");
        checkCreate.create();
    }

    private void ORM(View view) {
        edtInput = view.findViewById(R.id.text_input);
        btnCreate = view.findViewById(R.id.btn_create_text);
    }
}
