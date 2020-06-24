package com.miyako.ticketunion.utils;

import android.widget.Toast;

import com.miyako.ticketunion.base.MyApplication;

public class ToastUtils {

    private static Toast sToast;

    public static void showToast(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(MyApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }

        sToast.show();
    }
}
