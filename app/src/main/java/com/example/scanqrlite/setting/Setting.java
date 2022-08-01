package com.example.scanqrlite.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanqrlite.BuildConfig;
import com.example.scanqrlite.R;

import com.example.scanqrlite.setting.settingitem.QuestionAnswer;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.Locale;


public class Setting extends Fragment {
    private final int REQUEST_CODE =12;
    private SwitchCompat swBeep, swVibrite, swClipboard;
    RelativeLayout btnChangeLanguage;
    private LinearLayout btnBeep, btnVibrite, btnClipboard, btnQA, btnFeedback, btnRate, btnVersion;
    private ImageButton btnClickX;
    private RadioButton itemEnglish, itemChina, itemGermany, itemKorea, itemVietnam;
    private TextView txtLanguage, txtVersion;
    private ScaleRatingBar rtRating;
    private TextView btnRatingClose, btnRatingVote, btnRatingFeedback,
            btnRatingVoteAgain, btnRatingOpenChplay, txtTitleRating;
    private TextView txtContentFeedback, btnFeedbackYes, btnFeedbackNo;
    private ImageView imgRating;
    View view;
    AdView adsViewSetting;
    Locale locale;
    AdRequest adRequest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        ORM();
        Beep();
        Vibrate();
        CopyToClipboard();
        ChangeLanguage();
        QA();
        FeedBack();
        showAds();
        RateOnCHPlay();
        Version();
        return view;

    }


    private void showAds() {
        adRequest = new AdRequest.Builder().build();
        adsViewSetting.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if(adsViewSetting != null)
            adsViewSetting.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adsViewSetting != null)
            adsViewSetting.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adsViewSetting != null)
            adsViewSetting.resume();
    }

    private void RateOnCHPlay() {
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_rate);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.cus_dialog);
                ORMDialogRating(dialog);
                rtRating.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                        HandleRating(dialog);
                    }
                });
                btnClickX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                HandleRating(dialog);
                dialog.show();
            }
        });
    }

    private void HandleRating(Dialog dialog) {
        btnRatingVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rtRating.getRating() >= 5f) {
                    imgRating.setImageResource(R.drawable.img_love);
                    txtTitleRating.setText(R.string.thank_you);
                    btnRatingClose.setVisibility(View.VISIBLE);
                    btnRatingOpenChplay.setVisibility(View.VISIBLE);
                    btnRatingFeedback.setVisibility(View.GONE);
                    btnRatingVote.setVisibility(View.GONE);
                    btnRatingOpenChplay.setText(R.string.open_chplay);
                    btnRatingOpenChplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Writing something
                        }
                    });
                    rtRating.setScrollable(false);
                    rtRating.setClickable(false);
                } else if(rtRating.getRating() > 0f && rtRating.getRating() <= 4f) {
                    imgRating.setImageResource(R.drawable.img_sad);
                    txtTitleRating.setText(R.string.report_bug);
                    btnRatingVoteAgain.setVisibility(View.VISIBLE);
                    btnRatingClose.setVisibility(View.GONE);
                    btnRatingVoteAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rtRating.setRating(rtRating.getRating());
                            imgRating.setImageResource(R.drawable.img_smile);
                            txtTitleRating.setText(R.string.Ratings_to_boost);
                            btnRatingVoteAgain.setVisibility(View.GONE);
                            btnRatingClose.setVisibility(View.INVISIBLE);
                            btnRatingVote.setVisibility(View.VISIBLE);
                            btnRatingFeedback.setVisibility(View.GONE);
                            rtRating.setScrollable(true);
                            rtRating.setClickable(true);
                        }
                    });
                    btnRatingFeedback.setVisibility(View.VISIBLE);
                    btnRatingOpenChplay.setVisibility(View.GONE);
                    btnRatingVote.setVisibility(View.GONE);
                    btnRatingFeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            submitFeedback();
                        }
                    });
                    rtRating.setScrollable(false);
                    rtRating.setClickable(false);
                }
            }
        });
    }

    private void ORMDialogRating(Dialog dialog) {
        btnRatingClose = dialog.findViewById(R.id.btn_rating_close);
        btnRatingVote = dialog.findViewById(R.id.btn_rating_vote);
        btnRatingFeedback = dialog.findViewById(R.id.btn_rating_feedback);
        btnFeedback = dialog.findViewById(R.id.btn_feedback);
        btnRatingVoteAgain = dialog.findViewById(R.id.btn_rating_vote_again);
        btnRatingOpenChplay = dialog.findViewById(R.id.btn_rating_open_CHplay);
        rtRating = dialog.findViewById(R.id.rt_rating);
        imgRating = dialog.findViewById(R.id.img_rating);
        txtTitleRating = dialog.findViewById(R.id.txt_title_rating);
        btnClickX = dialog.findViewById(R.id.clickX_btn);
    }

    private void FeedBack() {
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_feedback);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.cus_dialog);
                ORMDialogFeedback(dialog);
                HandleFeedback(dialog);
                dialog.show();
            }
        });
    }
    private void ORMDialogFeedback(Dialog dialog) {
        txtContentFeedback = dialog.findViewById(R.id.txt_feedback);
        btnFeedbackYes = dialog.findViewById(R.id.btn_feedback_yes);
        btnFeedbackNo = dialog.findViewById(R.id.btn_feedback_no);
    }

    private void HandleFeedback(Dialog dialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtContentFeedback.setText(R.string.dialog_question_feedback_1);
        }
        btnFeedbackNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnFeedbackYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFeedbackYes.setClickable(false);
                submitFeedback();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE) {
            btnFeedbackYes.setClickable(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void submitFeedback(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setType("text/email");
        String EmailList = "nnhuquynh2603@gmail.com";
        emailIntent.setData(Uri.parse("mailto:" + EmailList + "?subject=" + getString(R.string.feedback_title)));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, EmailList);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.feedback_title);

        try{
            startActivityForResult(Intent.createChooser(emailIntent, getString(R.string.send_email)), REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getActivity(), getString(R.string.email_not_installed), Toast.LENGTH_SHORT).show();
        }


    }
    private void QA() {
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QuestionAnswer.class);
                startActivity(intent);
            }
        });
    }

    private void ChangeLanguage() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getActivity());
        SharedPreferences preferences = getContext().getSharedPreferences("language", Context.MODE_PRIVATE);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_language, null);
        String Language = preferences.getString("language", "English");
        Log.e("TAG1:",Language);


        itemEnglish = view1.findViewById(R.id.item_enlish);
        itemGermany = view1.findViewById(R.id.item_germany);
        itemChina = view1.findViewById(R.id.item_china);
        itemVietnam = view1.findViewById(R.id.item_vietnam);
        itemKorea = view1.findViewById(R.id.item_korea);

        switch (Language) {
            case "English":
                txtLanguage.setText(R.string.english);
                itemEnglish.setChecked(true);
                break;
            case "Korean":
                txtLanguage.setText(R.string.korean);
                itemKorea.setChecked(true);
                break;
            case "Vietnamese":
                txtLanguage.setText(R.string.vietnamese);
                itemVietnam.setChecked(true);
                break;
            case "German":
                txtLanguage.setText(R.string.german);
                itemGermany.setChecked(true);
                break;
            case "Chinese":
                txtLanguage.setText(R.string.chinese);
                itemChina.setChecked(true);
                break;
        }


        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLanguage(preferences, sheetDialog, view1);Log.e("TAG",txtLanguage.getText().toString());
            }
        });
    }

    private void CheckLanguage(SharedPreferences preferences, BottomSheetDialog sheetDialog, View view){
        SharedPreferences.Editor editor = preferences.edit();
        sheetDialog.setContentView(view);
        RadioGroup listItemLanguage = sheetDialog.findViewById(R.id.list_item_language);
        sheetDialog.show();
        listItemLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.item_enlish:
                        editor.putString("language","English");
                        locale = new Locale("en");
                        txtLanguage.setText("English");
                        break;
                    case R.id.item_germany:
                        editor.putString("language","German");
                        locale = new Locale("de");
                        txtLanguage.setText("German");
                        break;
                    case R.id.item_china:
                        editor.putString("language","Chinese");
                        locale = new Locale("zh");
                        txtLanguage.setText("Chinese");
                        break;
                    case R.id.item_korea:
                        editor.putString("language","Korean");
                        locale = new Locale("ko");
                        txtLanguage.setText("Korean");
                        break;
                    case R.id.item_vietnam:
                        editor.putString("language","Vietnamese");
                        locale = new Locale("vi");
                        txtLanguage.setText("Vietnamese");
                        break;
                }
                changeLanguage(locale);
                editor.commit();
                sheetDialog.dismiss();
            }
        });

    }
    private void changeLanguage(Locale locale) {
        Locale.setDefault(locale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        getActivity().recreate();
    }

    private void CopyToClipboard() {
        SharedPreferences clipboard = getActivity().getSharedPreferences("clipboard", Context.MODE_PRIVATE);
        boolean check = clipboard.getBoolean("clipboard", false);
        if(check) {
            swClipboard.setChecked(true);
        } else {
            swClipboard.setChecked(false);
        }
        btnClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swClipboard, clipboard, "clipboard");
            }
        });
        swClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swClipboard, clipboard, "clipboard");
            }
        });
    }

    public void Vibrate() {
        SharedPreferences vibrite = getActivity().getSharedPreferences("vibrate", Context.MODE_PRIVATE);
        boolean check = vibrite.getBoolean("vibrate", false);
        if(check) {
            swVibrite.setChecked(true);
        } else {
            swVibrite.setChecked(false);
        }
        btnVibrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swVibrite, vibrite, "vibrate");
            }
        });
        swVibrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swVibrite, vibrite, "vibrate");
            }
        });
    }



    public void Beep() {
        SharedPreferences beep = getActivity().getSharedPreferences("beep", Context.MODE_PRIVATE);
        boolean check = beep.getBoolean("beep", false);
        if(check) {
            swBeep.setChecked(true);
        } else {
            swBeep.setChecked(false);
        }
        btnBeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swBeep, beep, "beep");
            }
        });
        swBeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckSwitch(swBeep, beep, "beep");
            }
        });
    }

    private void CheckSwitch(SwitchCompat mSwitch, SharedPreferences preferences, String name) {
        SharedPreferences.Editor editor = preferences.edit();
        boolean check = preferences.getBoolean(name, false);
        if (check) {
            mSwitch.setChecked(false);
            editor.putBoolean(name, false);
        } else {
            mSwitch.setChecked(true);
            editor.putBoolean(name, true);
            if(name.equals("vibrate")) {
                Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
            } else if (name.equals("beep")) {
                final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 500);
                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            }
        }
        editor.commit();
    }

    private void Version() {
        txtVersion.setText( getText(R.string.version) + BuildConfig.VERSION_NAME);
    }

    private void ORM() {
        swBeep = view.findViewById(R.id.sw_beep);
        swVibrite = view.findViewById(R.id.sw_vibrite);
        swClipboard = view.findViewById(R.id.sw_clipboard);
        btnBeep = view.findViewById(R.id.btn_sw_beep);
        btnVibrite = view.findViewById(R.id.btn_sw_vibrite);
        btnClipboard = view.findViewById(R.id.btn_sw_clipboard);
        btnChangeLanguage = view.findViewById(R.id.btn_language);
        btnQA = view.findViewById(R.id.btn_QA);
        txtLanguage = view.findViewById(R.id.txt_language);
        btnFeedback = view.findViewById(R.id.btn_feedback);
        btnRate = view.findViewById(R.id.btn_rate);
        btnVersion = view.findViewById(R.id.verion);
        txtVersion = view.findViewById(R.id.txt_version);
        adsViewSetting = view.findViewById(R.id.adsViewSetting);
    }
}