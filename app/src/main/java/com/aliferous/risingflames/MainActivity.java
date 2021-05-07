package com.aliferous.risingflames;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.Do_Dont_Adapter;
import Model.Do_Dont;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity implements Do_Dont_Adapter.Linkrecycler {

    ImageView backgroundSplash,map,Profile,Maintainence,Card_bg;
    TextView SplashText,greeting,det_Text,det_Details;

    FirebaseUser firebaseUser;
    DatabaseReference dbr,reference;

    private RecyclerView do_dont_recycler;
    private Do_Dont_Adapter do_dont_adapter;
    private List<Do_Dont> mUsers;

    String Details,Text,Bg;
    Do_Dont_Adapter.Linkrecycler mListener;


    BlurView background_blur;
    ConstraintLayout cardview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListener = this;

        backgroundSplash = findViewById(R.id.backgroundSplash);
        SplashText = findViewById(R.id.SplashText);
        map = findViewById(R.id.main_map);
        Profile = findViewById(R.id.main_profile);
        Maintainence = findViewById(R.id.main_maintenance);
        do_dont_recycler = findViewById(R.id.do_dont_recycler);
        greeting = findViewById(R.id.greeting);
        cardview = findViewById(R.id.cardview);
        det_Text = findViewById(R.id.det_Text);
        det_Details = findViewById(R.id.det_details);
        Card_bg = findViewById(R.id.detailed_do_bg);
        background_blur = findViewById(R.id.main_background_blur);
        blurBackground();

        mUsers = new ArrayList<>();

        do_dont_recycler.setHasFixedSize(true);
        do_dont_recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = firebaseUser.getUid();
        dbr = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("First Name");
        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue().toString();
                greeting.setText("Welcome "+name);
                SplashText.setText("Hi, "+name+"\nYou are safe.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        read_do_dont();


        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());


        backgroundSplash.getLayoutParams().width = (int) (pixels*1400);
        backgroundSplash.getLayoutParams().height = (int) (pixels*1400);

        map.getLayoutParams().width = (int) (pixels*238);
        map.getLayoutParams().height = (int) (pixels*238);


        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Sensor List");


        emergency();




        backgroundSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(backgroundSplash, "scaleX", 1f, 0.17f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(backgroundSplash, "scaleY", 1f, 0.17f);
                final ObjectAnimator oa3 = ObjectAnimator.ofFloat(SplashText, "alpha", 1f, 0f);
                final ObjectAnimator oa4 = ObjectAnimator.ofFloat(backgroundSplash, "alpha", 1f, 0f);
                oa1.setDuration(600);
                oa2.setDuration(600);
                oa3.setDuration(600);
                oa4.setDuration(400);
                oa1.setInterpolator(new DecelerateInterpolator());
                oa2.setInterpolator(new DecelerateInterpolator());
                oa3.setInterpolator(new AccelerateDecelerateInterpolator());
                oa4.setInterpolator(new AccelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        oa4.start();
                    }
                });
                oa4.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        backgroundSplash.setVisibility(View.GONE);
                    }
                });
                oa1.start();
                oa2.start();
                oa3.start();


            }
        });

        Maintainence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MaintainenceActivity.class);
                startActivity(intent);
            }
        });


        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String toggle1 = dataSnapshot.child("Option 1").getValue().toString();
                        String toggle2 = dataSnapshot.child("Option 2").getValue().toString();
                        String toggle3 = dataSnapshot.child("Option 3").getValue().toString();
                        String toggle4 = dataSnapshot.child("Option 4").getValue().toString();

                        GoToProfile(toggle1,toggle2,toggle3,toggle4);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

    }

    private void emergency() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Sensor List");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=1;i<100;i++){
                    if (snapshot.hasChild(""+i)){
                        String status = snapshot.child(""+i).child("Status").getValue().toString();
                        if (status.equals("1")){
                            Intent intent = new Intent(MainActivity.this,EmergencyActivity.class);
                            startActivity(intent);
                            break;
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GoToProfile(String toggle1, String toggle2, String toggle3, String toggle4) {

        Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
        intent.putExtra("toggle1",toggle1);
        intent.putExtra("toggle2",toggle2);
        intent.putExtra("toggle3",toggle3);
        intent.putExtra("toggle4",toggle4);
        startActivity(intent);

    }

    private void read_do_dont() {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Do Don't");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Do_Dont user = snapshot.getValue(Do_Dont.class);


                    if (user != null && user.getText() != null) {
                        mUsers.add(user);
                    }

                }

                do_dont_adapter = new Do_Dont_Adapter(MainActivity.this,mUsers,false,Details,Text,Bg,mListener);
                do_dont_recycler.setAdapter(do_dont_adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void blurBackground() {

        float radius = 2f;

        background_blur.clearFocus();

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        background_blur.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }



    @Override
    public void linkingrecyler(Do_Dont do_dont, String detail, String text, String bg) {
        Details = detail;
        Text = text;
        Bg = bg;

        if(Bg.equals("G")){
            Card_bg.setBackgroundResource(R.drawable.do_bg);
        }
        else
        {
            Card_bg.setBackgroundResource(R.drawable.dont_bg);
        }

        det_Details.setText(Details);
        det_Text.setText(Text);
        cardview.setVisibility(View.VISIBLE);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardview.setVisibility(View.GONE);
            }
        });
    }
}