package com.example.scanqrlite.setting.settingitem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scanqrlite.R;

public class QuestionAnswer extends AppCompatActivity {
    LinearLayout layoutBack;
    ImageButton back;
    TextView txtQA1, txtQA2, txtQA3;
    LinearLayout btnQA1, btnQA2, btnQA3;
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
                    txtQA3.setVisibility(View.VISIBLE);
                    imgQA3.setImageResource(R.drawable.ic_down);
                    check3 = false;
                } else {
                    txtQA3.setVisibility(View.GONE);
                    imgQA3.setImageResource(R.drawable.ic_top);
                    check3 = true;
                }
            }
        });
        imgQA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check3) {
                    txtQA3.setVisibility(View.VISIBLE);
                    imgQA3.setImageResource(R.drawable.ic_down);
                    check3 = false;
                } else {
                    txtQA3.setVisibility(View.GONE);
                    imgQA3.setImageResource(R.drawable.ic_top);
                    check3 = true;
                }
            }
        });
    }

    private void HandlerClickQA2() {
        btnQA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check2) {
                    txtQA2.setVisibility(View.VISIBLE);
                    imgQA2.setImageResource(R.drawable.ic_down);
                    check2 = false;
                } else {
                    txtQA2.setVisibility(View.GONE);
                    imgQA2.setImageResource(R.drawable.ic_top);
                    check2 = true;
                }
            }
        });
        imgQA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check2) {
                    txtQA2.setVisibility(View.VISIBLE);
                    imgQA2.setImageResource(R.drawable.ic_down);
                    check2 = false;
                } else {
                    txtQA2.setVisibility(View.GONE);
                    imgQA2.setImageResource(R.drawable.ic_top);
                    check2 = true;
                }
            }
        });
    }

    private void HandlerClickQA1() {
        btnQA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1) {
                    txtQA1.setVisibility(View.VISIBLE);
                    imgQA1.setImageResource(R.drawable.ic_down);
                    check1 = false;
                } else {
                    txtQA1.setVisibility(View.GONE);
                    imgQA1.setImageResource(R.drawable.ic_top);
                    check1 = true;
                }
            }
        });
        imgQA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1) {
                    txtQA1.setVisibility(View.VISIBLE);
                    imgQA1.setImageResource(R.drawable.ic_down);
                    check1 = false;
                } else {
                    txtQA1.setVisibility(View.GONE);
                    imgQA1.setImageResource(R.drawable.ic_top);
                    check1 = true;
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