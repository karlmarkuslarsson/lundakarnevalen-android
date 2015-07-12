package se.lundakarnevalen.extern.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.sound.SoundFactory;

@SuppressLint("DefaultLocale")
public class MelodyFragment extends LKFragment {
    private static SoundFactory factory;
    private static ImageView play;
    private static ImageView rewind;
    private static boolean playing;
    private static boolean started;
    private int songID = R.raw.lundakarneval;
    private static float tot;

    // Lyrics
    private static ImageView heart;
    private static int text = 0;
    private static long startTime;
    private static int totTime;
    private static TextView lyric1;
    private static TextView lyric2;
    private static TextView lyric3;
    private static final Handler handler = new Handler();
    private static final Handler moveHandler = new Handler();
    private static LyricsRunnable r;
    private static moveRunnable r2;
    private static long pauseTime = -1;
    private static float taken = 0;
    private int[] delays = { 11260, 3456, 2578, 4451, 4002, 3671, 3849, 2518,
            5784, 3684, 3656, 3707, 2263, 1786, 1929, 1735, 1976, 1770, 4701,
            1344, 1922, 3856, 1060, 2269, 3289, 2462, 2481, 3802, 2486, 3166,
            6320, 3723, 3775, 3809, 2234, 1858, 1920, 1828, 1980, 2055, 4088,
            1824, 1908, 4479, 1094, 1782, 3146, 14278, 4617, 1638, 1898, 1851,
            1985, 1554, 3977, 1863, 1855, 5567, 1994, 3678, 1970, 1859, 3603,
            1810, 2296, 3347, 1940, 2047, 3259, 2317 };
    // 38

    private String[] lyrics;
    private static int delay;
    private static ImageView mover;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       final View rootView = inflater.inflate(R.layout.fragment_melody, container, false);

        heart = (ImageView) rootView.findViewById(R.id.melody_heart);
        lyric1 = (TextView) rootView.findViewById(R.id.lyric1);
        lyric2 = (TextView) rootView.findViewById(R.id.lyric2);
        lyric3 = (TextView) rootView.findViewById(R.id.lyric4);

        mover = (ImageView) rootView.findViewById(R.id.music_handle1);
        Log.d("Started:","yes"+started);
        if (started) {
            Matrix matrix = new Matrix();
            mover.setScaleType(ImageView.ScaleType.MATRIX);
            matrix.set(mover.getImageMatrix());
            float part = ((float) (System.currentTimeMillis() - startTime)) / 217000;
            float move = tot * part;

            matrix.postTranslate(move, 0);

            mover.setImageMatrix(matrix);
        }

        if (lyrics == null) {
            Resources r = getResources();
            this.lyrics = new String[]{
                    r.getString(R.string.playPart1),
                    r.getString(R.string.playPart2),
                    r.getString(R.string.playPart3),
                    r.getString(R.string.playPart4),
                    r.getString(R.string.playPart5),
                    r.getString(R.string.playPart6),
                    r.getString(R.string.playPart7),
                    r.getString(R.string.playPart8),
                    r.getString(R.string.playPart9),
                    r.getString(R.string.playPart10),
                    r.getString(R.string.playPart11),
                    r.getString(R.string.playPart12),
                    r.getString(R.string.playPart13),
                    r.getString(R.string.playPart14),
                    r.getString(R.string.playPart15),
                    r.getString(R.string.playPart16),
                    r.getString(R.string.playPart17),
                    r.getString(R.string.playPart18),
                    r.getString(R.string.playPart19),
                    r.getString(R.string.playPartRef1),
                    r.getString(R.string.playPartRef2),
                    r.getString(R.string.playPartRef3),
                    r.getString(R.string.playPartRef4),
                    r.getString(R.string.playPartRef5),
                    r.getString(R.string.playPartRef6),
                    r.getString(R.string.playPartRef7),
                    r.getString(R.string.playPart20),
                    r.getString(R.string.playPart21),
                    r.getString(R.string.playPart22),
                    r.getString(R.string.playPart23),
                    r.getString(R.string.playPart24),
                    r.getString(R.string.playPart25),
                    r.getString(R.string.playPart26),
                    r.getString(R.string.playPart27),
                    r.getString(R.string.playPart13),
                    r.getString(R.string.playPart14),
                    r.getString(R.string.playPart15),
                    r.getString(R.string.playPart16),
                    r.getString(R.string.playPart17),
                    r.getString(R.string.playPart18),
                    r.getString(R.string.playPart19),
                    r.getString(R.string.playPartRef1),
                    r.getString(R.string.playPartRef2),
                    r.getString(R.string.playPartRef3),
                    r.getString(R.string.playPartRef4),
                    r.getString(R.string.playPartRef5),
                    r.getString(R.string.playPartRef6),
                    r.getString(R.string.playPartRef7),
                    r.getString(R.string.playPart28),
                    r.getString(R.string.playPart29),
                    r.getString(R.string.playPart30),
                    r.getString(R.string.playPart31),
                    r.getString(R.string.playPart32),
                    r.getString(R.string.playPart33),
                    r.getString(R.string.playPart34),
                    r.getString(R.string.playPartRef1),
                    r.getString(R.string.playPartRef2),
                    r.getString(R.string.playPartRef1),
                    r.getString(R.string.playPartRef2),
                    r.getString(R.string.playPartRef3),
                    r.getString(R.string.playPartRef4),
                    r.getString(R.string.playPartRef5),
                    r.getString(R.string.playPartRef6),
                    r.getString(R.string.playPartRef7),
                    r.getString(R.string.playPartRef2),
                    r.getString(R.string.playPartRef3),
                    r.getString(R.string.playPartRef4),
                    r.getString(R.string.playPartRef5),
                    r.getString(R.string.playPartRef6),
                    r.getString(R.string.playPartRef7) };
        }
        play = (ImageView) rootView.findViewById(R.id.countdown_playbutton);
        play.setOnClickListener(new PlayButton());

        rewind = (ImageView) rootView.findViewById(R.id.rewind_button);
        rewind.setOnClickListener(new RewindButton());

        if (!started) {
            lyric1.setText("");
            lyric2.setText("");
            lyric3.setText("");

            playing = false;
            started = false;

        } else {
            if (playing) {

                heart.setVisibility(View.INVISIBLE);
                if (text != delays.length - 1) {
                    lyric3.setText(lyrics[text + 1]);
                }
                if (text != 0) {
                    lyric1.setText(lyrics[text - 1]);
                }
                lyric2.setText(lyrics[text]);

                play.setImageResource(R.drawable.pause);
            } else {
                play.setImageResource(R.drawable.playerbutton);
            }
        }
            startMovingClouds(rootView);

        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();


        return rootView;
    }


    public void startLyrics() {

        factory = new SoundFactory(getContext());
        factory.createLongMedia(songID, false);

        text = 0;
        totTime = 0;
        startTime = System.currentTimeMillis();

        startMover();

        lyric1.setText("");
        lyric3.setText(lyrics[1]);
        lyric2.setText(lyrics[0]);
        r = new LyricsRunnable();
        handler.postDelayed(r, delays[0]);
    }

    public void resumeLyrics(int delay) {
        Log.d("get here","DELAy"+delay);
        if (text != delays.length - 1) {

            lyric3.setText(lyrics[text + 1]);

        }
        if (text != 0) {
            lyric1.setText(lyrics[text - 1]);
        }
        lyric2.setText(lyrics[text]);

        handler.postDelayed(r, delay);
        moveHandler.post(r2);
    }

    private class PlayButton implements OnClickListener {
        @Override
        public void onClick(View v) {

            if (playing) {
                lyric1.setText("");
                lyric2.setText("");
                lyric3.setText("");
                heart.setVisibility(View.VISIBLE);
                handler.removeCallbacks(r);
                moveHandler.removeCallbacks(r2);
                pauseTime = System.currentTimeMillis();
                delay = (totTime + delays[text])
                        - (int) (pauseTime - startTime);
                factory.pause(songID);
                play.setImageResource(R.drawable.playerbutton);
                playing = false;

            } else {
                playing = true;
                if (started) {
                    heart.setVisibility(View.INVISIBLE);
                    startTime += (System.currentTimeMillis() - pauseTime);
                    resumeLyrics(delay);
                    factory.resume(songID);
                } else {
                    heart.setVisibility(View.INVISIBLE);
                    startLyrics();
                    factory.start(songID);
                    started = true;
                    taken = 0;
                }
                play.setImageResource(R.drawable.pause);

            }
        }
    }

    private class RewindButton implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (playing) {
                factory.stopAll();
            }
            if(started) {
                rewindMarker();
                rewindLyrics();
                handler.removeCallbacks(r);
                moveHandler.removeCallbacks(r2);
                playing = false;
                started = false;
                play.setImageResource(R.drawable.playerbutton);
            }
        }
    }

    public void stopMusic() {
        if (playing) {
            factory.pause(songID);
        }
    }

    private void startMover() {

        int[] img_coordinates = new int[2];
        mover.getLocationOnScreen(img_coordinates);

        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        @SuppressWarnings("deprecation")
        int width = display.getWidth(); // deprecated

        tot = width - 2*img_coordinates[0];

        r2 = new moveRunnable();

        moveHandler.post(r2);
    }


    private static void rewindLyrics() {
        heart.setVisibility(View.VISIBLE);
        lyric3.setText("");
        lyric2.setText("");
        lyric1.setText("");
    }


    private class LyricsRunnable implements Runnable {


        @Override
        public void run() {
            totTime += delays[text];
            text++;
            if (text >= delays.length) {
                rewindLyrics();

                started = false;
                playing = false;
                play.setImageResource(R.drawable.playerbutton);
                return;
            }
            if (text == delays.length - 1) {
                lyric3.setText("");
            } else {
                lyric3.setText(lyrics[text + 1]);
            }
            lyric2.setText(lyrics[text]);
            lyric1.setText(lyrics[text - 1]);
            long diff = delays[text]
                    - (System.currentTimeMillis() - startTime - totTime);
            if (diff > 0) {
                handler.postDelayed(this, diff);
            } else {
                handler.postDelayed(this, 0);
            }
        }
    }

    private static void rewindMarker() {
        Matrix matrix = new Matrix();
        mover.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.set(mover.getImageMatrix());

        matrix.postTranslate(-taken, 0);
        mover.setImageMatrix(matrix);
    }

    private class moveRunnable implements Runnable {

        public void run() {
            float part = ((float) (System.currentTimeMillis() - startTime)) / 217000;
            float move = tot * part;
            if (part >= 1) {
                rewindMarker();
                return;
            }
            Matrix matrix = new Matrix();
            mover.setScaleType(ImageView.ScaleType.MATRIX);
            matrix.set(mover.getImageMatrix());
            matrix.postTranslate(move - taken, 0);
            taken = move;

            mover.setImageMatrix(matrix);
            moveHandler.postDelayed(this, 500);
        }
    }

    private void startMovingClouds(View rootView) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;

        moveCloud(R.id.cloud1, R.id.cloud5, width + 200,    width, 10000,   10000, rootView);
        moveCloud(R.id.cloud2, R.id.cloud6, 350,            width, 4000,    13000, rootView);
        moveCloud(R.id.cloud3, R.id.cloud7, width + 100,     width, 13000,   15000, rootView);
        moveCloud(R.id.cloud4, R.id.cloud8, 400,            width, 4000,    8000, rootView);
    }



    @Override
    public void onDestroyView () {
        stopMusic();
        handler.removeCallbacks(r);
        moveHandler.removeCallbacks(r2);

        started = false;
        playing = false;
        stopMusic(); rewindLyrics();
        rewindMarker();
        super.onDestroyView();
    }

    private void moveCloud(int cloud1, int cloud2, int dx1, int width, int dur1, int dur2, View rootView) {

        ImageView cloud = (ImageView) rootView.findViewById(cloud1);
        ImageView movingCloud = (ImageView) rootView.findViewById(cloud2);

        Animation a = new TranslateAnimation(0, dx1, 0, 0);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) movingCloud.getLayoutParams();

        Animation a2 = new TranslateAnimation(0, width + (-lp.leftMargin),0 ,0);
        a2.setInterpolator(new AccelerateDecelerateInterpolator());
        a2.setInterpolator(new LinearInterpolator());
        a2.setRepeatCount(Animation.INFINITE);
        a2.setDuration(dur2);

        a.setAnimationListener(new CloudStartListener(cloud, movingCloud, a2));
        a.setDuration(dur1);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setInterpolator(new LinearInterpolator());
        cloud.startAnimation(a);
    }

    private class CloudStartListener implements Animation.AnimationListener {

        private ImageView view;
        private ImageView movingCloud;
        private Animation anim;

        public CloudStartListener(ImageView view, ImageView movingCloud, Animation anim) {
            this.view = view;
            this.movingCloud = movingCloud;
            this.anim = anim;
        }

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.INVISIBLE);
            if(anim != null)  {
                movingCloud.startAnimation(anim);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

}


