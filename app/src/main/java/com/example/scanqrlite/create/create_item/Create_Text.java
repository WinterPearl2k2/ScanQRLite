package com.example.scanqrlite.create.create_item;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.scanqrlite.R;
import com.example.scanqrlite.scan.ResultScan;


public class Create_Text extends Fragment {
    EditText edtInput;
    ImageButton btnCreate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_text, container, false);
        ORM(view);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ResultScan.class);
                intent.putExtra("create_txt", "" + edtInput.getText());
                intent.putExtra("create_title", "Text");
                intent.putExtra("type", "QRcode");
                startActivity(intent);
            }
        });
        return view;
    }

    private void ORM(View view) {
        edtInput = view.findViewById(R.id.text_input);
        btnCreate = view.findViewById(R.id.btn_create_text);
    }
}