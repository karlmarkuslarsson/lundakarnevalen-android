package se.lundakarnevalen.extern.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.util.SparseArray;

public class SoundFactory implements OnAudioFocusChangeListener {

	private SparseArray<LongSound> sparseArrayMediaPlayers;
	private Context context;
	
	public SoundFactory(Context context) {
		this.context = context;

		sparseArrayMediaPlayers = new SparseArray<>();
	}

	public boolean resume(int resourceID) {
		LongSound media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.resume();
		return true;
	}

	public boolean pause(int resourceID) {
		LongSound media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.pause();
		return true;
	}

	public boolean start(int resourceID) {
		LongSound media = sparseArrayMediaPlayers.get(resourceID);
		
		if(media == null) {
			return false;
		}
		
		media.start();
		return true;
	}
	
	public void createLongMedia(int resourceID, boolean looping) {
		LongSound media = new LongSound(context, resourceID, looping);
		sparseArrayMediaPlayers.put(resourceID, media);
	}
	
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

	public void stopAll() {
		for(int i = 0; i < sparseArrayMediaPlayers.size(); i++) {
			sparseArrayMediaPlayers.valueAt(i).stop();
		}
	}
}
