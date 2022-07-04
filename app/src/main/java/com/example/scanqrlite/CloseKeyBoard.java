package com.example.scanqrlite;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class CloseKeyBoard implements View.OnTouchListener {
    Activity mActivity;

    public CloseKeyBoard(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void closeKB(Activity activity) {
        InputMethodManager im = (InputMethodManager)
                activity.getSystemService(INPUT_METHOD_SERVICE);
        if (im.isAcceptingText())
            im.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view instanceof ViewGroup) {
            closeKB(mActivity);
            return true;
        }
        return false;
    }
}
