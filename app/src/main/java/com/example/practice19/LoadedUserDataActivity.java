package com.example.practice19;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class LoadedUserDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_user_data);
        Init();
    }

    private void Init() {
        ImageView imageForLoaded = findViewById(R.id.imageForLoaded);

        Animation animation = AnimationUtils.loadAnimation(
                this,
                R.anim.logo_rotate_animation
        );

        imageForLoaded.startAnimation(animation);
    }
}
