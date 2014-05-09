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
    private final static String LOG_TAG = LKFragment.class.getSimpleName();
    private static final boolean DEBUG_LIFECYCLE = true;

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
        if(DEBUG_LIFECYCLE)Logf.d(this, "onAttach(): Free mem: %d MB", onAttachMem);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onDetach(): Free mem: %d MB", getMemUsage());
    }

    @Override
    public void onResume() {
        super.onResume();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onResume(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onStart() {
        super.onStart();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onStart(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onStop() {
        super.onStop();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onStop(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onPause() {
        super.onPause();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onPause(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onDestroy(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onDestroyView(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onActivityCreated(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int usage = getMemUsage();
        if(DEBUG_LIFECYCLE)Logf.d(this, "onViewCreated(): Free mem: %d MB (since onAttach: %d MB)", usage, onAttachMem - usage);
    }

    public Context getContext(){
        return getActivity();
    }
}