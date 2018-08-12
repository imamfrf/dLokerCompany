package com.dloker.imam.dlokercompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {

    private ImageView logo;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = (ImageView)findViewById(R.id.img_splash);

        mAuth =FirebaseAuth.getInstance();

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        logo.startAnimation(myanim);

        if(mAuth.getCurrentUser() != null){

            final Intent intent = new Intent(Splash.this, MainActivity.class);
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(5000) ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();

        } else {
            final Intent i = new Intent(this, LoginActivity.class);
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(5000) ;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        finish();
                    }
                }
            };
            timer.start();
        }



    }

}


