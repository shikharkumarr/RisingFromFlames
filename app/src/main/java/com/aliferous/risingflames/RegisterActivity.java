package com.aliferous.risingflames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextView tv_next1,tv_next2,tv_skip,tv_back;
    EditText et_name1,et_name2,et_email,et_password,et_cpassword,et_contact;
    ConstraintLayout page1,page2;
    Switch sw1,sw2,sw3,sw4;

    FirebaseAuth auth;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        tv_next1 = findViewById(R.id.reg_next1);
        tv_next2 = findViewById(R.id.reg_next2);
        tv_back = findViewById(R.id.reg_back);
        tv_skip = findViewById(R.id.reg_skip);

        et_name1 = findViewById(R.id.reg_et_name1);
        et_name2 = findViewById(R.id.reg_et_name2);
        et_email = findViewById(R.id.reg_et_email);
        et_password = findViewById(R.id.reg_et_password);
        et_cpassword = findViewById(R.id.reg_et_cpassword);
        et_contact = findViewById(R.id.reg_et_contact);

        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);
        sw4 = findViewById(R.id.switch4);

        page1 = findViewById(R.id.page1);
        page2 = findViewById(R.id.page2);

        tv_next1.setPaintFlags(tv_next1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_next2.setPaintFlags(tv_next2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_skip.setPaintFlags(tv_skip.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_back.setPaintFlags(tv_back.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        tv_next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String txt_email = Objects.requireNonNull(et_email.getText()).toString().trim();
                String txt_password = Objects.requireNonNull(et_password.getText()).toString().trim();
                String txt_cpassword = Objects.requireNonNull(et_cpassword.getText()).toString().trim();
                String txt_name1 = Objects.requireNonNull(et_name1.getText()).toString().trim();
                String txt_name2 = Objects.requireNonNull(et_name2.getText()).toString().trim();
                String txt_contact = Objects.requireNonNull(et_contact.getText()).toString().trim();

                if(TextUtils.isEmpty(txt_name1)  ||  TextUtils.isEmpty(txt_name2) ||  TextUtils.isEmpty(txt_password)  || TextUtils.isEmpty(txt_cpassword)  ||  TextUtils.isEmpty(txt_email)){
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characters long", Toast.LENGTH_SHORT).show();
                }
                else if (!(txt_password.equals(txt_cpassword)))
                    Toast.makeText(RegisterActivity.this, "Please check your Password", Toast.LENGTH_SHORT).show();


                else
                {
                    final ObjectAnimator oa1 = ObjectAnimator.ofFloat(page1, "translationX", 0f, -1500f);
                    final ObjectAnimator oa2 = ObjectAnimator.ofFloat(page2, "translationX", 1500f, 0f);
                    oa1.setDuration(600);
                    oa2.setDuration(600);
                    oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                    oa1.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            page1.setVisibility(View.GONE);
                        }
                    });
                    oa1.start();
                    oa2.start();
                    page2.setVisibility(View.VISIBLE);
                }


            }
        });

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(page1, "translationX", -1500f, 0f);
                final ObjectAnimator oa2 = ObjectAnimator.ofFloat(page2, "translationX", 0f, 1500f);
                oa1.setDuration(600);
                oa2.setDuration(600);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                oa2.setInterpolator(new AccelerateDecelerateInterpolator());
                oa1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        page2.setVisibility(View.GONE);
                    }
                });
                oa1.start();
                oa2.start();
                page1.setVisibility(View.VISIBLE);
            }
        });

        tv_next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_next2.setAlpha((float) 0.4);

                String txt_email = Objects.requireNonNull(et_email.getText()).toString().trim();
                String txt_password = Objects.requireNonNull(et_password.getText()).toString().trim();
                String txt_name1 = Objects.requireNonNull(et_name1.getText()).toString().trim();
                String txt_name2 = Objects.requireNonNull(et_name2.getText()).toString().trim();
                String txt_contact = Objects.requireNonNull(et_contact.getText()).toString().trim();

                String op1="No",op2="No",op3="No",op4="No";
                if (sw1.isChecked())
                    op1 = "Yes";
                if (sw2.isChecked())
                    op2 = "Yes";
                if (sw3.isChecked())
                    op3 = "Yes";
                if (sw4.isChecked())
                    op4 = "Yes";

                register(txt_name1,txt_name2,txt_email,txt_password,txt_contact,op1,op2,op3,op4);



            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_skip.setAlpha((float) 0.4);
                String txt_email = Objects.requireNonNull(et_email.getText()).toString().trim();
                String txt_password = Objects.requireNonNull(et_password.getText()).toString().trim();
                String txt_name1 = Objects.requireNonNull(et_name1.getText()).toString().trim();
                String txt_name2 = Objects.requireNonNull(et_name2.getText()).toString().trim();
                String txt_contact = Objects.requireNonNull(et_contact.getText()).toString().trim();
                String op1="No",op2="No",op3="No",op4="No";
                register(txt_name1,txt_name2,txt_email,txt_password,txt_contact,op1,op2,op3,op4);
            }
        });

    }



    private void register(final String name1,final String name2, final String email, final String password, final String contact, final String op1, final String op2, final String op3, final String op4) {

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("First Name", name1);
                            hashMap.put("Last Name", name2);
                            hashMap.put("Email", email);
                            hashMap.put("Password", password);
                            hashMap.put("Contact", contact);
                            hashMap.put("Option 1", op1);
                            hashMap.put("Option 2", op2);
                            hashMap.put("Option 3", op3);
                            hashMap.put("Option 4", op4);



                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                        tv_next2.setAlpha(1);
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Illegal Email Id or Password", Toast.LENGTH_SHORT).show();
                            tv_next2.setAlpha(1);
                        }

                    }
                });

    }
}