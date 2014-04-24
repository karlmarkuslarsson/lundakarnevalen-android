package se.lundakarnevalen.extern.fragments;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;

import se.lundakarnevalen.extern.android.R;

public class MusicFragment extends LKFragment implements View.OnClickListener {
    private Button buttonPlay;

    private Button buttonStopPlay;
    private boolean finish = false;
    private MediaPlayer player;
    private ProgressBar playSeekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_music, null);
       // TODO CHECK INTERNET CONNECTION....

        initializeUIElements(rootView);
        initializeMediaPlayer();

        return rootView;
    }

    private void initializeUIElements(View rootView) {

        playSeekBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (Button) rootView.findViewById(R.id.button);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) rootView.findViewById(R.id.buttonStop);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            if(checkInternet()) {
                startPlaying();
            } else {
                // TODO show message...
            }

        } else if (v == buttonStopPlay) {
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
        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }


    private void startPlaying() {

        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);

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


        try {
            player.setDataSource("http://webradio.af.lu.se:8000/;stream/1");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
