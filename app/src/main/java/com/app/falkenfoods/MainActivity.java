package com.app.falkenfoods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //splash Animation
    Animation topAnimation,middleAnimation, bottomAnimation;
    View falkenlogo;
    TextView Falken, Zencode;
    Button Start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        //set animation
        //topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        // middleAnimation = AnimationUtils.loadAnimation(this,R.anim.middle_animation);
        //bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //link Layout Components to class file
        //falkenlogo = findViewById(R.id.splashlogo);
        //Falken = findViewById(R.id.splashtext);
        //Zencode = findViewById(R.id.foundername);

        //set animation in components
        //falkenlogo.setAnimation(topAnimation);
        //Falken.setAnimation(middleAnimation);
        //Start.setAnimation(bottomAnimation);
        //Zencode.setAnimation(bottomAnimation);

        //set time in splash screen shown
        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    super.run();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        timer.start();
    }
}