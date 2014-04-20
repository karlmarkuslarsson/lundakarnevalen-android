package se.lundakarnevalen.extern.fragments;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import se.lundakarnevalen.extern.android.R;

public class MusicFragment extends LKFragment implements View.OnClickListener {
    private Button buttonPlay;

    private Button buttonStopPlay;

    private MediaPlayer player;


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
            startPlaying();
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }
    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
    }


    private void startPlaying() {

        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);


        player.prepareAsync();

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
    }


    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource("http://webradio.af.lu.se:8000/;stream/1");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });
    }

}
