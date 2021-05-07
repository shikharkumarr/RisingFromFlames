package com.aliferous.risingflames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmergencyActivity extends AppCompatActivity {

    ImageView circle1,circle2;
    Button callFire,callAmbulance,complete;
    MediaPlayer mp;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mp= MediaPlayer.create(EmergencyActivity.this,R.raw.alarm);
        mp.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        mp= MediaPlayer.create(EmergencyActivity.this,R.raw.alarm);

        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mp.start();
                return false;
            }
        });

        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.start();

            }
        });

        mp.start();


        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        callFire = findViewById(R.id.sosCall);
        callAmbulance = findViewById(R.id.sosCall2);
        complete = findViewById(R.id.sosComplete);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Sensor List");


        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(circle1, "scaleX", 0f, 1.3f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(circle1, "scaleY", 0f, 1.3f);
        final ObjectAnimator oa3 = ObjectAnimator.ofFloat(circle2, "scaleX", 0f, 1.3f);
        final ObjectAnimator oa4 = ObjectAnimator.ofFloat(circle2, "scaleY", 0f, 1.3f);
        final ObjectAnimator oa5 = ObjectAnimator.ofFloat(circle1, "alpha", 1f, 0.2f);
        final ObjectAnimator oa6 = ObjectAnimator.ofFloat(circle2, "alpha", 1f, 0.2f);
        final ObjectAnimator oa7 = ObjectAnimator.ofFloat(circle1, "alpha", 0f, 1f);
        final ObjectAnimator oa8 = ObjectAnimator.ofFloat(circle2, "alpha", 0f, 1f);
        final ObjectAnimator oa9 = ObjectAnimator.ofFloat(circle1, "alpha", 1f, 1f);
        final ObjectAnimator oa10 = ObjectAnimator.ofFloat(circle2, "alpha", 1f, 1f);


        oa1.setDuration(1200);
        oa2.setDuration(1200);
        oa3.setDuration(1200);
        oa4.setDuration(1200);
        oa5.setDuration(300);
        oa6.setDuration(300);
        oa7.setDuration(300);
        oa8.setDuration(300);
        oa9.setDuration(600);
        oa10.setDuration(600);


        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new DecelerateInterpolator());
        oa3.setInterpolator(new DecelerateInterpolator());
        oa4.setInterpolator(new DecelerateInterpolator());
        oa5.setInterpolator(new AccelerateInterpolator());
        oa6.setInterpolator(new AccelerateInterpolator());
        oa7.setInterpolator(new DecelerateInterpolator());
        oa8.setInterpolator(new DecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oa1.start();
                oa2.start();
                oa3.start();
                oa4.start();
                oa7.start();
                oa8.start();
                mp.start();
            }
        });
        oa7.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oa9.start();
                oa10.start();

            }
        });
        oa9.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                oa5.start();
                oa6.start();

            }
        });

        oa1.start();
        oa2.start();
        oa3.start();
        oa4.start();
        oa7.start();
        oa8.start();



        callFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone("101");
            }
        });

        callAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialContactPhone("102");
            }
        });

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (int i=1;i<100;i++){
                            if (snapshot.hasChild(""+i)){
                                reference.child(""+i).child("Status").setValue("0");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                mp.stop();
                Intent intent = new Intent(EmergencyActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}