package se.lundakarnevalen.extern.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSwipeableViewPager;

public class SongsPagerFragment extends LKFragment {
	
	private static final int PAGE_MARGIN_DP = 20;
	
	private static final String KEY_GROUP_NAME = "group";
	private static final String KEY_ARRAY = "array";
	private static final String KEY_ICON = "icon";
	private static final String KEY_SELECTED = "selected";

	public static SongsPagerFragment newInstance(int songsArrayId, int icon, String groupName ,int selceted) {
		SongsPagerFragment f = new SongsPagerFragment();
		Bundle b = new Bundle();
		b.putInt(KEY_ARRAY, songsArrayId);
		b.putInt(KEY_ICON, icon);
		b.putInt(KEY_SELECTED, selceted);
		b.putString(KEY_GROUP_NAME, groupName);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sangbok_songs_layout, container, false);
		
		Bundle b = (getArguments() != null) ? getArguments() : new Bundle();
		
		int arrayId = b.getInt(KEY_ARRAY, R.array.sangbok_songs_group_karnmel);
		int iconId = b.getInt(KEY_ICON, R.drawable.kommunikation);
		int selected = b.getInt(KEY_SELECTED,0);
		String[] songs = inflater.getContext().getResources().getStringArray(arrayId);
		boolean hasMultiple = songs.length > 1;
		
		final Context c = inflater.getContext();
		final FragmentManager fm = getChildFragmentManager();
		int pageMargin = Math.round(LKFragment.dpToPx(PAGE_MARGIN_DP, c));

		LKSwipeableViewPager mViewPager = get(R.id.sangbok_layout_viewpager, root, LKSwipeableViewPager.class);
		mViewPager.setAdapter(new MyAdapter(fm, c, songs, iconId));
		mViewPager.setPageMargin(pageMargin);
		mViewPager.setClipToPadding(false);
		mViewPager.setCurrentItem(selected); 
		mViewPager.setPagingEnabled(hasMultiple);
		
		if(!hasMultiple){
			int w = container.getWidth();
			int padding = (int) (w * (1.0 - 0.93));
			Log.d("Songpager", "Padding: "+ padding + " Widht: "+ w + " PageMargin: " + pageMargin);
			mViewPager.setPadding(padding, mViewPager.getPaddingTop(), padding - pageMargin, mViewPager.getPaddingBottom()); // Fix equal margins/paddings if only one element
		}
		
		get(R.id.sangbok_layout_backbtn, root, Button.class).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// @TODO GO back
			}
		});
		return root;
	}
	
	private static class MyAdapter extends FragmentPagerAdapter {
		private final String[] songs;
		private final int icon;
        
		public MyAdapter(FragmentManager fm, Context c, String[] songs, int iconDrawable) {
            super(fm);
            this.songs = songs;
            this.icon = iconDrawable;
        }

        @Override
        public int getCount() {
        	return songs.length;
        }

        @Override
        public Fragment getItem(int position) {
        	String[] split = songs[position].split("\\|", 4);
    		String title = split[1];
    		String subtitle = split[2];
    		String text = split[3];
    		return ShowSongFragment.newInstance(title, subtitle, text, icon);
        }
        
        @Override
        public float getPageWidth (int position)
    	{
    		return 0.93f;
    	}
    }
	
	public static class ShowSongFragment extends Fragment {
		private static final String KEY_SUBTITLE = "subtitle";
		private static final String KEY_TITLE = "title";
		private static final String KEY_TEXT = "text";
		private static final String KEY_ICON = "icon";
		
		public static ShowSongFragment newInstance(String title, String subtitle, String text, int icon){
			ShowSongFragment f = new ShowSongFragment();
			Bundle b = new Bundle();
			b.putString(KEY_TITLE, title);
			b.putString(KEY_TEXT, text);
			b.putString(KEY_SUBTITLE, subtitle);
			b.putInt(KEY_ICON, icon);
			f.setArguments(b);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sangbok_songs_inflated_song_layout, container, false);
			Bundle b = getArguments();
			get(R.id.sangbok_layout_song_layout_title, root, TextView.class).setText(Html.fromHtml(b.getString(KEY_TITLE)));
			get(R.id.sangbok_layout_song_layout_title, root, TextView.class).setSelected(b.getString(KEY_TITLE).length() > 24); //make it scroll if too long
			get(R.id.sangbok_layout_song_layout_subtitle, root, TextView.class).setText(Html.fromHtml(b.getString(KEY_SUBTITLE)));
			get(R.id.sangbok_layout_song_layout_subtitle, root, TextView.class).setSelected(b.getString(KEY_SUBTITLE).length() > 20); //make it scroll if too long
			get(R.id.sangbok_layout_song_layout_text, root, TextView.class).setText(Html.fromHtml(b.getString(KEY_TEXT)));
			
			return root;
		}
	}
}