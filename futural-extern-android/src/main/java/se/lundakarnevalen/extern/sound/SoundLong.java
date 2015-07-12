package se.lundakarnevalen.extern.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.PowerManager;
import android.util.Log;

public class SoundLong {

	private static final String TAG = SoundLong.class.getSimpleName();
	
	private MediaPlayer mediaPlayer;
	private int resourceId;
	private Context context;
	private boolean looping;
	private boolean wakelock;
	private boolean started;
	private boolean prepared;
	
	protected SoundLong(Context context, int resourceId, boolean looping, boolean wakelock) {
		this.resourceId = resourceId;
		this.context = context;
		this.looping = looping;
		this.wakelock = wakelock;
		
		prepared = false;
		started = false;
		
		initMediaPlayer();
	}
	
	public boolean isPlaying() {
		if(mediaPlayer == null) {
			return false;
		}
		return mediaPlayer.isPlaying();
	}
	
	public void resume() {
		if(!started) {
			return;
		}
		start();
	}
	
	public void setLooping(boolean looping) {
		if(mediaPlayer == null) {
			return;
		}
		
		mediaPlayer.setLooping(looping);
	}
	
	public void start() {
		Log.d(TAG, "Playing media recource: " + resourceId);
		
		started = true;
		mediaPlayer.start();
	}

	public void pause() {
		Log.d(TAG, "Pausing media recource: " + resourceId);
		mediaPlayer.pause();
	}
	
	public void stop() {
		Log.d(TAG, "Stopping media recource: " + resourceId);
		started = false;
		mediaPlayer.stop();
	}

	public void release() {
		Log.d(TAG, "Release media recource: " + resourceId);
		mediaPlayer.release();
		mediaPlayer = null;
		started = false;
	}

	public void gainedFocus() {
		if (mediaPlayer == null) { 
			initMediaPlayer();
		} else if (!isPlaying()) { 
			start();
		}
		
		setVolume(1.0f, 1.0f);
	}
	
	public void lostFocusLong() {
        if (isPlaying()) {
        	stop();
        }
        
        release();
	}

	public void lostFocusShort() {
        if (isPlaying()) {
        	pause();
        }
	}

	public void lostFocusDuck() {
        if (isPlaying()) {
        	setVolume(0.1f, 0.1f);
        }
	}
	
//	Private Methods
	
	private void setVolume(float low, float high) {
        mediaPlayer.setVolume(1.0f, 1.0f);
	}

	private void initMediaPlayer() {
		mediaPlayer = MediaPlayer.create(context, resourceId);
		
		if(wakelock) {
			mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);	
		}
		
		mediaPlayer.setOnPreparedListener(new ReadyListener());
		mediaPlayer.setOnErrorListener(new ErrorListener());
		mediaPlayer.prepareAsync();
		mediaPlayer.setLooping(looping);
	}
	
	private class ErrorListener implements OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}
	}
	
	private class ReadyListener implements OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			if(started) {
				mp.start();
			}
			mediaPlayer = mp;
		}
	}
}
