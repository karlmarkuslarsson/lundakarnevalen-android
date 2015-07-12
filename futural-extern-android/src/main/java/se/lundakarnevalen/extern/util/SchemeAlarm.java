package se.lundakarnevalen.extern.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-05-05.
 */
public class SchemeAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.splash, "LUNDAKARNEVALEN",
                System.currentTimeMillis());
        // The PendingIntent to launch our activity if the user selects this notification
        // TODO Bitmap crash!
        Intent i = new Intent(context, ContentActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, i, 0);
        // Set the info for the views that show in the notification panel.

        notification.setLatestEventInfo(context, intent.getStringExtra("Title"), intent.getStringExtra("Desc"), contentIntent);
        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.futufutu);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to cancel.
        mNM.notify(intent.getStringExtra("Title") + intent.getStringExtra("Desc"),0, notification);
    }
}