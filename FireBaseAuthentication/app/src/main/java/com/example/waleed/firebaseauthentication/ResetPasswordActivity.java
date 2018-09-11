package com.example.waleed.firebaseauthentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
EditText inputEmail;
    Button btnResetPass,btnBack;
    FirebaseAuth auth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_reset_password);
        inputEmail=(EditText) findViewById(R.id.email_forgot_pg);
        btnResetPass=(Button) findViewById(R.id.btn_reset_pass_in_reset);
        btnBack=(Button) findViewById(R.id.btn_back);
        progressBar=(ProgressBar) findViewById(R.id.progressBar_reset);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String email=inputEmail.getText().toString().trim();
                if(email.isEmpty()){
                    inputEmail.setError("Please enter your registered Email id!");
                }
                    else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "We have send you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
