package com.example.scanqrlite;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class TextColorStatusbar {
    int type;
    Activity activity;

    public TextColorStatusbar(Activity activity) {
        this.activity = activity;
    }

    public void changeColor(int type) {
        if(type == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                & ~ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if(type == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if(type == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }
}
