package com.mayank.integratingaudioandvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener , SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener{

    // Ui Components
    private VideoView myVideoView;
    private Button btnPlayVideo;
    private Button btnPlayMusic,btnPauseMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar volumeSeekBar;
    private SeekBar moveBackAndForthSeekBar;


    private MediaController mediaController;
    private AudioManager audioManager;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        volumeSeekBar = findViewById(R.id.seekBarVolime);
        moveBackAndForthSeekBar = findViewById(R.id.seekBarMove);


        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);

        mediaController = new MediaController(MainActivity.this);

        mediaPlayer = mediaPlayer.create(this, R.raw.music);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        int maximunVolumeOfUserDevice = audioManager.
                getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolumeOfUserDevice = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maximunVolumeOfUserDevice);
        volumeSeekBar.setProgress(currentVolumeOfUserDevice);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){

                   // Toast.makeText(MainActivity.this, Integer.toString(progress),Toast.LENGTH_LONG).show();
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveBackAndForthSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        moveBackAndForthSeekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(this);
    }


    @Override
    public void onClick(View buttonView) {

        switch (buttonView.getId()){

            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName()
                        + "/" + R.raw.video);

                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);
                myVideoView.start();
                break;

            case R.id.btnPlayMusic:
                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        moveBackAndForthSeekBar.setProgress(mediaPlayer.getCurrentPosition());

                    }
                }, 0, 1000);

                break;

            case R.id.btnPauseMusic:
                mediaPlayer.pause();
                timer.cancel();
                break;

        }



    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if(fromUser){

          //  Toast.makeText(this, Integer.toString(progress),
          //          Toast.LENGTH_LONG).show();
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        mediaPlayer.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaPlayer.start();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        timer.cancel();
        Toast.makeText(this, "Music is Ended", Toast.LENGTH_LONG).show();

    }
}
