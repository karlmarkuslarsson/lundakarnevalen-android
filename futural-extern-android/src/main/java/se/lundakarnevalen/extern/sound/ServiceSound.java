package se.lundakarnevalen.extern.sound;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;

public class ServiceSound extends Service {

	private static final String TAG = ServiceSound.class.getSimpleName();
	private SoundFactory factory;
	
	private int NOTIFICATION_ID = 10;
	private int songId = R.raw.lundakarneval;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		factory.start(songId);
	
		startForeground();
		
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		factory = new SoundFactory(this);
		factory.createLongMedia(R.raw.lundakarneval, true, true);
	}

	public IBinder onUnBind(Intent arg0) {
		// TO DO Auto-generated method
		return null;
	}
	
	public void onResume() {
		factory.resume(songId);
		startForeground();
	}

	public void onPause() {
		factory.pause(songId);
		stopForeground(true);
	}

	@Override
	public void onDestroy() {
		factory.stopAll();
		stopForeground(true);
	}

	@Override
	public void onLowMemory() {
		factory.stopAll();
	}
	
	private void startForeground() {
		String songName;
		// assign the song name to songName
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
		                new Intent(getApplicationContext(), ContentActivity.class),
		                PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();
		notification.tickerText = "Yo";
		notification.icon = R.drawable.fun_logo;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample", "Playing: " + "Karnevalen", pi);
		startForeground(NOTIFICATION_ID, notification);
	}
}
