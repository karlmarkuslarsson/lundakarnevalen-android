        package fragments;

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

    public Messanger messanger;

    /**
     * Gets the application context
     *
     * @return The context.
     */
    public Context getContext() {
        return super.getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            messanger = (Messanger) activity;
        } catch (ClassCastException e) {
            Log.wtf(LOG_TAG, "could not get messanger");
        }
        Log.d(LOG_TAG, "Got messanger");
    }

    /**
     * Set the Titlebar title
     */

    public void setTitle(String title) {
        Bundle data = new Bundle();
        data.putString("title", title);
        messanger.message(MessangerMessage.SET_TITLE, data);
    }

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


    /**
     * Show or hide actionbar logo.
     *
     * @param show
     *            If true the logo will be shown, if false the text will be
     *            shown.
     */
    public void showActionBarLogo(boolean show) {
        Bundle data = new Bundle();
        data.putBoolean("show", show);
        messanger.message(MessangerMessage.SHOW_ACTION_BAR_LOGO, data);
    }

    /**
     * Loads fragment into framelayout.
     *
     * @param fragment
     *            The fragment to launch h
     * @param addToBackstack
     *            If true it will be added to backstack on launch.
     */
    public void loadFragment(Fragment fragment, boolean addToBackstack) {
        Log.d(LOG_TAG, "messenger was"
                + ((messanger == null) ? "null" : "not null"));
        messanger.loadFragment(fragment, addToBackstack);
    }

    /**
     *
     * @see fragments.LKFragment.Messanger#popFragmentStack()
     */
    public void popFragmentStack() {
        messanger.popFragmentStack();
    }



    /**
     * Interface to be implemented by activity containing the fragment.
     *
     *
     */
    public interface Messanger {
        public void message(MessangerMessage message, Bundle data);

        public void loadFragment(Fragment fragment, boolean addToBackstack);
        public void popFragmentStack();
    }

    /**
     * Enum for different types of messages.
     *
     *
     */
    public enum MessangerMessage {
        SET_TITLE, SET_INBOX_COUNT, SHOW_ACTION_BAR_LOGO
    }




}