package com.aliferous.risingflames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView tv_signup,bn_signin;
    EditText et_email,et_pass;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        tv_signup = findViewById(R.id.tv_signup);
        bn_signin = findViewById(R.id.signin_submit);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_password);


        tv_signup.setPaintFlags(tv_signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        bn_signin.setPaintFlags(bn_signin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        bn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bn_signin.setAlpha((float) 0.4);
                String txt_email = Objects.requireNonNull(et_email.getText()).toString().trim();
                String txt_password = Objects.requireNonNull(et_pass.getText()).toString().trim();

                if(TextUtils.isEmpty(txt_password)  ||  TextUtils.isEmpty(txt_email)){
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    bn_signin.setAlpha(1);

                }

                else{
                    auth.signInWithEmailAndPassword(txt_email,txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                        bn_signin.setAlpha(1);
                                    }
                                }
                            });
                }
            }
        });

    }
}