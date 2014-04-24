package se.lundakarnevalen.extern.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.util.SparseArray;

public class SoundFactory implements OnAudioFocusChangeListener {

	private SparseArray<SoundLong> sparseArrayMediaPlayers;
	private Context context;
	private static SoundShort shortMedia;
	private boolean wakeLock;
	
	public SoundFactory(Context context) {
		this.context = context;

		sparseArrayMediaPlayers = new SparseArray<SoundLong>();
		shortMedia = new SoundShort(context);
	}
	
	/**
	 * Resume a song from a specific resourceID
	 * @param resourceID
	 * @return true if successful, false if that resourceID wasn't found
	 */
	public boolean resume(int resourceID) {
		SoundLong media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.resume();
		return true;
	}
	
	/**
	 * Pause a song from a specific resourceID
	 * @param resourceID
	 * @return true if successful, false if that resourceID wasn't found
	 */
	public boolean pause(int resourceID) {
		SoundLong media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.pause();
		return true;
	}
	
	/**
	 * Start a song from a specific resourceID
	 * @param resourceID
	 * @return true if successful, false if that resourceID wasn't found
	 */
	public boolean start(int resourceID) {
		SoundLong media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.start();
		return true;
	}
	
	public void createLongMediaAndStart(int resourceID, boolean looping, boolean aquireWakelock) {
		createLongMedia(resourceID, looping, aquireWakelock);
		start(resourceID);
	}
	
	public void createLongMedia(int resourceID, boolean looping, boolean aquireWakelock) {
		SoundLong media = new SoundLong(context, resourceID, looping, aquireWakelock);
		sparseArrayMediaPlayers.put(resourceID, media);
	}
	
	public static void playShortMedia(int resourceID) {
		shortMedia.play(resourceID);
	}
	
	public boolean setLooping(int resourceID, boolean looping) {
		SoundLong media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.setLooping(looping);
		return true;
	}
	
//	External Interupts
	
	@Override
	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
        	case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
        		gainedFocus();
        		break;
        	
        	case AudioManager.AUDIOFOCUS_LOSS:
        		// Lost focus for an unbounded amount of time: stop playback and release media player
        		lostFocusLong();
        		break;
        	
        	case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        		// Lost focus for a short time, but we have to stop playback.
        		lostFocusShort();
        		break;
        		
        	case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing at an attenuated level
        		lostFocusDuck();
        		break;
		}
	}
	
//	Handle interrupt Events
	
	private void gainedFocus() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).gainedFocus();
		}
	}
	
	private void lostFocusLong() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).lostFocusLong();
		}
	}
	
	private void lostFocusShort() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).lostFocusShort();
		}
	}

	private void lostFocusDuck() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).lostFocusDuck();
		}
	}

//	Handle music play and pauses
	
	public void pauseAll() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).pause();
		}
	}

	public void resumeAll() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).resume();
		}
	}

	public void stopAll() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).stop();
		}
	}
}
