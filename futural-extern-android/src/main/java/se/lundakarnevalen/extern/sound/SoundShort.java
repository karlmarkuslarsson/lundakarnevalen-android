package se.lundakarnevalen.extern.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundShort {

	private SoundPool pool;
	private Context context;
	private SparseIntArray sparseIntArraySoundPool;
	private float volume;
	
	protected SoundShort(Context context) {		
		pool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		sparseIntArraySoundPool = new SparseIntArray();
		this.context = context;
					
		float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		volume = actualVolume / maxVolume;
		
		loadSounds();
	}

	protected void play(int resourceID) {
		Integer id = sparseIntArraySoundPool.get(resourceID);
		
		if(id == null) {
			return;
		}
		pool.play(id, volume, volume, 1, 0, 1f);
	}
	
	private void addResourceID(int resourceID) {
		int id = pool.load(context, resourceID, 1);
		sparseIntArraySoundPool.put(resourceID, id);
	}
	
	private void loadSounds() {	
		//addResourceID(R.raw.shot);
	}
}
