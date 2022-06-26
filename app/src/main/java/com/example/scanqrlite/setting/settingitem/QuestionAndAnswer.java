package com.example.scanqrlite.setting.settingitem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.scanqrlite.R;
import com.example.scanqrlite.setting.Setting;

public class QuestionAndAnswer extends Fragment {
    ImageButton back;
    TextView txtQA1, txtQA2, txtQA3;
    LinearLayout btnQA1, btnQA2, btnQA3;
    ImageView imgQA1, imgQA2, imgQA3;
    boolean check1 = true, check2 = true, check3 = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_and_answer, container, false);
        back = (ImageButton) view.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        });
        ORM(view);
        HandlerClickQA1();
        HandlerClickQA2();
        HandlerClickA3();
        return view;
    }

    private void HandlerClickA3() {
        btnQA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check3) {
                    txtQA3.setVisibility(View.VISIBLE);
                    imgQA3.setImageResource(R.drawable.ic_top);
                    check3 = false;
                } else {
                    txtQA3.setVisibility(View.GONE);
                    imgQA3.setImageResource(R.drawable.ic_down);
                    check3 = true;
                }
            }
        });
        imgQA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check3) {
                    txtQA3.setVisibility(View.VISIBLE);
                    imgQA3.setImageResource(R.drawable.ic_top);
                    check3 = false;
                } else {
                    txtQA3.setVisibility(View.GONE);
                    imgQA3.setImageResource(R.drawable.ic_down);
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
                    imgQA2.setImageResource(R.drawable.ic_top);
                    check2 = false;
                } else {
                    txtQA2.setVisibility(View.GONE);
                    imgQA2.setImageResource(R.drawable.ic_down);
                    check2 = true;
                }
            }
        });
        imgQA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(check2) {
                    txtQA2.setVisibility(View.VISIBLE);
                    imgQA2.setImageResource(R.drawable.ic_top);
                    check2 = false;
                } else {
                    txtQA2.setVisibility(View.GONE);
                    imgQA2.setImageResource(R.drawable.ic_down);
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
                    imgQA1.setImageResource(R.drawable.ic_top);
                    check1 = false;
                } else {
                    txtQA1.setVisibility(View.GONE);
                    imgQA1.setImageResource(R.drawable.ic_down);
                    check1 = true;
                }
            }
        });
        imgQA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check1) {
                    txtQA1.setVisibility(View.VISIBLE);
                    imgQA1.setImageResource(R.drawable.ic_top);
                    check1 = false;
                } else {
                    txtQA1.setVisibility(View.GONE);
                    imgQA1.setImageResource(R.drawable.ic_down);
                    check1 = true;
                }
            }
        });
    }

    private void ORM(View view) {
        txtQA1 = view.findViewById(R.id.txt_QA1);
        txtQA2 = view.findViewById(R.id.txt_QA2);
        txtQA3 = view.findViewById(R.id.txt_QA3);
        btnQA1 = view.findViewById(R.id.btn_QA1);
        btnQA2 = view.findViewById(R.id.btn_QA2);
        btnQA3 = view.findViewById(R.id.btn_QA3);
        imgQA1 = view.findViewById(R.id.img_QA1);
        imgQA2 = view.findViewById(R.id.img_QA2);
        imgQA3 = view.findViewById(R.id.img_QA3);
    }
}