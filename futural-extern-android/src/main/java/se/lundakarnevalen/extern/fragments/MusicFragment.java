package se.lundakarnevalen.extern.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import se.lundakarnevalen.extern.activities.ContentActivity;
import se.lundakarnevalen.extern.android.R;

public class MusicFragment extends LKFragment implements View.OnClickListener {

    private boolean play = true;
    private boolean finish = false;
    private MediaPlayer player;
    private ProgressBar playSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_music, container, false);

        initializeUIElements(rootView);
        initializeMediaPlayer();

        ContentActivity.class.cast(getActivity()).allBottomsUnfocus();


        return rootView;
    }

    private void initializeUIElements(View rootView) {

        playSeekBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        View buttonPlay = rootView.findViewById(R.id.button);
        buttonPlay.setOnClickListener(this);


    }

    public void onClick(View v) {
        if(play) {
            if (checkInternet()) {
                ((ImageView)v).setImageResource(R.drawable.radio_pause);
                play = false;
                startPlaying();
            } else {
                Context context = getActivity().getApplicationContext();
                CharSequence text = context.getString(R.string.no_internet);
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }else{
            ((ImageView)v).setImageResource(R.drawable.radio_play);
           play= true;
                stopPlaying();
            }
    }

    private boolean checkInternet() {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        } else {
            player.release();
            initializeMediaPlayer();
        }
        finish = false;
        playSeekBar.setVisibility(View.INVISIBLE);
    }


    private void startPlaying() {


        playSeekBar.setVisibility(View.VISIBLE);

        player.prepareAsync();

    }


    private void initializeMediaPlayer() {

        player = new MediaPlayer();

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                if(!finish) {
                    finish = true;
                } else {
                    playSeekBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();

            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                Context context = getActivity().getApplicationContext();
                CharSequence text = context.getString(R.string.no_internet);
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                stopPlaying();

                return false;
            }
        });


        try {
            player.setDataSource("http://webradio.af.lu.se:8000/;stream/1");
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onDestroyView () {
        stopPlaying();
        super.onDestroyView();
    }
}
