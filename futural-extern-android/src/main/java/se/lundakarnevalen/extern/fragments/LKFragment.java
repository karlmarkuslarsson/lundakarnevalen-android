package se.lundakarnevalen.extern.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import se.lundakarnevalen.extern.util.Logf;

public class LKFragment extends Fragment {
    protected final static String LOG_TAG = "LKFragment";
    private int onAttachMem;

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

    public int getMemUsage(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return (int) (mi.availMem / 1048576L);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onAttachMem = getMemUsage();
        Logf.d(this, "onAttach(): Free mem: %d MB", onAttachMem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logf.d(this, "onDetach(): Free mem: %d MB", getMemUsage());
    }

    @Override
    public void onResume() {
        super.onResume();
        int usage = getMemUsage();
        Logf.d(this, "onResume(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    public Context getContext(){
        return getActivity();
    }
}