package com.example.scanqrlite.create;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.scanqrlite.DateTime;
import com.example.scanqrlite.R;
import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.database.CreateDatabase;
import com.example.scanqrlite.scan.ResultScan;

public class CheckCreate {
    EditText editText;
    ImageButton imageButton;
    String notifyEror, type;
    Context context;

    public CheckCreate(EditText editText, ImageButton imageButton, String notifyEror,
                       Context context, String type) {
        this.editText = editText;
        this.imageButton = imageButton;
        this.notifyEror = notifyEror;
        this.context = context;
        this.type = type;
    }

    public void create() {
        if (editText.getText().toString().trim().length() < 1) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setError(notifyEror);
                }
            });
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    imageButton.setImageResource(R.drawable.ic_btn_create_true);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ResultScan.class);
                            intent.putExtra("create_txt", editText.getText().toString().trim());
                            intent.putExtra("create_title", type);
                            intent.putExtra("type", "QRcode");
                            //
                            DateTime dateTime = new DateTime();
                            HistoryCreateItem createItem = new HistoryCreateItem(type, editText.getText().toString().trim(), dateTime.getDateTime(),
                                                                                    editText.getText().toString().trim());
                            CreateDatabase.getInstance(context).createItemDAO().insertItem(createItem);
                            //
                            context.startActivity(intent);
                        }
                    });
                } else {
                    imageButton.setImageResource(R.drawable.ic_button);
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editText.setError(notifyEror);
                        }
                    });
                }
            }
        });
    }
}
