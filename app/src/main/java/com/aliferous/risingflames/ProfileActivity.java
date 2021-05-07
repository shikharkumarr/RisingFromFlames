package com.aliferous.risingflames;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;

public class ProfileActivity extends AppCompatActivity {

    Switch sw1,sw2,sw3,sw4;

    DatabaseReference db;
    FirebaseUser firebaseUser;

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String toggle1 = getIntent().getStringExtra("toggle1");
        String toggle2 = getIntent().getStringExtra("toggle2");
        String toggle3 = getIntent().getStringExtra("toggle3");
        String toggle4 = getIntent().getStringExtra("toggle4");

        sw1 = findViewById(R.id.pr_switch1);
        sw2 = findViewById(R.id.pr_switch2);
        sw3 = findViewById(R.id.pr_switch3);
        sw4 = findViewById(R.id.pr_switch4);
        name = findViewById(R.id.Name);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userid = firebaseUser.getUid();
        db = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String full_name = snapshot.child("First Name").getValue().toString()+" "+snapshot.child("Last Name").getValue().toString();
                name.setText(full_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        if (toggle1.equals("Yes"))
            sw1.setChecked(true);
        if (toggle2.equals("Yes"))
            sw2.setChecked(true);
        if (toggle3.equals("Yes"))
            sw3.setChecked(true);
        if (toggle4.equals("Yes"))
            sw4.setChecked(true);


       /* db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String toggle1 = dataSnapshot.child("Option 1").getValue().toString();
                if (toggle1.equals("Yes"))
                    sw1.setChecked(true);
                String toggle2 = dataSnapshot.child("Option 2").getValue().toString();
                if (toggle2.equals("Yes"))
                    sw2.setChecked(true);
                String toggle3 = dataSnapshot.child("Option 3").getValue().toString();
                if (toggle3.equals("Yes"))
                    sw3.setChecked(true);
                String toggle4 = dataSnapshot.child("Option 4").getValue().toString();
                if (toggle4.equals("Yes"))
                    sw4.setChecked(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true)
                    db.child("Option 1").setValue("Yes");
                else if(b==false)
                    db.child("Option 1").setValue("No");
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true)
                    db.child("Option 2").setValue("Yes");
                else if(b==false)
                    db.child("Option 2").setValue("No");
            }
        });

        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true)
                    db.child("Option 3").setValue("Yes");
                else if(b==false)
                    db.child("Option 3").setValue("No");
            }
        });

        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true)
                    db.child("Option 4").setValue("Yes");
                else if(b==false)
                    db.child("Option 4").setValue("No");
            }
        });



    }
}