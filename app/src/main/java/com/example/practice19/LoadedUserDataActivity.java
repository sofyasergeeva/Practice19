package com.example.practice19;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoadedUserDataActivity extends AppCompatActivity {

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_user_data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPlayer = MediaPlayer.create(this, R.raw.noise);

        if (mPlayer != null) {
            mPlayer.setVolume(0.2f, 0.2f);
            mPlayer.setLooping(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        super.onStop();
    }
}
