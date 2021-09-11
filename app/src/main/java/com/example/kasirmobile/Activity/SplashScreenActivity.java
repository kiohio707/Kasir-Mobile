package com.example.kasirmobile.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kasirmobile.R;

public class SplashScreenActivity extends AppCompatActivity {

    //Inisialisasi View
    ImageView ivLogo;
    TextView tvKasirMobile, tvVersi;
    Animation fromTop, fromBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ivLogo = findViewById(R.id.iv_logo);
        tvKasirMobile = findViewById(R.id.tv_kasir_mobile);
        tvVersi = findViewById(R.id.tv_versi);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        fromTop = AnimationUtils.loadAnimation(this,R.anim.from_top);

        //Menyembunyikan toolbar
        getSupportActionBar().hide();

        //Memberi animasi
        ivLogo.setAnimation(fromTop);
        tvKasirMobile.setAnimation(fromBottom);
        tvVersi.setAnimation(fromBottom);

        //Membuat Timer dari ProgressBar
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreenActivity.this, IntroActivity.class));
                finish();
            }
        }.start();
    }
}