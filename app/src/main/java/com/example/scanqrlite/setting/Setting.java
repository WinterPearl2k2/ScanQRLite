package com.example.scanqrlite.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Vibrator;
import android.text.Html;
import android.util.DisplayMetrics;
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
import com.example.scanqrlite.setting.settingitem.QuestionAndAnswer;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.Locale;

public class Setting extends Fragment {
    private SwitchCompat swBeep, swVibrite, swClipboard;
    private LinearLayout btnBeep, btnVibrite, btnClipboard, btnQA, btnChangeLanguage, btnFeedback, btnRate, btnVersion;
    private ImageButton btnClickX;
    private RadioButton itemEnglish, itemFrance, itemChina, itemGermany, itemKorea, itemVietnam;
    private TextView txtLanguage, txtVersion;
    private ScaleRatingBar rtRating;
    private TextView btnRatingClose, btnRatingVote, btnRatingFeedback,
            btnRatingVoteAgain, btnRatingOpenChplay, txtTitleRating;
    private TextView txtContentFeedback, btnFeedbackYes, btnFeedbackNo;
    private ImageView imgRating;
    View view;
    AdView adsViewSetting;
    AdRequest adRequest;
    AdLoader adLoader;

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
                    txtTitleRating.setText("Thank you for voting for the app");
                    btnRatingClose.setVisibility(View.VISIBLE);
                    btnRatingOpenChplay.setVisibility(View.VISIBLE);
                    btnRatingFeedback.setVisibility(View.GONE);
                    btnRatingVote.setVisibility(View.GONE);
                    btnRatingOpenChplay.setText("OpenCHplay");
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
                    txtTitleRating.setText("Report bugs and let us know what needs improvement");
                    btnRatingVoteAgain.setVisibility(View.VISIBLE);
                    btnRatingClose.setVisibility(View.GONE);
                    btnRatingVoteAgain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rtRating.setRating(3f);
                            imgRating.setImageResource(R.drawable.img_smile);
                            txtTitleRating.setText("Ratings to boost your app rankings on the app market");
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
            txtContentFeedback.setText(Html.fromHtml(
                    "<p>Many questions have been answered in \"Question & Answer\"." +
                            "<br>If possible, take or attach a barcode or screenshot.</p>"
                    , Html.FROM_HTML_MODE_COMPACT));
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
                submitFeedback();
            }
        });
    }

    public void submitFeedback(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//        emailIntent.setType("text/email");
        String EmailList = "nnhuquynh2603@gmail.com";

        String feedback_title = " Scan QR Lite Feedback ";
        emailIntent.setData(Uri.parse("mailto:" + EmailList + "?subject=" + feedback_title));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, EmailList);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, feedback_title);

        try{
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getActivity(), "Email not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void QA() {
        btnQA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment2 = new QuestionAndAnswer();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFrame, fragment2);
                fragmentTransaction.commit();
            }
        });
    }

    private void ChangeLanguage() {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(getActivity());
        SharedPreferences preferences = getActivity().getSharedPreferences("language", Context.MODE_PRIVATE);
        View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_language, null);
        String Language = preferences.getString("language", "English");

        itemEnglish = view1.findViewById(R.id.item_enlish);
        itemFrance = view1.findViewById(R.id.item_france);
        itemGermany = view1.findViewById(R.id.item_germany);
        itemChina = view1.findViewById(R.id.item_china);
        itemVietnam = view1.findViewById(R.id.item_vietnam);
        itemKorea = view1.findViewById(R.id.item_korea);

        switch (Language) {
            case "English":
                txtLanguage.setText("English");
                itemEnglish.setChecked(true);
                break;
            case "France":
                txtLanguage.setText("France");
                itemFrance.setChecked(true);
                break;
            case "Korea":
                txtLanguage.setText("Korea");
                itemKorea.setChecked(true);
                break;
            case "Vietnam":
                txtLanguage.setText("Vietnam");
                itemVietnam.setChecked(true);
                break;
            case "Germany":
                txtLanguage.setText("Germany");
                itemGermany.setChecked(true);
                break;
            case "China":
                txtLanguage.setText("China");
                itemChina.setChecked(true);
                break;
            default:
                txtLanguage.setText("English");
                itemEnglish.setChecked(true);
        }

        btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLanguage(preferences, sheetDialog, view1);
            }
        });
    }

    private void CheckLanguage(SharedPreferences preferences, BottomSheetDialog sheetDialog, View view){
        SharedPreferences.Editor editor = preferences.edit();
        sheetDialog.setContentView(view);
        RadioGroup listItemLanguage = sheetDialog.findViewById(R.id.list_item_language);
        listItemLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.item_enlish:
                        editor.putString("language","English");
                        txtLanguage.setText("English");
                        break;
                    case R.id.item_france:
                        editor.putString("language","France");
                        txtLanguage.setText("France");
                        break;
                    case R.id.item_germany:
                        editor.putString("language","Germany");
                        txtLanguage.setText("Germany");
                        break;
                    case R.id.item_china:
                        editor.putString("language","China");
                        txtLanguage.setText("China");
                        break;
                    case R.id.item_korea:
                        editor.putString("language","Korea");
                        txtLanguage.setText("Korea");
                        break;
                    case R.id.item_vietnam:
                        editor.putString("language","Vietnam");
                        txtLanguage.setText("Vietnam");
                        break;
                }
                editor.commit();
            }
        });
        sheetDialog.show();
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
        boolean check = vibrite.getBoolean("vabrate", false);
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
        }
        editor.commit();
    }

    private void Version() {
        txtVersion.setText("Version Lite: " + BuildConfig.VERSION_NAME);
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