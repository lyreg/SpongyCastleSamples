package com.uestc.lyreg.spongycastle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Button mButtonDES;
    private Button mButtonRSA;
    private Button mButtonCSR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupWindowAnimations();
        setupToolbar();
        setupLayout();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
    private void setupLayout() {
        mButtonDES = (Button) findViewById(R.id.button_des);
        mButtonRSA = (Button) findViewById(R.id.button_rsa);
        mButtonCSR = (Button) findViewById(R.id.button_cert);
    }
    private void setupWindowAnimations() {
        Log.v(TAG, "setupWindowAnimations");

        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(500);
        getWindow().setExitTransition(slideTransition);
        getWindow().setReenterTransition(slideTransition);
    }


    public void onButtonDesClicked(View v) {
        if(v.getId() == R.id.button_des) {
            Intent intent = new Intent(this, DesActivity.class);
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    public void onButtonRsaClicked(View v) {
        if(v.getId() == R.id.button_rsa) {
            Intent intent = new Intent(this, RsaActivity.class);
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    public void onButtonCsrClicked(View v) {
        if(v.getId() == R.id.button_cert) {
            Intent intent = new Intent(this, CsrActivity.class);
            startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        }
    }
}
