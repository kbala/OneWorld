package com.laundryfy.oneworld;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by sabari on 4/21/2015.
 */
public class AppController {

    private static ProgressDialog progress;

    public static void InitializeProgressDialog(Activity activity, String message)
    {
        progress = new ProgressDialog(activity);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
    }

    public static void ShowProgressDialog()
    {
        progress.show();
    }

    public static void HideProgressDialog()
    {
        progress.dismiss();
    }
}
