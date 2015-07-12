package se.lundakarnevalen.extern.sound;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class LongSound {

	private static final String TAG = LongSound.class.getSimpleName();
	
	private MediaPlayer mediaPlayer;
	private int resourceId;
	private Context context;
	private Boolean looping;
	private boolean started;
	
	protected LongSound(Context context, int resourceId, boolean looping) {
		this.resourceId = resourceId;
		this.context = context;
		this.looping = looping;
		
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

	private void setVolume(float low, float high) {
        mediaPlayer.setVolume(1.0f, 1.0f);
	}

	private void initMediaPlayer() {
		mediaPlayer = MediaPlayer.create(context, resourceId);
		mediaPlayer.setLooping(looping);
	}
}
