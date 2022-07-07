package com.example.scanqrlite.setting.settingitem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.scanqrlite.R;

public class QuestionAnswer extends AppCompatActivity {
    LinearLayout layoutBack;
    ImageButton back;
    TextView txtQA1, txtQA2, txtQA3;
    RelativeLayout btnQA1, btnQA2, btnQA3;
    ImageView imgQA1, imgQA2, imgQA3;
    boolean check1 = true, check2 = true, check3 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_and_answer);
        ORM();
        onBack();
        HandlerClickQA1();
        HandlerClickQA2();
        HandlerClickA3();
    }

    private void onBack() {
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void HandlerClickA3() {
        btnQA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check3) {
                    check3 = openContent(txtQA3, imgQA3);
                } else {
                    check3 = closeContent(txtQA3, imgQA3);
                }
            }
        });
    }

    private boolean openContent(TextView txtQA, ImageView imgQA) {
        txtQA.setVisibility(View.VISIBLE);
        imgQA.setImageResource(R.drawable.ic_down);
        return false;
    }

    private boolean closeContent(TextView txtQA, ImageView imgQA) {
        txtQA.setVisibility(View.GONE);
        imgQA.setImageResource(R.drawable.ic_top);
        return true;
    }

    private void HandlerClickQA2() {
        btnQA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check2) {
                    check2 = openContent(txtQA2, imgQA2);
                } else {
                    check2 = closeContent(txtQA2, imgQA2);
                }
            }
        });
    }

    private void HandlerClickQA1() {
        btnQA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1) {
                    check1 = openContent(txtQA1, imgQA1);
                } else {
                    check1 = closeContent(txtQA1, imgQA1);
                }
            }
        });
    }


    private void ORM() {
        txtQA1 = findViewById(R.id.txt_QA1);
        txtQA2 = findViewById(R.id.txt_QA2);
        txtQA3 = findViewById(R.id.txt_QA3);
        btnQA1 = findViewById(R.id.btn_QA1);
        btnQA2 = findViewById(R.id.btn_QA2);
        btnQA3 = findViewById(R.id.btn_QA3);
        imgQA1 = findViewById(R.id.img_QA1);
        imgQA2 = findViewById(R.id.img_QA2);
        imgQA3 = findViewById(R.id.img_QA3);

        back = findViewById(R.id.btn_back);
        layoutBack = findViewById(R.id.btn_layout_back);
    }
}