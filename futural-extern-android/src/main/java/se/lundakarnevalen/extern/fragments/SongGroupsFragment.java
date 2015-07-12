package fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.fragments.LKFragment;
import se.lundakarnevalen.extern.fragments.SongsPagerFragment;

public class SongGroupsFragment extends LKFragment {
	
	public static SongGroupsFragment newInstance(){
		return new SongGroupsFragment();
	}
	
	private ViewGroup mLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sangbok_groups_layout, container, false);
		List<SongGroup> groups = new ArrayList<SongGroupsFragment.SongGroup>();
		
		mLayout = get(R.id.sangbok_groups_layout, root, ViewGroup.class);
		
		makeGroup(groups, "KARNEVALSMELODIN", R.drawable.songs_karnmel_box, R.drawable.songs_karnmel_shadow, R.array.sangbok_songs_group_karnmel);
		makeGroup(groups, "ALKOHOLFRIA VISOR", R.drawable.songs_noacl_box, R.drawable.songs_noacl_shadow, R.array.sangbok_songs_group_noacl);
		makeGroup(groups, "PUNSCHVISOR",R.drawable.songs_punch_box, R.drawable.songs_punch_shadow, R.array.sangbok_songs_group_punsch);
		makeGroup(groups, "SNAPSVISOR", R.drawable.songs_snaps_box, R.drawable.songs_snaps_shadow, R.array.sangbok_songs_group_snaps);
		makeGroup(groups, "VINVISOR", R.drawable.songs_wine_box, R.drawable.songs_wine_shadow, R.array.sangbok_songs_group_wine);
		makeGroup(groups, "ÖLVISOR", R.drawable.songs_ol_box, R.drawable.songs_ol_shadow, R.array.sangbok_songs_group_ol);
		makeGroup(groups, "ÖVRIGA VISOR", R.drawable.songs_other_box, R.drawable.songs_other_shadow, R.array.sangbok_songs_group_other);
		
		for(final SongGroup sg: groups){
			createHeader(inflater, mLayout, sg);
			for (int i = 0; i < sg.songs.length; i++) {
				final int songIdx = i;
				createSong(inflater, mLayout, sg, songIdx, new OnClickListener() {
					@Override
					public void onClick(View v) {
						openSongGroup(sg, songIdx);
					}

				});
			}
		}
		return root;
	}

	private void  makeGroup(Collection<SongGroup> list, String name, int boxId, int boxShadowId, int songsArrayId) {
		SongGroup sg = new SongGroup();
		sg.name = name;
		sg.imageBox = boxId;
		sg.imageShadow = boxShadowId;
		readGroup(sg, songsArrayId);
		list.add(sg);
	}

	private void readGroup(SongGroup sg1, int arrayId) {
		sg1.songArrayId = arrayId;
		String[] karnmel = getResources().getStringArray(arrayId);
		sg1.songNumbers = new int[karnmel.length];
		sg1.songs = new String[karnmel.length];
		for (int i = 0; i < karnmel.length; i++) { 
			String song = karnmel[i]; 
			String[] split = song.split("\\|", 4);
			sg1.songNumbers[i] = Integer.parseInt(split[0]);
			sg1.songs[i] = split[1].toUpperCase(Locale.getDefault());
		}
	}

	private void openSongGroup(SongGroup sg, int selected) {
		SongsPagerFragment.newInstance(sg.songArrayId, sg.imageBox, sg.name, selected);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	private static class SongGroup {
		String name;
		int imageBox;
		int imageShadow;
		int songArrayId;
		String[] songs;
		int[] songNumbers;
		
	}


	private void createSong(LayoutInflater inflater, ViewGroup layout, SongGroup sg, int i, OnClickListener l) {
		View t = inflater.inflate(R.layout.sangbok_groups_inflated_groupitem, layout, false);
		get(R.id.sangbok_groups_inflated_groupitem_text, t, TextView.class).setOnClickListener(l);
		get(R.id.sangbok_groups_inflated_groupitem_text, t, TextView.class).setText(Html.fromHtml(sg.songs[i]));
		get(R.id.sangbok_groups_inflated_groupitem_nbr, t, TextView.class).setText(String.valueOf(sg.songNumbers[i]));
		layout.addView(t);
	}

	private void createHeader(LayoutInflater inflater, ViewGroup layout, SongGroup sg) {
		View h = inflater.inflate(R.layout.sangbok_groups_inflated_groupheader, layout, false);
		get(R.id.sangbok_groups_inflated_groupheader_text, h, TextView.class).setBackgroundResource(sg.imageBox);
		get(R.id.sangbok_groups_inflated_groupheader_text, h, TextView.class).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, sg.imageShadow);
		get(R.id.sangbok_groups_inflated_groupheader_text, h, TextView.class).setText(sg.name);
		layout.addView(h);
	}
}