package se.lundakarnevalen.extern.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class LKFragment extends Fragment {
    protected final static String LOG_TAG = "LKFragment";

    public static String getAppVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        String version = "";
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            Log.wtf(LOG_TAG, "Could not get package info.");
        }
        try {
            version = info.versionName;
        } catch (NullPointerException e) {
            return "";
        }
        return version;
    }
}